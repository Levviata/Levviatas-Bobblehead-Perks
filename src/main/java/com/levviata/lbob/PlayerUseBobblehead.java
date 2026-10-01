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
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
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

    public static final String STRENGTH_BOARD = "Strength";
    public static final String ENDURANCE_BOARD = "Endurance";
    public static final String ENDURANCE_BONUS_BOARD = "EnduranceBonus";
    public static final String CHARISMA_BOARD = "Charisma";
    public static final String PERCEPTION_BOARD = "Perception";
    public static final String INTELLIGENCE_BOARD = "Intelligence";

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

        scoreboard = player.getEntityWorld().getScoreboard();

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
        if (usedStrengthBob) {
            if (scoreboard.getObjective(STRENGTH_BOARD) == null) {
                scoreboard.addScoreObjective(STRENGTH_BOARD, IScoreCriteria.DUMMY);
            }

            ScoreObjective objective = scoreboard.getObjective(STRENGTH_BOARD);
            Score score = scoreboard.getOrCreateScore(player.getName(), objective);
            score.setScorePoints(score.getScorePoints() + 1);

            usedStrengthBob = false;
        }

        if (usedPerceptionBob) {
           /* pDataIn.setInteger(GUN_ACCURACY_TAG, pDataIn.getInteger(GUN_ACCURACY_TAG) + 1); // reduces spread

            if (pDataIn.getInteger(GUN_ACCURACY_TAG) >= 5) { // perk
                pDataIn.setBoolean(NIGHT_VISION_TAG, true);
            }*/

            usedPerceptionBob = false;
        }

        if (usedEnduranceBob) {
            if (scoreboard.getObjective(ENDURANCE_BOARD) == null) {
                scoreboard.addScoreObjective(ENDURANCE_BOARD, IScoreCriteria.DUMMY);
            }
            if (scoreboard.getObjective(ENDURANCE_BONUS_BOARD) == null) {
                scoreboard.addScoreObjective(ENDURANCE_BONUS_BOARD, IScoreCriteria.DUMMY);
            }

            ScoreObjective endBonusObj = scoreboard.getObjective(ENDURANCE_BONUS_BOARD);
            Score enduranceBonus = scoreboard.getOrCreateScore(player.getName(), endBonusObj);

            ScoreObjective objective = scoreboard.getObjective(ENDURANCE_BOARD);
            Score endurance = scoreboard.getOrCreateScore(player.getName(), objective);

            if (endurance.getScorePoints() >= 20 && enduranceBonus.getScorePoints() != 1) { // perk, 5 bobs
                endurance.setScorePoints(endurance.getScorePoints() + 10);
                enduranceBonus.setScorePoints(1);
            } else {
                endurance.setScorePoints(endurance.getScorePoints() + 4);
            }

            usedEnduranceBob = false;
        }

        if (usedCharismaBob) { // plot armor
            // todo perk

            if (scoreboard.getObjective(CHARISMA_BOARD) == null) {
                scoreboard.addScoreObjective(CHARISMA_BOARD, IScoreCriteria.DUMMY);
            }

            ScoreObjective objective = scoreboard.getObjective(CHARISMA_BOARD);
            Score score = scoreboard.getOrCreateScore(player.getName(), objective);
            score.setScorePoints(score.getScorePoints() + 1);
            
            usedCharismaBob = false;
        }

        if (usedIntelligenceBob) { // increases gun damage
            if (scoreboard.getObjective(INTELLIGENCE_BOARD) == null) {
                scoreboard.addScoreObjective(INTELLIGENCE_BOARD, IScoreCriteria.DUMMY);
            }

            ScoreObjective objective = scoreboard.getObjective(INTELLIGENCE_BOARD);
            Score score = scoreboard.getOrCreateScore(player.getName(), objective);
            score.setScorePoints(score.getScorePoints() + 1);

            usedIntelligenceBob = false;
        }


        if (usedAgilityBob) { // percentage
            //pDataIn.setFloat(MOVEMENT_SPEED_TAG, pDataIn.getFloat(MOVEMENT_SPEED_TAG) + 0.05F); // +5% movement speed

            // todo keybind for tweaking current move speed after acquiring 5 agility bob

            usedAgilityBob = false;
        }

        if (usedLuckBob) { // increases the chance of reloading rounds for free,
            //pDataIn.setInteger(LUCK_TAG, pDataIn.getInteger(LUCK_TAG) + 1);

            usedAgilityBob = false;
        }
    }
}
