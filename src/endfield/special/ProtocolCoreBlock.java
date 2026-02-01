package endfield.special;

import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;

public class ProtocolCoreBlock extends CoreBlock {
    public ProtocolCoreBlock(String name) {
        super(name);
        size = 9;
        itemCapacity = 8000;
        requirements(Category.effect, BuildVisibility.hidden, ItemStack.with());
        consumePowerBuffered(10000);
        outputsPower = true;
        consumesPower = true;
        hasPower = true;
    }

    public class ProtocolCoreBuild extends CoreBuild {
        @Override
        public float getPowerProduction(){
            return enabled ? 200f / 60 : 0;
        }
    }
}
