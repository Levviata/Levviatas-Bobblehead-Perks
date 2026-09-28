package com.levviata.lbob;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


import static com.levviata.lbob.LBAttributeModifier.*;
import static com.levviata.lbob.LeviathanPlayerAttributes.LOGGER;

public class ConstantTags {
    //public void onTick(EntityEvent )

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        LOGGER.info("something happened");
        EntityPlayer oldPlayer = event.getOriginal();
        EntityPlayer newPlayer = event.getEntityPlayer();

        NBTTagCompound oldData = oldPlayer.getEntityData();
        NBTTagCompound newData = newPlayer.getEntityData();

        if (oldData.hasKey(EntityPlayer.PERSISTED_NBT_TAG, 10)) {
            newData.setTag(EntityPlayer.PERSISTED_NBT_TAG, oldData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).copy());
        }

        NBTTagCompound data = newData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);

        if (!data.hasKey(ATTACK_DAMAGE_TAG)) {
            data.setInteger(ATTACK_DAMAGE_TAG, 0);
        }

        if (!data.hasKey(ATTACK_DAMAGE_BONUS_TAG)) {
            data.setBoolean(ATTACK_DAMAGE_BONUS_TAG, false);
        }

        if (!data.hasKey(GUN_ACCURACY_TAG)) {
            data.setInteger(GUN_ACCURACY_TAG, 0);
        }

        if (!data.hasKey(NIGHT_VISION_TAG)) {
            data.setBoolean(NIGHT_VISION_TAG, false);
        }

        if (!data.hasKey(MAX_HEALTH_TAG)) {
            data.setDouble(MAX_HEALTH_TAG, 0.0D);
        }

        if (!data.hasKey(MAX_HEALTH_BONUS_TAG)) {
            data.setBoolean(MAX_HEALTH_BONUS_TAG, false);
        }

        if (!data.hasKey(ARMOR_TAG)) {
            data.setDouble(ARMOR_TAG, 0.0D);
        }

        if (!data.hasKey(ARMOR_TOUGHNESS_TAG)) {
            data.setDouble(ARMOR_TOUGHNESS_TAG, 0.0D);
        }

        if (!data.hasKey(FOLLOW_RANGE_TAG)) {
            data.setDouble(FOLLOW_RANGE_TAG, 0.0D);
        }

        if (!data.hasKey(KNOCKBACK_RESISTANCE_TAG)) {
            data.setDouble(KNOCKBACK_RESISTANCE_TAG, 0.0D);
        }

        if (!data.hasKey(MOVEMENT_SPEED_TAG)) {
            data.setDouble(MOVEMENT_SPEED_TAG, 0.0D);
        }

        if (!data.hasKey(FLYING_SPEED_TAG)) {
            data.setDouble(FLYING_SPEED_TAG, 0.0D);
        }

        if (!data.hasKey(GUN_DAMAGE_TAG)) {
            data.setDouble(GUN_DAMAGE_TAG, 0.0D);
        }

        if (!data.hasKey(ATTACK_SPEED_TAG)) {
            data.setDouble(ATTACK_SPEED_TAG, 0.0D);
        }

        if (!data.hasKey(LUCK_TAG)) {
            data.setDouble(LUCK_TAG, 0.0D);
        }
    }
}
