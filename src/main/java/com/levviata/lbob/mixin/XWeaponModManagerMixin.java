package com.levviata.lbob.mixin;

import com.hbm.items.ModItems;
import com.hbm.items.weapon.sedna.factory.GunFactory;
import com.hbm.items.weapon.sedna.mods.IWeaponMod;
import com.hbm.items.weapon.sedna.mods.WeaponModTestDamage;
import com.hbm.items.weapon.sedna.mods.XWeaponModManager;
import com.levviata.lbob.LeviathanPlayerAttributes;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*
@Mixin(XWeaponModManager.class)
public class XWeaponModManagerMixin {

    @Inject(method = "init", at = @At("RETURN"), remap = false)
    private static void onInit(CallbackInfo ci) {
        IWeaponMod DAMAGE = new WeaponModTestDamage(371, "DAMAGE");
        new XWeaponModManager.WeaponModDefinition(new ItemStack(LeviathanPlayerAttributes.pseudoDamageMod, 1, GunFactory.EnumModTest.MULTI.ordinal())).addDefault(TEST_MULTI);
    }
}
*/