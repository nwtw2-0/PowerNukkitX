package cn.nukkit.block;

import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityFallingBlock;
import cn.nukkit.event.block.BlockFallEvent;
import cn.nukkit.level.Position;
import cn.nukkit.nbt.tag.*;


/**
 * @author rcsuperman (Nukkit Project)
 */
public abstract class BlockFallable extends BlockSolid {

    protected BlockFallable() {
    }

    @Override
    public int onUpdate(int type) {
        Block down = this.down();
        if ((down.getId() == AIR || down instanceof BlockFire || down instanceof BlockLiquid ||
                (down instanceof BlockBubbleColumn && down.getLevelBlockAtLayer(1) instanceof BlockLiquid)) &&
                !BlockPistonBase.isBlockLocked(new Position(down.x, down.y, down.z, this.getLevel()))) {
            BlockFallEvent event = new BlockFallEvent(this);
            this.level.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return type;
            }

            drop(new CompoundTag());
        }
        return type;
    }

    @PowerNukkitOnly
    public void drop(CompoundTag customNbt) {
        this.level.setBlock(this, Block.get(Block.AIR), true, true);
        EntityFallingBlock fall = createFallingEntity(customNbt);

        fall.spawnToAll();
    }

    @PowerNukkitOnly
    protected EntityFallingBlock createFallingEntity(CompoundTag customNbt) {
        CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", this.x + 0.5))
                        .add(new DoubleTag("", this.y))
                        .add(new DoubleTag("", this.z + 0.5)))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0)))

                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", 0))
                        .add(new FloatTag("", 0)))
                .putInt("TileID", this.getId())
                .putByte("Data", this.getDamage());

        for (Tag customTag : customNbt.getAllTags()) {
            nbt.put(customTag.getName(), customTag.copy());
        }

        EntityFallingBlock fall = (EntityFallingBlock) Entity.createEntity("FallingSand", this.getLevel().getChunk((int) this.x >> 4, (int) this.z >> 4), nbt);

        if (fall != null) {
            fall.spawnToAll();
        }

        return fall;
    }
}
