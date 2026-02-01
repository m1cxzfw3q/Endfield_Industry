package endfield;

import arc.Core;
import endfield.special.EIConveyorBlock;
import endfield.special.ProtocolCoreBlock;
import endfield.special.ProtocolStorageBoxBlock;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.power.ConsumeGenerator;
import mindustry.world.blocks.power.LongPowerNode;
import mindustry.world.consumers.ConsumeItemList;
import mindustry.world.meta.Stat;

import static mindustry.Vars.world;

public class EIBlocks {//武陵的内容我想等武陵更完再更
    public static Block
    protocolCore, //协议核心
    protocolStorageBox, //协议储存箱
    repeater, //中继器
    conveyor, //传送带
    thermalEnergyPool //热能池

    ;

    public static void load() {
        protocolCore = new ProtocolCoreBlock("protocol-core");
        protocolStorageBox = new ProtocolStorageBoxBlock("protocol-storage-box");

        repeater = new LongPowerNode("repeater") {
            {
                size = 3;
                maxNodes = 1000;
                laserRange = 80;
                autolink = false;
                requirements(Category.power, ItemStack.with(EIItems.crystalShell, 20));
                glowColor = laserColor2 = Pal.powerLight;
                laser = Core.atlas.find("power-beam");
                laserEnd = Core.atlas.find("power-beam-end");
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

        thermalEnergyPool = new ConsumeGenerator("thermal-energy-pool") {{
            size = 2;
            itemDuration = 10 * 60;
            itemDurationMultipliers.put(EIItems.low_capacityValleyBattery, 4);
            itemDurationMultipliers.put(EIItems.medium_capacityValleyBattery, 4);
            itemDurationMultipliers.put(EIItems.high_capacityValleyBattery, 4);
            powerProduction = 50f / 60;

            consume(new ConsumeItemList(){{
                setMultipliers(
                        EIItems.sourceOre, 1,
                        EIItems.low_capacityValleyBattery, 220f / 50,
                        EIItems.medium_capacityValleyBattery, 420f / 50,
                        EIItems.high_capacityValleyBattery, 1100f / 50
                        //,EIItems.low_capacityWulingBattery, 1600f / 50
                );
            }});

            itemCapacity = 50;
            buildType = ThermalEnergyPoolBuild::create;
            requirements(Category.power, ItemStack.with(EIItems.crystalShell, 10, EIItems.amethystComponent, 10));
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
        };
    }
}
