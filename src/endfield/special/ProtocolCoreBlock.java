package endfield.special;

import arc.Core;
import arc.func.Boolf;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Intersector;
import arc.math.geom.Point2;
import arc.struct.EnumSet;
import arc.struct.IntSeq;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.*;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.core.Renderer;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.input.Placement;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.Edges;
import mindustry.world.Tile;
import mindustry.world.blocks.power.PowerGraph;
import mindustry.world.blocks.power.PowerNode;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.*;
import mindustry.world.modules.PowerModule;

import static mindustry.Vars.*;

public class ProtocolCoreBlock extends CoreBlock {
    public boolean isSub = false;
    public float powerProduction = 200 / 60f;
    public int powerStorage = 10000;

    public TextureRegion laser = Core.atlas.find("laser"), laserEnd = Core.atlas.find("laser-end");

    public static float laserRange = 80;
    public int maxNodes = 1000;
    public boolean autolink = true, drawRange = false, sameBlockConnection = false;

    protected final static ObjectSet<PowerGraph> graphs = new ObjectSet<>();

    public ProtocolCoreBlock(String name) {
        super(name);
        size = 9;
        itemCapacity = 8000;
        requirements(Category.effect, BuildVisibility.hidden, ItemStack.with());
        consumesPower = true;
        hasPower = true;
        flags = EnumSet.of(BlockFlag.generator, BlockFlag.core, BlockFlag.battery);

        config(Integer.class, (entity, value) -> {
            PowerModule power = entity.power;
            Building other = world.build(value);
            boolean contains = power.links.contains(value), valid = other != null && other.power != null;

            if(contains){
                //unlink
                power.links.removeValue(value);
                if(valid) other.power.links.removeValue(entity.pos());

                PowerGraph newgraph = new PowerGraph();

                //reflow from this point, covering all tiles on this side
                newgraph.reflow(entity);

                if(valid && other.power.graph != newgraph){
                    //create new graph for other end
                    PowerGraph og = new PowerGraph();
                    //reflow from other end
                    og.reflow(other);
                }
            }else if(linkValid(entity, other) && valid && power.links.size < maxNodes){

                power.links.addUnique(other.pos());

                if(other.team == entity.team){
                    other.power.links.addUnique(entity.pos());
                }

                power.graph.addGraph(other.power.graph);
            }
        });

        config(Point2[].class, (tile, value) -> {
            IntSeq old = new IntSeq(tile.power.links);

            //clear old
            for(int i = 0; i < old.size; i++){
                configurations.get(Integer.class).get(tile, old.get(i));
            }

            //set new
            for(Point2 p : value){
                configurations.get(Integer.class).get(tile, Point2.pack(p.x + tile.tileX(), p.y + tile.tileY()));
            }
        });
    }

    @Override
    public void init() {
        super.init();
        consumePowerBuffered(!isSub ? powerStorage : 0);
    }

    @Override
    public void setStats() {
        super.setStats();

        if (!isSub) stats.add(Stat.powerCapacity, powerStorage, StatUnit.none);
    }

    @Override
    public void setBars(){
        super.setBars();

        if (!isSub) {
            addBar("power", (ProtocolCoreBuild entity) -> new Bar(() ->
                    Core.bundle.format("bar.poweroutput",
                            Strings.fixed(entity.getPowerProduction() * 60 * entity.timeScale(), 1)),
                    () -> Pal.powerBar,
                    () -> 1f
            ));
            addBar("powerstored", (ProtocolCoreBuild entity) -> new Bar(
                    () -> Core.bundle.format("bar.powerstored",
                            Strings.fixed(entity.power.status * powerStorage, 1),
                            powerStorage),
                    () -> Pal.powerBar,
                    () -> entity.power.status
            ));
        }
    }

    @Override
    public void changePlacementPath(Seq<Point2> points, int rotation){
        Placement.calculateNodes(points, this, rotation, (point, other) -> overlaps(world.tile(point.x, point.y), world.tile(other.x, other.y)));
    }

    protected static boolean overlaps(float srcx, float srcy, Tile other, Block otherBlock, float range){
        return Intersector.overlaps(Tmp.cr1.set(srcx, srcy, range), Tmp.r1.setCentered(other.worldx() + otherBlock.offset, other.worldy() + otherBlock.offset,
                otherBlock.size * tilesize, otherBlock.size * tilesize));
    }

    protected boolean overlaps(float srcx, float srcy, Tile other, float range){
        return Intersector.overlaps(Tmp.cr1.set(srcx, srcy, range), other.getHitbox(Tmp.r1));
    }

    protected boolean overlaps(Building src, Building other, float range){
        return overlaps(src.x, src.y, other.tile, range);
    }

    protected boolean overlaps(Tile src, Tile other, float range){
        return overlaps(src.drawx(), src.drawy(), other, range);
    }

    public boolean overlaps(@Nullable Tile src, @Nullable Tile other){
        if(src == null || other == null) return true;
        return Intersector.overlaps(Tmp.cr1.set(src.worldx() + offset, src.worldy() + offset, laserRange * tilesize), Tmp.r1.setSize(size * tilesize).setCenter(other.worldx() + offset, other.worldy() + offset));
    }

    public boolean linkValid(Building tile, Building link){
        return linkValid(tile, link, true);
    }

    public boolean linkValid(Building tile, Building link, boolean checkMaxNodes){
        if(tile == link || link == null || !link.block.hasPower || !link.block.connectedPower || tile.team != link.team || (sameBlockConnection && tile.block != link.block)) return false;

        if(overlaps(tile, link, laserRange * tilesize) || (link.block instanceof PowerNode node && overlaps(link, tile, node.laserRange * tilesize))){
            if(checkMaxNodes && link.block instanceof PowerNode node){
                return link.power.links.size < node.maxNodes || link.power.links.contains(tile.pos());
            }
            return true;
        }
        return false;
    }

    protected void setupColor(float satisfaction){
        Draw.color(Tmp.c1.set(Color.white).lerp(Pal.powerLight, (1f - satisfaction) * 0.86f + Mathf.absin(3f, 0.1f)).a(Renderer.laserOpacity));
    }

    public void drawLaser(float x1, float y1, float x2, float y2, int size1, int size2) {
        Drawf.laser(laser, laserEnd, x1, y1, x2, y2, 0.25f);
    }

    /** Iterates through linked nodes of a block at a tile. All returned buildings are power nodes. */
    public static void getNodeLinks(Tile tile, Block block, Team team, Cons<Building> others){
        Boolf<Building> valid = other -> other != null && other.tile != tile && other.block instanceof PowerNode node &&
                node.autolink &&
                other.power.links.size < node.maxNodes &&
                overlaps(other.x, other.y, tile, block, node.laserRange * tilesize) && other.team == team
                && !graphs.contains(other.power.graph) &&
                !PowerNode.insulated(tile, other.tile) &&
                !Structs.contains(Edges.getEdges(block.size), p -> { //do not link to adjacent buildings
                    var t = world.tile(tile.x + p.x, tile.y + p.y);
                    return t != null && t.build == other;
                });

        tempBuilds.clear();
        graphs.clear();

        //add conducting graphs to prevent double link
        for(var p : Edges.getEdges(block.size)){
            Tile other = tile.nearby(p);
            if(other != null && other.team() == team && other.build != null && other.build.power != null
                    && !(block.consumesPower && other.block().consumesPower && !block.outputsPower && !other.block().outputsPower)){
                graphs.add(other.build.power.graph);
            }
        }

        if(tile.build != null && tile.build.power != null){
            graphs.add(tile.build.power.graph);
        }

        var rangeWorld = laserRange * tilesize;
        var tree = team.data().buildingTree;
        if(tree != null){
            tree.intersect(tile.worldx() - rangeWorld, tile.worldy() - rangeWorld, rangeWorld * 2, rangeWorld * 2, build -> {
                if(valid.get(build) && !tempBuilds.contains(build)){
                    tempBuilds.add(build);
                }
            });
        }

        tempBuilds.sort((a, b) -> {
            int type = -Boolean.compare(a.block instanceof PowerNode, b.block instanceof PowerNode);
            if(type != 0) return type;
            return Float.compare(a.dst2(tile), b.dst2(tile));
        });

        tempBuilds.each(valid, t -> {
            graphs.add(t.power.graph);
            others.get(t);
        });
    }

    protected static BuildPlan otherReq;

    @Override
    public void drawPlanConfigTop(BuildPlan plan, Eachable<BuildPlan> list){
        if(plan.config instanceof Point2[] ps){
            setupColor(1f);
            for(Point2 point : ps){
                int px = plan.x + point.x, py = plan.y + point.y;
                otherReq = null;
                list.each(other -> {
                    if(other.block != null
                            && (px >= other.x - ((other.block.size-1)/2) && py >= other.y - ((other.block.size-1)/2) && px <= other.x + other.block.size/2 && py <= other.y + other.block.size/2)
                            && other != plan && other.block.hasPower){
                        otherReq = other;
                    }
                });

                if(otherReq == null || otherReq.block == null) continue;

                drawLaser(plan.drawx(), plan.drawy(), otherReq.drawx(), otherReq.drawy(), size, otherReq.block.size);
            }
            Draw.color();
        }
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        return isSub || !team.cores().contains(b -> b instanceof ProtocolCoreBuild);
    }

    @Override
    public boolean canReplace(Block other) {
        return false;
    }

    protected int returnInt = -1;

    protected void getPotentialLinks(Tile tile, Team team, Cons<Building> others){
        if(!autolink) return;

        Boolf<Building> valid = other -> other != null && other.tile != tile && other.block.connectedPower && other.power != null &&
                (other.block.outputsPower || other.block.consumesPower || other.block instanceof PowerNode) &&
                overlaps(tile.x * tilesize + offset, tile.y * tilesize + offset, other.tile, laserRange * tilesize) && other.team == team &&
                !graphs.contains(other.power.graph) &&
                !PowerNode.insulated(tile, other.tile) &&
                !(other instanceof PowerNode.PowerNodeBuild obuild && obuild.power.links.size >= ((PowerNode)obuild.block).maxNodes);

        tempBuilds.clear();
        graphs.clear();

        //add conducting graphs to prevent double link
        for(var p : Edges.getEdges(size)){
            Tile other = tile.nearby(p);
            if(other != null && other.team() == team && other.build != null && other.build.power != null){
                graphs.add(other.build.power.graph);
            }
        }

        if(tile.build != null && tile.build.power != null){
            graphs.add(tile.build.power.graph);
        }

        var worldRange = laserRange * tilesize;
        var tree = team.data().buildingTree;
        if(tree != null){
            tree.intersect(tile.worldx() - worldRange, tile.worldy() - worldRange, worldRange * 2, worldRange * 2, build -> {
                if(valid.get(build) && !tempBuilds.contains(build)){
                    tempBuilds.add(build);
                }
            });
        }

        tempBuilds.sort((a, b) -> {
            int type = -Boolean.compare(a.block instanceof PowerNode, b.block instanceof PowerNode);
            if(type != 0) return type;
            return Float.compare(a.dst2(tile), b.dst2(tile));
        });

        returnInt = 0;

        tempBuilds.each(valid, t -> {
            if(returnInt ++ < maxNodes){
                graphs.add(t.power.graph);
                others.get(t);
            }
        });
    }

    public class ProtocolCoreBuild extends CoreBuild {
        public float generateTime;

        @Override
        public float getPowerProduction(){
            return enabled ? powerProduction: 0f;
        }

        @Override
        public float warmup(){
            return enabled ? 1f : 0f;
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
            write.f(generateTime);
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            if(revision >= 1){
                generateTime = read.f();
            }
        }

        @Override
        public void draw(){
            super.draw();

            if(Mathf.zero(Renderer.laserOpacity) || isPayload() || team == Team.derelict) return;

            Draw.z(Layer.power);
            setupColor(power.graph.getSatisfaction());

            for(int i = 0; i < power.links.size; i++){
                Building link = world.build(power.links.get(i));

                if(!linkValid(this, link)) continue;

                if(link.block instanceof PowerNode && link.id >= id) continue;

                drawLaser(x, y, link.x, link.y, size, link.block.size);
            }

            Draw.reset();
        }

        @Override
        public void updateTile() {
            super.updateTile();
            //感谢MinRi2大佬的讲解
            //不过我还是喜欢堆史山（）
            if (power != null && power.graph != null) {
                if (power.graph.producers != null && !power.graph.producers.contains(this)) power.graph.producers.add(this);
                if (power.graph.batteries != null && !power.graph.batteries.contains(this)) power.graph.batteries.add(this);
            }

            if(!net.client() || power.links.size <= 0) {
                getPotentialLinks(tile, team, other -> {
                    if (!power.links.contains(other.pos())) {
                        configureAny(other.pos());
                    }
                });
            }
        }

        @Override
        public Point2[] config(){
            Point2[] out = new Point2[power.links.size];
            for(int i = 0; i < out.length; i++){
                out[i] = Point2.unpack(power.links.get(i)).sub(tile.x, tile.y);
            }
            return out;
        }
    }
}
