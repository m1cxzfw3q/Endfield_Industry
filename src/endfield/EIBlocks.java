package endfield;

import arc.scene.ui.layout.Table;
import arc.util.io.Reads;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Building;
import mindustry.gen.Teamc;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.Edges;
import mindustry.world.Tile;
import mindustry.world.blocks.distribution.ArmoredConveyor;
import mindustry.world.blocks.power.LongPowerNode;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.storage.StorageBlock;
import mindustry.world.meta.BuildVisibility;

import static mindustry.Vars.content;

public class EIBlocks {//武陵的内容我想等武陵更完再更
    public static Block
    protocolCore, //协议核心
    protocolStorageBox, //协议储存箱
    repeater, //中继器
    conveyor //传送带
    ;

    public static void load() {
        protocolCore = new CoreBlock("protocol-core") {
            {
                size = 9;
                itemCapacity = 8000;
                requirements(Category.effect, BuildVisibility.hidden, ItemStack.with());
                consumePowerBuffered(10000);
                outputsPower = true;
            }
            @Override
            protected void initBuilding() {
                buildType = ProtocolCoreBuild::create;
            }
            class ProtocolCoreBuild extends CoreBuild {
                @Override
                public float getPowerProduction(){
                    return enabled ? 200f / 60 : 0;
                }
            }
        };

        protocolStorageBox = new StorageBlock("protocol-storage-box") {
            {
                size = 3;
                itemCapacity = 300;
                coreMerge = false;

                config(Boolean.class, (StorageBuild building, Boolean b) -> {

                });
                requirements(Category.effect, ItemStack.with());
            }
            @Override
            protected void initBuilding() {
                buildType = ProtocolStorageBoxBuild::create;
            }
            class ProtocolStorageBoxBuild extends StorageBuild {
                @Override
                public void buildConfiguration(Table table) {
                    super.buildConfiguration(table);
                }
            }
        };

        repeater = new LongPowerNode("repeater") {
            {
                size = 3;
                maxNodes = 1000;
                laserRange = 80;
                autolink = false;
                requirements(Category.power, ItemStack.with());
                glowColor = Pal.powerLight;
            }
            @Override
            public void setBars() {
                super.setBars();
                removeBar("connections");
            }
        };

        conveyor = new ArmoredConveyor("conveyor") {
            {
                itemCapacity = 1;
                speed = 0.5f / 60;
                noSideBlend = true;
                targetable = false;
                requirements(Category.distribution, ItemStack.with());
            }
            @Override
            protected void initBuilding() {
                buildType = EIConveyorBuild::create;
            }
            class EIConveyorBuild extends ArmoredConveyorBuild {
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
                    return (((direction == 0) && minitem >= 0.4f) || ((direction % 2 == 1) && minitem > 0.7f)) && !(source.block.rotate && next == source);
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
        };
    }
}
