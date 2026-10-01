package com.levviata.lbob.mod;

import com.hbm.items.weapon.sedna.Receiver;

import com.hbm.items.weapon.sedna.mods.WeaponModBase;
import net.minecraft.item.ItemStack;

import java.util.Objects;
/*
public class DamageMod extends WeaponModBase {

    public DamageMod(int id, String... slots) {
        super(id, slots);
        this.setPriority(PRIORITY_MULT_FINAL);
    }

    @Override
    public <T> T eval(T base, ItemStack gun, String key, Object parent) {

        if(parent instanceof Receiver && Objects.equals(key, Receiver.F_BASEDAMAGE) && base instanceof Float) {
            return cast((Float) base * 1.5F, base);
        }

        return base;
    }
}
*/