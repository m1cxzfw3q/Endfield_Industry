package endfield.world.consume;

import arc.struct.ObjectFloatMap;
import mindustry.gen.Building;
import mindustry.type.Item;
import mindustry.world.consumers.ConsumeItemEfficiency;

public class EIConsumeItemEfficiency extends ConsumeItemEfficiency {
    public ObjectFloatMap<Item> itemMultipliers = new ObjectFloatMap<>();

    public EIConsumeItemEfficiency(Object... obj){
        super();
        for (int i = 0; i < obj.length; i+=3) {
            itemDurationMultipliers.put((Item)obj[i], (float)obj[i+1]);
            itemMultipliers.put((Item)obj[i], (float)obj[i+2]);
        }
    }

    /** Initializes item efficiency multiplier map. Format: [item1, mult1, item2, mult2...] */
    public void setMultipliers(Object... objects){
        for(int i = 0; i < objects.length; i += 2){
            itemMultipliers.put((Item)objects[i], (Float)objects[i + 1]);
        }
    }

    @Override
    public float efficiencyMultiplier(Building build){
        var item = getConsumed(build);
        return itemMultipliers.get(item, 1f);
    }
}
