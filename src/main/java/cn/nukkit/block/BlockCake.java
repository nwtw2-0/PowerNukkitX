package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.api.PowerNukkitOnly;
import cn.nukkit.api.Since;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.IntBlockProperty;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemCake;
import cn.nukkit.item.food.Food;
import cn.nukkit.level.Level;
import cn.nukkit.level.vibration.VibrationEvent;
import cn.nukkit.level.vibration.VibrationType;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.BlockColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Nukkit Project Team
 */
public class BlockCake extends BlockTransparentMeta {

    @PowerNukkitOnly
    @Since("1.5.0.0-PN")
    public static final IntBlockProperty BITES = new IntBlockProperty("bite_counter", false, 6);

    @PowerNukkitOnly
    @Since("1.5.0.0-PN")
    public static final BlockProperties PROPERTIES = new BlockProperties(BITES);

    public BlockCake(int meta) {
        super(meta);
    }

    public BlockCake() {
        this(0);
    }

    @Override
    public String getName() {
        return "Cake Block";
    }

    @Override
    public int getId() {
        return CAKE_BLOCK;
    }

    @Since("1.4.0.0-PN")
    @PowerNukkitOnly
    @Nonnull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 0.5;
    }

    @PowerNukkitOnly
    @Override
    public int getWaterloggingLevel() {
        return 1;
    }

    @Override
    public double getMinX() {
        return this.x + (1 + getDamage() * 2) / 16;
    }

    @Override
    public double getMinY() {
        return this.y;
    }

    @Override
    public double getMinZ() {
        return this.z + 0.0625;
    }

    @Override
    public double getMaxX() {
        return this.x - 0.0625 + 1;
    }

    @Override
    public double getMaxY() {
        return this.y + 0.5;
    }

    @Override
    public double getMaxZ() {
        return this.z - 0.0625 + 1;
    }

    @Override
    public boolean place(@Nonnull Item item, @Nonnull Block block, @Nonnull Block target, @Nonnull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if (down().getId() != Block.AIR) {
            getLevel().setBlock(block, this, true, true);

            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (down().getId() == Block.AIR) {
                getLevel().setBlock(this, Block.get(BlockID.AIR), true);

                return Level.BLOCK_UPDATE_NORMAL;
            }
        }

        return 0;
    }

    @Override
    public Item[] getDrops(Item item) {
        return Item.EMPTY_ARRAY;
    }

    @Override
    public Item toItem() {
        return new ItemCake();
    }

    @Override
    public boolean onActivate(@Nonnull Item item, Player player) {
        if (item.getId() >= BlockID.CANDLE && item.getId() <= BlockID.BLACK_CANDLE) {
            return false;
        }
        if (player != null && (player.getFoodData().getLevel() < player.getFoodData().getMaxLevel() || player.isCreative())) {
            if (getDamage() <= 0x06) setDamage(getDamage() + 1);
            if (getDamage() >= 0x06) {
                getLevel().setBlock(this, Block.get(BlockID.AIR), true);
            } else {
                Food.getByRelative(this).eatenBy(player);
                getLevel().setBlock(this, this, true);
            }
            this.level.getVibrationManager().callVibrationEvent(new VibrationEvent(player, this.add(0.5, 0.5, 0.5), VibrationType.EAT));
            return true;
        }
        return false;
    }

    @Override
    public BlockColor getColor() {
        return BlockColor.AIR_BLOCK_COLOR;
    }

    @Override
    public int getComparatorInputOverride() {
        return (7 - this.getDamage()) * 2;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    @PowerNukkitOnly
    public boolean breaksWhenMoved() {
        return true;
    }

    @Override
    @PowerNukkitOnly
    public boolean sticksToPiston() {
        return false;
    }
}
