package endfield.special;

import arc.Core;
import arc.math.Mathf;
import arc.struct.EnumSet;
import arc.struct.Seq;
import arc.util.Strings;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.ui.Bar;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BlockFlag;
import mindustry.world.meta.BlockStatus;
import mindustry.world.meta.BuildVisibility;

public class ProtocolCoreBlock extends CoreBlock {
    public boolean isSub = false;
    public float powerProduction = 200 / 60f;

    public ProtocolCoreBlock(String name) {
        super(name);
        size = 9;
        itemCapacity = 8000;
        requirements(Category.effect, BuildVisibility.hidden, ItemStack.with());
        consumePowerBuffered(10000);
        outputsPower = true;
        consumesPower = true;
        hasPower = true;
        flags = EnumSet.of(BlockFlag.generator, BlockFlag.core, BlockFlag.battery);
    }

    @Override
    public void setBars(){
        super.setBars();

        if(hasPower && outputsPower){
            addBar("power", (ProtocolCoreBlock.ProtocolCoreBuild entity) -> new Bar(() ->
                    Core.bundle.format("bar.poweroutput",
                            Strings.fixed(entity.getPowerProduction() * 60 * entity.timeScale(), 1)),
                    () -> Pal.powerBar,
                    () -> entity.productionEfficiency));
        }
    }

    public class ProtocolCoreBuild extends CoreBuild {
        public float generateTime;
        /** The efficiency of the producer. An efficiency of 1.0 means 100% */
        public float productionEfficiency = 0.0f;

        @Override
        public float getPowerProduction(){
            return enabled ? powerProduction * productionEfficiency : 0f;
        }

        @Override
        public float warmup(){
            return enabled ? productionEfficiency : 0f;
        }

        @Override
        public void overwrote(Seq<Building> previous){
            for(Building other : previous){
                if(other.power != null && other.block.consPower != null && other.block.consPower.buffered){
                    float amount = other.block.consPower.capacity * other.power.status;
                    power.status = Mathf.clamp(power.status + amount / consPower.capacity);
                }
            }
        }

        @Override
        public BlockStatus status(){
            if(Mathf.equal(power.status, 0f, 0.001f)) return BlockStatus.noInput;
            if(Mathf.equal(power.status, 1f, 0.001f)) return BlockStatus.active;
            return BlockStatus.noOutput;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            write.f(productionEfficiency);
            write.f(generateTime);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            productionEfficiency = read.f();
            if(revision >= 1){
                generateTime = read.f();
            }
        }
    }
}
