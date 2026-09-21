package com.levviata.lbob;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

import static com.levviata.lbob.LeviathanPlayerAttributes.*;

// this class provides flags that are used in LNBTMagic
@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class PlayerUseBobblehead {
    public static boolean usedStrengthBob = false;
    public static boolean usedNightVisionBob = false;
    public static boolean usedMaxHealthBob = false;
    public static boolean usedResistanceBob = false;
    public static boolean usedMovementSpeedBob = false;
    public static boolean usedLuckBob = false;

    @SubscribeEvent
    public void use(PlayerInteractEvent.RightClickItem event) {
        //ItemStack bob = new ItemStack(Objects.requireNonNull(Item.getByNameOrId("hbm:bobblehead")));
        ItemStack bob = new ItemStack(Objects.requireNonNull(Item.getByNameOrId("minecraft:iron_ingot")));

        LOGGER.info("Hi");
        LOGGER.info(bob);
        String s = String.valueOf(event.getItemStack().getItem().getRegistryName());
        LOGGER.info(s);

        ItemStack stack = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();

        // the goal is when a hbm:bobblehead is crouch right-clicked, I remove the item and I set a flag as true which is handled by LNBTMagic.class
        // 1 strength
        // 2 toggable night vision, keybind
        // 3 max health
        // 4 resistance
        // 5 random enchanment book
        // 6 movement speed
        // 7 luck
        if (stack.getItem().equals(bob.getItem())  && player.isSneaking()) {
            LOGGER.info("conditions passed");
            switch(bob.getItemDamage()){
                case 0: {
                    LOGGER.info("hi again");
                    usedStrengthBob = true;
                    stack.setCount(stack.getCount() - 1);
                    break;
                }
                case 2: {
                    usedNightVisionBob = true;
                    break;
                }
                case 3: {

                }
            }


        }
        if (stack.getItem().equals(bob.getItem()) && bob.getItemDamage() == 3 && player.isSneaking()) {
            LOGGER.info("i got the iron_ingot");

        }
    }
}
