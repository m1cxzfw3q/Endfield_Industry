package endfield;

import mindustry.mod.Mod;

public class EICore extends Mod {
    public EICore() {

    }

    @Override
    public void loadContent() {
        EIItems.load();
        EILiquids.load();
        EIBlocks.load();
    }
}
