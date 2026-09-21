package com.levviata.lplayeratt;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.collection.parallel.ParIterableLike;

import java.util.Objects;
import java.util.UUID;

import static com.levviata.lplayeratt.LeviathanPlayerAttributes.*;

// this class provides flags that are used in LNBTMagic
@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class PlayerUseBobblehead {

    private boolean usedMaxHealthBob = false;
    private boolean usedFollowRangeBob = false;
    private boolean usedKnockbackResistanceBob = false;
    private boolean usedMovementSpeedBob = false;
    private boolean usedFlyingSpeedBob = false;
    private boolean usedAttackDamageBob = false;
    private boolean usedAttackSpeedBob = false;
    private boolean usedArmorBob = false;
    private boolean usedArmorToughnessBob = false;
    private boolean usedLuckBob = false;

    @SubscribeEvent
    public void use(PlayerInteractEvent.RightClickItem event) {
        ItemStack key = new ItemStack(Objects.requireNonNull(Item.getByNameOrId("minecraft:iron_ingot")));
        LOGGER.info("Hi");
        LOGGER.info(key);
        String s = String.valueOf(event.getItemStack().getItem().getRegistryName());
        LOGGER.info(s);
        ItemStack stack = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();

        // the goal is when a hbm:bobblehead is crouch right-clicked, I remove the item and I set a flag as true which is handled by LNBTMagic.class
        // config options added because why not
        if (stack.getItem().equals(key.getItem()) && player.isSneaking()) {
            LOGGER.info("i got the iron_ingot");
            SharedMonsterAttributes
        }
    }
}
