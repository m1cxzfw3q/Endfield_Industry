package endfield.special;

import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.liquid.ArmoredConduit;

public class EIConduitBlock extends ArmoredConduit {
    public EIConduitBlock(String name) {
        super(name);
        liquidCapacity = 1;
        noSideBlend = true;
        targetable = false;
        requirements(Category.liquid, ItemStack.with());
    }
}
