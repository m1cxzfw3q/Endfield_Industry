package endfield.special;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Vec2;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.world.blocks.power.ConsumeGenerator;

import static mindustry.Vars.world;

public class ThermalEnergyPoolBlock extends ConsumeGenerator {
    public ThermalEnergyPoolBlock(String name) {
        super(name);
        rotate = true;
        drawArrow = false;
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);
        TextureRegion tr = Core.atlas.find("industry-io");
        switch (rotation) {
            case 0 -> {
                Draw.rect(tr, new Vec2(x - 1 , y), rotation);
                Draw.rect(tr, new Vec2(x - 1 , y + 1), rotation);
            }
            case 1 -> {
                Draw.rect(tr, new Vec2(x, y - 1), rotation);
                Draw.rect(tr, new Vec2(x + 1 , y - 1), rotation);
            }
            case 2 -> {
                Draw.rect(tr, new Vec2(x + 2 , y), rotation);
                Draw.rect(tr, new Vec2(x + 2 , y + 1), rotation);
            }
            case 3 -> {
                Draw.rect(tr, new Vec2(x, y + 2), rotation);
                Draw.rect(tr, new Vec2(x + 1 , y + 2), rotation);
            }
        }
    }

    public class ThermalEnergyPoolBuild extends ConsumeGeneratorBuild {//写世处写的   能跑就行
        @Override
        public boolean acceptItem(Building source, Item item) {
            Building b1 = null, b2 = null;
            switch (rotation) {
                case 0 -> {
                    b1 = world.tile((int) x / 8 - 1 , (int) y / 8).build;
                    b2 = world.tile((int) x / 8 - 1 , (int) y / 8 + 1).build;
                }
                case 1 -> {
                    b1 = world.tile((int) x / 8 , (int) y / 8 - 1).build;
                    b2 = world.tile((int) x / 8 + 1 , (int) y / 8 - 1).build;
                }
                case 2 -> {
                    b1 = world.tile((int) x / 8 + 2 , (int) y / 8).build;
                    b2 = world.tile((int) x / 8 + 2 , (int) y / 8 + 1).build;
                }
                case 3 -> {
                    b1 = world.tile((int) x / 8 , (int) y / 8 + 2).build;
                    b2 = world.tile((int) x / 8 + 1 , (int) y / 8 + 2).build;
                }
            }
            return super.acceptItem(source, item) && (b1 == source || b2 == source);
        }
    }
}
