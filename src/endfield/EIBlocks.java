package endfield;

import arc.scene.ui.layout.Table;
import mindustry.content.Fx;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.StackConveyor;
import mindustry.world.blocks.power.LongPowerNode;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.blocks.storage.StorageBlock;
import mindustry.world.meta.BuildVisibility;

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
            }

            @Override
            public void setBars() {
                super.setBars();
                removeBar("connections");
            }
        };

        conveyor = new StackConveyor("conveyor") {
            {
                itemCapacity = 1;
                speed = 0.5f / 60;
                noSideBlend = true;
                loadEffect = unloadEffect = Fx.none;
                targetable = false;
                requirements(Category.distribution, ItemStack.with());
            }
        };
    }
}
