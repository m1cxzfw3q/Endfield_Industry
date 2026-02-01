package endfield.special;

import arc.util.io.Reads;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Building;
import mindustry.gen.Teamc;
import mindustry.type.Category;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.Edges;
import mindustry.world.Tile;
import mindustry.world.blocks.distribution.ArmoredConveyor;

import static mindustry.Vars.content;

public class EIConveyorBlock extends ArmoredConveyor {
    public EIConveyorBlock(String name) {
        super(name);
        itemCapacity = 1;
        speed = 0.008f;
        displayedSpeed = 0.5f;
        noSideBlend = true;
        targetable = false;
        requirements(Category.distribution, ItemStack.with());
    }

    public class EIConveyorBuild extends ArmoredConveyorBuild {
        public final int capacityX = 1;
        {
            ids = new Item[capacityX];
            xs = new float[capacityX];
            ys = new float[capacityX];
        }

        @Override
        public void handleStack(Item item, int amount, Teamc source){
            amount = Math.min(amount, capacityX - len);

            for(int i = amount - 1; i >= 0; i--){
                add(0);
                xs[0] = 0;
                ys[0] = i * 0.4f;
                ids[0] = item;
                items.add(item, 1);
            }

            noSleep();
        }

        @Override
        public boolean acceptItem(Building source, Item item){
            if(len >= capacityX) return false;
            Tile facing = Edges.getFacingEdge(source.tile, tile);
            if(facing == null) return false;
            int direction = Math.abs(facing.relativeTo(tile.x, tile.y) - rotation);
            return (((direction == 0) && minitem >= 0.4f) || ((direction % 2 == 1) && minitem > 0.7f)) && !(source.block.rotate && next == source)
                    && (source.block instanceof EIConveyorBlock || Edges.getFacingEdge(source.tile, tile).relativeTo(tile) == rotation);
        }

        @Override
        public void handleItem(Building source, Item item){
            if(len >= capacityX) return;

            int r = rotation;
            Tile facing = Edges.getFacingEdge(source.tile, tile);
            int ang = ((facing.relativeTo(tile.x, tile.y) - r));
            float x = (ang == -1 || ang == 3) ? 1 : (ang == 1 || ang == -3) ? -1 : 0;

            noSleep();
            items.add(item, 1);

            if(Math.abs(facing.relativeTo(tile.x, tile.y) - r) == 0){ //idx = 0
                add(0);
                xs[0] = x;
                ys[0] = 0;
                ids[0] = item;
            }else{ //idx = mid
                add(mid);
                xs[mid] = x;
                ys[mid] = 0.5f;
                ids[mid] = item;
            }
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            int amount = read.i();
            len = Math.min(amount, capacityX);

            for(int i = 0; i < amount; i++){
                short id;
                float x, y;

                if(revision == 0){
                    int val = read.i();
                    id = (short)(((byte)(val >> 24)) & 0xff);
                    x = (float)((byte)(val >> 16)) / 127f;
                    y = ((float)((byte)(val >> 8)) + 128f) / 255f;
                }else{
                    id = read.s();
                    x = (float)read.b() / 127f;
                    y = ((float)read.b() + 128f) / 255f;
                }

                if(i < capacityX){
                    ids[i] = content.item(id);
                    xs[i] = x;
                    ys[i] = y;
                }
            }

            //this updates some state
            updateTile();
        }

        @Override
        public void setProp(UnlockableContent content, double value){
            if(content instanceof Item item && items != null){
                int amount = Math.min((int)value, capacityX);
                if(items.get(item) != amount){
                    if(items.get(item) < amount){
                        handleStack(item, amount - items.get(item), null);
                    }else if(amount >= 0){
                        removeStack(item, items.get(item) - amount);
                    }
                }
            }else super.setProp(content, value);
        }
    }
}
