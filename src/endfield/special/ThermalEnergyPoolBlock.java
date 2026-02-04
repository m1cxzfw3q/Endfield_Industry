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
    public void drawPlace(int x, int y, int rotation, boolean valid) {//写世处写的   能跑就行
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

    public class ThermalEnergyPoolBuild extends ConsumeGeneratorBuild {
        @Override
        public boolean acceptItem(Building source, Item item) {
            return super.acceptItem(source, item) && source.rotation == rotation;
        }
    }
}
