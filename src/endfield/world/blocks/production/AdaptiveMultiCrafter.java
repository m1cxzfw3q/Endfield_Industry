package endfield.world.blocks.production;

import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.Styles;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.consumers.ConsumeItemDynamic;
import mindustry.world.consumers.ConsumeLiquidsDynamic;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatValues;

public class AdaptiveMultiCrafter extends GenericCrafter {
    public Seq<Recipe> recipes = new Seq<>();
    public AdaptiveMultiCrafter(String name) {
        super(name);
    }

    @Override
    public void init() {
        consume(new ConsumeItemDynamic(
                (AdaptiveMultiCrafterBuild build) ->
                        build.currentRecipe != null ? build.currentRecipe.inputItems : ItemStack.empty
        ));

        consume(new ConsumeLiquidsDynamic(
                (AdaptiveMultiCrafterBuild build) ->
                        build.currentRecipe != null ? new LiquidStack[]{build.currentRecipe.inputLiquid} : LiquidStack.empty
        ));

        if (!recipes.isEmpty()) {
            for (var recipe : recipes) {
                for (var item : recipe.inputItems) {
                    itemFilter[item.item.id] = true;
                }

                liquidFilter[recipe.inputLiquid.liquid.id] = true;
            }
        }

        super.init();
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.remove(Stat.productionTime);
        stats.remove(Stat.output);
        stats.remove(Stat.input);

        stats.add(Stat.output, table -> {
            table.row();
            for (Recipe recipe : recipes) {
                table.table(Styles.grayPanel, t -> {
                    t.left();
                    for (ItemStack it : recipe.inputItems) {
                        table.add(StatValues.displayItem(it.item, it.amount, craftTime, true)).pad(5f);
                    }
                    table.add(StatValues.displayLiquid(recipe.inputLiquid.liquid, recipe.inputLiquid.amount, true)).pad(5f);
                    t.image(Icon.right).color(Pal.darkishGray).size(40).pad(5f).fill();
                    for (ItemStack it : recipe.outputItems) {
                        table.add(StatValues.displayItem(it.item, it.amount, craftTime, true)).pad(5f);
                    }
                    table.add(StatValues.displayLiquid(recipe.outputLiquid.liquid, recipe.outputLiquid.amount, true)).pad(5f);
                });
                table.row();
            }
        });
    }

    public class AdaptiveMultiCrafterBuild extends GenericCrafterBuild {
        public @Nullable Recipe currentRecipe;

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

        @Override
        public void updateTile() {
            for (Recipe recipe : recipes) {
                if (items.has(recipe.inputItems) &&liquids.get(recipe.inputLiquid.liquid) >= 1) currentRecipe = recipe;
            }
            if (currentRecipe != null) {
                craftTime = currentRecipe.craftTime;
                outputItems = currentRecipe.outputItems;
                outputLiquids = new LiquidStack[]{currentRecipe.outputLiquid};
            }

            super.updateTile();
        }
    }

    public static class Recipe {
        public ItemStack[] inputItems, outputItems;
        public LiquidStack inputLiquid, outputLiquid;
        public float craftTime = 120f;

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
