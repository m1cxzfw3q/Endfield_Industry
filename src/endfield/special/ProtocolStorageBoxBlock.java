package endfield.special;

import arc.scene.ui.layout.Table;
import endfield.EIItems;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Iconc;
import mindustry.type.Category;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.blocks.storage.StorageBlock;

public class ProtocolStorageBoxBlock extends StorageBlock {
    public ProtocolStorageBoxBlock(String name) {
        super(name);
        size = 3;
        itemCapacity = 300;
        coreMerge = false;

        config(Boolean.class, (ProtocolStorageBoxBuild building, Boolean b) -> {
                building.autoReturn = b;
        });
        requirements(Category.effect, ItemStack.with(EIItems.crystalShell, 20));
        consumePower(5 / 60f);
    }

    public class ProtocolStorageBoxBuild extends StorageBuild {
        public boolean autoReturn = true;
        float itemReturnProgress = 0;

        @Override
        public void buildConfiguration(Table table) {
            table.table(t -> t.button("\uE87B", () -> autoReturn =! autoReturn));
        }

        @Override
        public void updateTile() {
            super.updateTile();

            if (autoReturn) {
                itemReturnProgress++;
                if (itemReturnProgress / 600 >= 1) {
                    team.core().items.add(items);
                    items.clear();
                    itemReturnProgress = 0;
                }
            }
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return super.acceptItem(source, item) && source.rotation == rotation;
        }
    }
}
