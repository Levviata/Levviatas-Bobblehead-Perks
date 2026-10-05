package com.levviata.lbob;

import com.hbm.items.weapon.sedna.BulletConfig;
import com.hbm.items.weapon.sedna.mags.IMagazine;
import com.hbm.particle.SpentCasing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import static com.levviata.lbob.LeviathanPlayerAttributes.luckScore;

public class RefundMagazine implements IMagazine<BulletConfig> {

    private final IMagazine<BulletConfig> original;

    public RefundMagazine(IMagazine<BulletConfig> original) {
        this.original = original;
    }

    @Override
    public BulletConfig getType(ItemStack stack, IInventory inventory) {
        return original.getType(stack, inventory);
    }

    @Override
    public void setType(ItemStack stack, BulletConfig type) {
        original.setType(stack, type);
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return original.getCapacity(stack);
    }

    @Override
    public int getAmount(ItemStack stack, IInventory inventory) {
        return original.getAmount(stack, inventory);
    }

    @Override
    public void setAmount(ItemStack stack, int amount) {
        original.setAmount(stack, amount);
    }

    @Override
    public void useUpAmmo(ItemStack stack, IInventory inventory, int amount) {
        original.useUpAmmo(stack, inventory, amount);
    }

    @Override
    public boolean canReload(ItemStack stack, IInventory inventory) {
        return original.canReload(stack, inventory);
    }

    @Override
    public void initNewType(ItemStack stack, IInventory inventory) {
        original.initNewType(stack, inventory);
    }

    @Override
    public void reloadAction(ItemStack stack, IInventory inventory) {

        // Capture this BEFORE NTM performs its reload.
        int before = original.getAmount(stack, inventory);

        // Let NTM do the actual reload.
        original.reloadAction(stack, inventory);

        // Capture the result AFTER the reload.
        int after = original.getAmount(stack, inventory);

        int ammoDebt = Math.abs(after - before);

        if (ammoDebt <= 0) {
            return;
        }

        boolean refundAmmo = Math.random() <
                Math.min(luckScore.getScorePoints() * 0.05, 0.5);

        if (!refundAmmo) {
            return;
        }

        BulletConfig config = original.getType(stack, inventory);

        if (config == null || config.ammo == null) {
            return;
        }

        ItemStack refund = config.ammo.getStack().copy();
        refund.setCount(ammoDebt);

        if (inventory instanceof InventoryPlayer) {
            EntityPlayer player = ((InventoryPlayer) inventory).player;
            player.inventory.addItemStackToInventory(refund);
        }
    }
    @Override
    public ItemStack getIconForHUD(ItemStack stack, EntityPlayer player) {
        return original.getIconForHUD(stack, player);
    }

    @Override
    public String reportAmmoStateForHUD(ItemStack stack, EntityPlayer player) {
        return original.reportAmmoStateForHUD(stack, player);
    }

    @Override
    public SpentCasing getCasing(ItemStack stack, IInventory inventory) {
        return original.getCasing(stack, inventory);
    }

    @Override
    public void setAmountBeforeReload(ItemStack stack, int amount) {
        original.setAmountBeforeReload(stack, amount);
    }

    @Override
    public int getAmountBeforeReload(ItemStack stack) {
        return original.getAmountBeforeReload(stack);
    }

    @Override
    public void setAmountAfterReload(ItemStack stack, int amount) {
        original.setAmountAfterReload(stack, amount);
    }

    @Override
    public int getAmountAfterReload(ItemStack stack) {
        return original.getAmountAfterReload(stack);
    }
}
