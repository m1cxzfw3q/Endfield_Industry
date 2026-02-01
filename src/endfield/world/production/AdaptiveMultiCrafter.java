package endfield.world.production;

import arc.struct.Seq;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.blocks.production.GenericCrafter;

public class AdaptiveMultiCrafter extends GenericCrafter {
    public Seq<Recipe> recipes = new Seq<>();
    public AdaptiveMultiCrafter(String name) {
        super(name);
    }

    public class AdaptiveMultiCrafterBuild extends GenericCrafterBuild {
        @Override
        public void dumpOutputs(){ //神经
            boolean b = true;
            while (b){
                if (outputItems != null && timer(timerDump, dumpTime / timeScale)) {
                    for (ItemStack output : outputItems) {
                        dump(output.item);
                    }
                }

                if (outputLiquids != null) {
                    for (int i = 0; i < outputLiquids.length; i++) {
                        int dir = liquidOutputDirections.length > i ? liquidOutputDirections[i] : -1;

                        dumpLiquid(outputLiquids[i].liquid, 2f, dir);
                    }
                }
                b = false;
            }
        }
    }

    public class Recipe {
        public ItemStack[] inputItems, outputItems;
        public LiquidStack inputLiquid, outputLiquid;
        public int craftTime = 120;

        public Recipe(int time) {
            craftTime = time;
        }
        public Recipe() {}
        public Recipe(ItemStack[] inputItems, LiquidStack inputLiquid, int time, ItemStack[] outputItems, LiquidStack outputLiquid) {
            this(time);
            this.inputItems = inputItems;
            this.inputLiquid = inputLiquid;
            this.outputItems = outputItems;
            this.outputLiquid = outputLiquid;
        }
    }
}
