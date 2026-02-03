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

    // ,Xirang, XirangEquipmentPart, low_capacityWulingBattery
    //息壤 息壤装备原件 低容武陵电池
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

        sandLeaf = new Item("sand-leaf", Color.valueOf("F0BD2F"));
        sandLeafSeeds = new Item("sand-leaf-seeds", Color.valueOf("F5E300"));

        ketoneShrub = new Item("ketone-shrub", Color.valueOf("757552"));
        ketoneTreeSeeds = new Item("ketone-tree-seeds", Color.valueOf("DEDC81"));

        carbonBlock = new Item("carbon-block", Color.valueOf("2F332F")) {{
            flammability = 1.3f;
            explosiveness = 0.4f;
        }};

        crystalShell = new Item("crystal-shell", Color.valueOf("EEC96D"));
        amethystFiber = new Item("amethyst-fiber", Color.valueOf("9372B8"));
        blueIronBlock = new Item("blue-iron-block", Color.valueOf("539FBB"));

        stableCarbonBlock = new Item("stable-carbon-block", Color.valueOf("62656F")) {{
            flammability = 1.5f;
        }};

        denseCrystalline = new Item("dense-crystalline", Color.valueOf("CF9B66"));
        highCrystalFiber = new Item("high-crystal-fiber", Color.valueOf("D4C9D7"));
        steelBlock = new Item("steel-block", Color.valueOf("4DC4D1"));

        carbonPowder = new Item("carbon-powder", Color.valueOf("3D3D3D")) {{
            flammability = 0.65f;
        }};
        sourceOrePowder = new Item("source-ore-powder", Color.valueOf("BF9038"));
        crystalShellPowder = new Item("crystal-shell-powder", Color.valueOf("C1441C"));
        amethystPowder = new Item("amethyst-powder", Color.valueOf("63548A"));
        blueIronPowder = new Item("blue-iron-powder", Color.valueOf("4B7293"));
        sandLeafPowder = new Item("sand-leaf-powder", Color.valueOf("C08C00"));
        ketonicShrubPowder = new Item("ketone-shrub-powder", Color.valueOf("C7BF47"));

        denseCarbonPowder = new Item("dense-carbon-powder", Color.valueOf("5F5F5F"));
        denseOriginiumPowder = new Item("dense-originium-powder", Color.valueOf("C99413"));
        denseCrystalPowder = new Item("dense-crystal-powder", Color.valueOf("D9995A"));
        highCrystalPowder = new Item("high-crystal-powder", Color.valueOf("CCCBE0"));
        denseBlueIronPowder = new Item("dense-blue-iron-powder", Color.valueOf("4485DE"));

        purpleCrystalBottle = new Item("purple-crystal-bottle", Color.valueOf("5C5599"));
        blueIronBottle = new Item("blue-iron-bottle", Color.valueOf("21577C"));
        highCrystalBottle = new Item("high-crystal-bottle", Color.valueOf("A3A0CC"));
        steelBottle = new Item("steel-bottle", Color.valueOf("517499"));

        amethystComponent = new Item("amethyst-component", Color.valueOf("996CBF"));
        ironComponent = new Item("iron-component", Color.valueOf("3060D5"));
        highCrystalComponent = new Item("high-crystal-component", Color.valueOf("BFC9CD"));
        steelComponent = new Item("steel-component", Color.valueOf("23305C"));

        amethystEquipmentPart = new Item("amethyst-equipment-part", Color.valueOf("ffffff"));
        blueIronEquipmentPart = new Item("blue-iron-equipment-part", Color.valueOf("ffffff"));
        highCrystalEquipmentPart = new Item("high-crystal-equipment-part", Color.valueOf("ffffff"));

        low_capacityValleyBattery = new Item("low-capacity-valley-battery", Color.valueOf("ffffff"));
        medium_capacityValleyBattery = new Item("medium-capacity-valley-battery", Color.valueOf("ffffff"));
        high_capacityValleyBattery = new Item("high-capacity-valley-battery", Color.valueOf("ffffff"));


        /*
        Xirang = new Item("Xirang", Color.valueOf("46655A"));
        XirangEquipmentPart = new Item("Xirang-equipment-part", Color.valueOf("ffffff"));

        low_capacityWulingBattery = new Item("low-capacity-wuling-battery", Color.valueOf("ffffff"));
         */
    }
}
