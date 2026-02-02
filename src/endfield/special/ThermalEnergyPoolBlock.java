package endfield.special;

import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.world.blocks.power.ConsumeGenerator;

import static mindustry.Vars.world;

public class ThermalEnergyPoolBlock extends ConsumeGenerator {
    public ThermalEnergyPoolBlock(String name) {
        super(name);
        rotate = true;
    }
    public class ThermalEnergyPoolBuild extends ConsumeGeneratorBuild {//写世处写的   能跑就行
        @Override
        public boolean acceptItem(Building source, Item item) {
            Building b1 = null, b2 = null;
            switch (rotation) {
                case 0 -> {
                    b1 = world.tile((int) x / 8 + 2 , (int) y / 8).build;
                    b2 = world.tile((int) x / 8 + 2 , (int) y / 8 + 1).build;
                }
                case 1 -> {
                    b1 = world.tile((int) x / 8 , (int) y / 8 + 2).build;
                    b2 = world.tile((int) x / 8 + 1 , (int) y / 8 + 2).build;
                }
                case 2 -> {
                    b1 = world.tile((int) x / 8 - 1 , (int) y / 8).build;
                    b2 = world.tile((int) x / 8 - 1 , (int) y / 8 + 1).build;
                }
                case 3 -> {
                    b1 = world.tile((int) x / 8 , (int) y / 8 - 1).build;
                    b2 = world.tile((int) x / 8 + 1 , (int) y / 8 - 1).build;
                }
            }
            return super.acceptItem(source, item) && (b1 == source || b2 == source);
        }
    }
}
