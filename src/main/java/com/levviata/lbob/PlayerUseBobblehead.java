package com.levviata.lbob;

import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

import static com.levviata.lbob.LeviathanPlayerAttributes.*;
import static com.levviata.lbob.LBAttributeModifier.*;

// this class provides flags that are used in LNBTMagic
@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class PlayerUseBobblehead {
    public static boolean usedStrengthBob = false;
    public static boolean usedPerceptionBob = false;
    public static boolean usedEnduranceBob = false;
    public static boolean usedCharismaBob = false;
    public static boolean usedIntelligenceBob = false;
    public static boolean usedAgilityBob = false;
    public static boolean usedLuckBob = false;

    @SubscribeEvent
    public void use(PlayerInteractEvent.RightClickItem event) {
        //ItemStack bob = new ItemStack(Objects.requireNonNull(Item.getByNameOrId("hbm:bobblehead")));
        ItemStack bob = new ItemStack(Objects.requireNonNull(Item.getByNameOrId("minecraft:dye"))); // test

        LOGGER.info("Hi");
        LOGGER.info(bob);
        String s = String.valueOf(event.getItemStack().getItem().getRegistryName());
        LOGGER.info(s);

        ItemStack stack = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();

        // i write to the player data with my desired tags, this is persistent as its stored to the disk (i think)
        NBTTagCompound playerData = player.getEntityData();

        checkBob(stack, player, bob);

        addTags(player, playerData);
    }

    private void checkBob(ItemStack stack, EntityPlayer player, ItemStack bob) {
        // the goal is when a hbm:bobblehead is crouch right-clicked, I remove the item and I set a flag as true which is handled by LNBTMagic.class
        if (stack.getItem().equals(bob.getItem()) && player.isSneaking()) {
            LOGGER.info("conditions passed");
            // String sa = Objects.requireNonNull(stack.get.;
            //LOGGER.info("bob is {}", sa);
            switch (stack.getItemDamage()) {
                case 1: {
                    LOGGER.info("used strength bob");
                    usedStrengthBob = true;
                    stack.setCount(stack.getCount() - 1);
                    break;
                }
                case 2: {
                    usedPerceptionBob = true;
                    stack.setCount(stack.getCount() - 1);
                    break;
                }
                case 3: {
                    LOGGER.info("used health bob");
                    usedEnduranceBob = true;
                    stack.setCount(stack.getCount() - 1);
                    break;
                }
                case 4: {
                    usedCharismaBob = true;
                    stack.setCount(stack.getCount() - 1);
                    break;
                }
                case 5: {
                    usedIntelligenceBob = true;
                    stack.setCount(stack.getCount() - 1);
                    break;
                }
                case 6: {
                    usedAgilityBob = true;
                    stack.setCount(stack.getCount() - 1);
                    break;
                }
                case 8: {
                    usedLuckBob = true;
                    stack.setCount(stack.getCount() - 1);
                    break;
                }
            }
        }
    }

    private void addTags(EntityPlayer player, NBTTagCompound pDataIn) {

        // comes with stat increases then has perks at X amount consumed, usually 5 or 10
        // total fallout bobbleheads is 7
        if (usedStrengthBob) { // exponential, might get insane
            int damageFormula = Math.round(pDataIn.getInteger(ATTACK_DAMAGE_TAG) * 1.1F); // multiplies damage by 1.5, 50% damage increase for melee

            LOGGER.info("adding attack damage tag");

            pDataIn.setInteger(ATTACK_DAMAGE_TAG, 1 + damageFormula);

            if (pDataIn.getInteger(ATTACK_DAMAGE_TAG) >= 5 && !pDataIn.getBoolean(ATTACK_DAMAGE_BONUS_TAG)) { // perk. if above 5 damage and had no bonus
                pDataIn.setInteger(ATTACK_DAMAGE_TAG, 1 + 5 + damageFormula);
                pDataIn.setBoolean(ATTACK_DAMAGE_BONUS_TAG, true);
            }

            usedStrengthBob = false;
        }

        if (usedPerceptionBob) {
            pDataIn.setInteger(GUN_ACCURACY_TAG, pDataIn.getInteger(GUN_ACCURACY_TAG) + 1); // reduces spread

            if (pDataIn.getInteger(GUN_ACCURACY_TAG) >= 5) { // perk
                pDataIn.setBoolean(NIGHT_VISION_TAG, true);
            }

            usedPerceptionBob = false;
        }

        if (usedEnduranceBob) {
            IAttributeInstance maxHealth = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);

            maxHealth.removeModifier(MAX_HEALTH_UUID);
            if (pDataIn.getDouble(MAX_HEALTH_TAG) >= 20 && !pDataIn.getBoolean(MAX_HEALTH_BONUS_TAG)) { // perk, 5 bobs
                pDataIn.setDouble(MAX_HEALTH_TAG, pDataIn.getDouble(MAX_HEALTH_TAG) + 10);
                maxHealth.applyModifier(new AttributeModifier(MAX_HEALTH_UUID, nameIn, maxHealth.getAttributeValue() + pDataIn.getDouble(MAX_HEALTH_TAG), 0)); // 4 + 10 = 7 full hearts
                pDataIn.setBoolean(MAX_HEALTH_BONUS_TAG, true);
            } else {
                pDataIn.setDouble(MAX_HEALTH_TAG, pDataIn.getDouble(MAX_HEALTH_TAG) + 4);
                maxHealth.applyModifier(new AttributeModifier(MAX_HEALTH_UUID, nameIn, maxHealth.getAttributeValue() + pDataIn.getDouble(MAX_HEALTH_TAG), 0)); // +2 full hearts
            }
            usedEnduranceBob = false;
        }

        if (usedCharismaBob) { // plot armor
            IAttributeInstance armor = player.getEntityAttribute(SharedMonsterAttributes.ARMOR);
            IAttributeInstance armorToughness = player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);

            pDataIn.setInteger(ARMOR_TAG, pDataIn.getInteger(ARMOR_TAG) + 2);
            pDataIn.setInteger(ARMOR_TOUGHNESS_TAG, pDataIn.getInteger(ARMOR_TOUGHNESS_TAG) + 1);

            if (pDataIn.getInteger(ARMOR_TAG) >= 6) {

            }
            usedCharismaBob = false;
        }

        if (usedIntelligenceBob) { // increases gun damage
            pDataIn.setInteger(GUN_DAMAGE_TAG, pDataIn.getInteger(GUN_DAMAGE_TAG) + 2);

            usedIntelligenceBob = false;
        }


        if (usedAgilityBob) { // percentage
            pDataIn.setFloat(MOVEMENT_SPEED_TAG, pDataIn.getFloat(MOVEMENT_SPEED_TAG) + 0.05F); // +5% movement speed

            // todo keybind for tweaking current move speed after acquiring 5 agility bob

            usedAgilityBob = false;
        }

        if (usedLuckBob) { // increases the chance of reloading rounds for free,
            pDataIn.setInteger(LUCK_TAG, pDataIn.getInteger(LUCK_TAG) + 1);

            usedAgilityBob = false;
        }
    }
}
