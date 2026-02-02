package endfield;

import endfield.special.EIConveyorBlock;
import endfield.special.ProtocolCoreBlock;
import endfield.special.ProtocolStorageBoxBlock;
import endfield.special.ThermalEnergyPoolBlock;
import mindustry.graphics.Drawf;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.consumers.ConsumeItemList;
import mindustry.world.meta.Stat;

public class EIBlocks {//武陵的内容我想等武陵更完再更
    public static Block
    /* 基础(没管道) */
    protocolCore, //协议核心
    subCore, //次级核心
    conveyor, //传送带
    /* 仓储(四号谷地) */
    protocolStorageBox, //协议储存箱
    /* 生产(四号谷地) */
    /* 制造(四号谷地) */
    /* 电力(四号谷地) */
    repeater, //中继器
    thermalEnergyPool //热能池
    /* 功能 */
    /* 战斗 */

    ;

    public static void load() {
        protocolCore = new ProtocolCoreBlock("protocol-core");
        subCore = new ProtocolCoreBlock("sub-core");
        protocolStorageBox = new ProtocolStorageBoxBlock("protocol-storage-box");

        repeater = new PowerNode("repeater") {
            {
                size = 3;
                maxNodes = 1000;
                laserRange = 80;
                autolink = false;
                requirements(Category.power, ItemStack.with(EIItems.crystalShell, 20));
                drawRange = false;
            }
            @Override
            public void setBars() {
                super.setBars();
                removeBar("connections");
            }

            @Override
            public void setStats() {
                super.setStats();
                stats.remove(Stat.powerConnections);
            }

            @Override
            public void drawLaser(float x1, float y1, float x2, float y2, int size1, int size2) {
                Drawf.laser(laser, laserEnd, x1, y1, x2, y2, laserScale);
            }
        };

        conveyor = new EIConveyorBlock("conveyor");

        thermalEnergyPool = new ThermalEnergyPoolBlock("thermal-energy-pool") {{
            size = 2;
            itemDuration = 10f * 60;
            itemDurationMultipliers.put(EIItems.low_capacityValleyBattery, 4);
            itemDurationMultipliers.put(EIItems.medium_capacityValleyBattery, 4);
            itemDurationMultipliers.put(EIItems.high_capacityValleyBattery, 4);
            powerProduction = 50f / 60;

            consume(new ConsumeItemList(){{
                setMultipliers(
                        EIItems.sourceOre, 1f,
                        EIItems.low_capacityValleyBattery, 220f / 50,
                        EIItems.medium_capacityValleyBattery, 420f / 50,
                        EIItems.high_capacityValleyBattery, 1100f / 50
                        //,EIItems.low_capacityWulingBattery, 1600f / 50
                );
            }});

            itemCapacity = 50;
            requirements(Category.power, ItemStack.with(EIItems.crystalShell, 10, EIItems.amethystComponent, 10));
            consumePower(0f); //接入电网才能运作
            consumesPower = true;
        }};
    }
}
