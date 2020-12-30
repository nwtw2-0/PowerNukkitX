package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.api.PowerNukkitDifference;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/*
 * @author Pub4Game
 * @since 2015/12/11
 */
public class BlockRedstone extends BlockSolidMeta {

    public BlockRedstone() {
        this(0);
    }

    public BlockRedstone(int meta) {
        super(0);
    }

    @Override
    public int getId() {
        return REDSTONE_BLOCK;
    }

    @Override
    public double getResistance() {
        return 10;
    }

    @Override
    public double getHardness() {
        return 5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return "Redstone Block";
    }

    @Override
    public int getToolTier() {
        return ItemTool.TIER_WOODEN;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.REDSTONE_BLOCK_COLOR;
    }

    @Override
    @PowerNukkitOnly
    @PowerNukkitDifference(info = "Update around redstone", since = "1.4.0.0-PN")
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if (super.place(item, block, target, face, fx, fy, fz, player)) {
            level.updateAroundRedstone(this.getLocation(), null);

            return true;
        }
        return false;
    }

    @Override
    @PowerNukkitOnly
    @PowerNukkitDifference(info = "Update around redstone", since = "1.4.0.0-PN")
    public boolean onBreak(Item item) {
        if (!super.onBreak(item)) {
            return false;
        }

        level.updateAroundRedstone(this.getLocation(), null);

        return true;
    }

    @Override
    @PowerNukkitDifference(info = "Update around redstone if moved", since = "1.4.0.0-PN")
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_MOVED) {
            level.updateAroundRedstone(this.getLocation(), null);
            return type;
        }
        return 0;
    }

    @Override
    public boolean isPowerSource() {
        return true;
    }

    @Override
    public int getWeakPower(BlockFace face) {
        return 15;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
