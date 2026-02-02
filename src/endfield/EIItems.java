package endfield;

import arc.graphics.Color;
import mindustry.type.Item;

public class EIItems {
    public static Item
    /* 矿物 */
    sourceOre, amethystOre, vivianite,
    //源矿 紫晶矿 蓝铁矿

    /* 植物和对应的种子  仅保留工业需要的植物 */
    sandLeaf, sandLeafSeeds,  ketoneShrub, ketoneTreeSeeds,
    //砂叶 砂叶种子 酮化灌木 酮化树种

    /* 工业产物(四号谷地) */
    carbonBlock, crystalShell, amethystFiber, blueIronBlock, stableCarbonBlock, denseCrystalline,
    //碳块 晶体外壳 紫晶纤维 蓝铁块 稳定碳块 密制晶体

    highCrystalFiber, steelBlock, carbonPowder, sourceOrePowder, crystalShellPowder, amethystPowder, blueIronPowder, sandLeafPowder,
    //高晶纤维 钢块 碳粉末 源石粉末 晶体外壳粉末 紫晶粉末 蓝铁粉末 砂叶粉末

    ketonicShrubPowder, denseCarbonPowder, denseOriginiumPowder, denseCrystalPowder, highCrystalPowder, denseBlueIronPowder, purpleCrystalBottle,
    //酮化灌木粉末 致密碳粉末 致密源石粉末 致密晶体粉末 高晶粉末 致密蓝铁粉末 紫晶质瓶

    blueIronBottle, highCrystalBottle, steelBottle, amethystComponent, ironComponent, highCrystalComponent, steelComponent, amethystEquipmentPart,
    blueIronEquipmentPart,
    //蓝铁瓶 高晶质瓶 钢质瓶 紫晶零件 铁质零件 高晶零件 钢制零件 紫晶装备原件 蓝铁装备元件

    highCrystalEquipmentPart, low_capacityValleyBattery, medium_capacityValleyBattery, high_capacityValleyBattery
    //高晶装备原件 低容谷地电池 中容谷地电池 高容谷地电池

    /* 工业产物(武陵) */
    //TODO 武陵的内容我想等武陵更完再更

    // ,Xirang, XirangEquipmentPart, low_capacityWulingBattery //息壤 息壤装备原件 低容武陵电池
    ;

    public static void load() {
        sourceOre = new Item("source-ore", Color.valueOf("D06C16")) {{
            hardness = 1;
        }};

        amethystOre = new Item("amethyst-ore", Color.valueOf("745991")) {{
            hardness = 2;
        }};

        vivianite = new Item("vivianite", Color.valueOf("5276AA")) {{
            hardness = 3;
        }};

        sandLeaf = new Item("sand-leaf", Color.valueOf("ffffff"));
        sandLeafSeeds = new Item("sand-leaf-seeds", Color.valueOf("ffffff"));

        ketoneShrub = new Item("ketone-shrub", Color.valueOf("ffffff"));
        ketoneTreeSeeds = new Item("ketone-tree-seeds", Color.valueOf("ffffff"));

        carbonBlock = new Item("carbon-block", Color.valueOf("ffffff")) {{
            flammability = 1.3f;
            explosiveness = 0.4f;
        }};

        crystalShell = new Item("crystal-shell", Color.valueOf("ffffff"));
        amethystFiber = new Item("amethyst-fiber", Color.valueOf("ffffff"));
        blueIronBlock = new Item("blue-iron-block", Color.valueOf("ffffff"));

        stableCarbonBlock = new Item("stable-carbon-block", Color.valueOf("ffffff")) {{
            flammability = 1.5f;
        }};

        denseCrystalline = new Item("dense-crystalline", Color.valueOf("ffffff"));
        highCrystalFiber = new Item("high-crystal-fiber", Color.valueOf("ffffff"));
        steelBlock = new Item("steel-block", Color.valueOf("ffffff"));

        carbonPowder = new Item("carbon-powder", Color.valueOf("ffffff")) {{
            flammability = 0.65f;
        }};
        sourceOrePowder = new Item("source-ore-powder", Color.valueOf("ffffff"));
        crystalShellPowder = new Item("crystal-shell-powder", Color.valueOf("ffffff"));
        amethystPowder = new Item("amethyst-powder", Color.valueOf("ffffff"));
        blueIronPowder = new Item("blue-iron-powder", Color.valueOf("ffffff"));
        sandLeafPowder = new Item("sand-leaf-powder", Color.valueOf("ffffff"));
        ketonicShrubPowder = new Item("ketone-shrub-powder", Color.valueOf("ffffff"));

        denseCarbonPowder = new Item("dense-carbon-powder", Color.valueOf("ffffff"));
        denseOriginiumPowder = new Item("dense-originium-powder", Color.valueOf("ffffff"));
        denseCrystalPowder = new Item("dense-crystal-powder", Color.valueOf("ffffff"));
        highCrystalPowder = new Item("high-crystal-powder", Color.valueOf("ffffff"));
        denseBlueIronPowder = new Item("dense-blue-iron-powder", Color.valueOf("ffffff"));

        purpleCrystalBottle = new Item("purple-crystal-bottle", Color.valueOf("ffffff"));
        blueIronBottle = new Item("blue-iron-bottle", Color.valueOf("ffffff"));
        highCrystalBottle = new Item("high-crystal-bottle", Color.valueOf("ffffff"));
        steelBottle = new Item("steel-bottle", Color.valueOf("ffffff"));

        amethystComponent = new Item("amethyst-component", Color.valueOf("ffffff"));
        ironComponent = new Item("iron-component", Color.valueOf("ffffff"));
        highCrystalComponent = new Item("high-crystal-component", Color.valueOf("ffffff"));
        steelComponent = new Item("steel-component", Color.valueOf("ffffff"));

        amethystEquipmentPart = new Item("amethyst-equipment-part", Color.valueOf("ffffff"));
        blueIronEquipmentPart = new Item("blue-iron-equipment-part", Color.valueOf("ffffff"));
        highCrystalEquipmentPart = new Item("high-crystal-equipment-part", Color.valueOf("ffffff"));

        low_capacityValleyBattery = new Item("low-capacity-valley-battery", Color.valueOf("ffffff"));
        medium_capacityValleyBattery = new Item("medium-capacity-valley-battery", Color.valueOf("ffffff"));
        high_capacityValleyBattery = new Item("high-capacity-valley-battery", Color.valueOf("ffffff"));


        /*
        Xirang = new Item("Xirang", Color.valueOf("ffffff"));
        XirangEquipmentPart = new Item("Xirang-equipment-part", Color.valueOf("ffffff"));

        low_capacityWulingBattery = new Item("low-capacity-wuling-battery", Color.valueOf("ffffff"));
         */
    }
}
