package endfield.world.consume;

import arc.Core;
import arc.func.Boolf;
import arc.func.Floatf;
import arc.struct.ObjectFloatMap;
import arc.util.Nullable;
import arc.util.Scaling;
import arc.util.Strings;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.ui.Styles;
import mindustry.world.consumers.ConsumeItemEfficiency;
import mindustry.world.meta.*;

import static mindustry.Vars.content;
import static mindustry.world.meta.StatValues.fixValue;

public class EIConsumeItemEfficiency extends ConsumeItemEfficiency {
    public ObjectFloatMap<Item> itemMultipliers = new ObjectFloatMap<>();

    public EIConsumeItemEfficiency(Object... obj){
        super();
        itemDurationMultipliers = new ObjectFloatMap<>();
        for (int i = 0; i < obj.length; i+=3) {
            itemDurationMultipliers.put((Item)obj[i], (float)obj[i+1]);
            itemMultipliers.put((Item)obj[i], (float)obj[i+2]);
        }
        filter = item -> itemMultipliers.containsKey(item);
        booster = true;
    }

    @Override
    public float efficiencyMultiplier(Building build){
        var item = getConsumed(build);
        return item == null ? 0f : itemEfficiencyMultiplier(item);
    }

    public float itemEfficiencyMultiplier(Item item){
        return itemMultipliers.get(item, 1f);
    }
}
