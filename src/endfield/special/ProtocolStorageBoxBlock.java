package endfield.special;

import arc.scene.ui.layout.Table;
import endfield.EIItems;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.storage.StorageBlock;

public class ProtocolStorageBoxBlock extends StorageBlock {
    public ProtocolStorageBoxBlock(String name) {
        super(name);
        size = 3;
        itemCapacity = 300;
        coreMerge = false;

        config(Boolean.class, (StorageBuild building, Boolean b) -> {

        });
        requirements(Category.effect, ItemStack.with(EIItems.crystalShell, 20));
        consumePower(5 / 60f);
    }

    public class ProtocolStorageBoxBuild extends StorageBuild {
        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);
        }
    }
}
