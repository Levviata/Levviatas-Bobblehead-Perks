package com.levviata.lbob;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

import static com.levviata.lbob.LeviathanPlayerAttributes.*;

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

    int funLimit = 10;

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

        scoreboard = player.getEntityWorld().getScoreboard();

        // WARNING if the scores or scoreboards don't start, the mod is useless
        startBoards();
        startScores(player);

        //LOGGER.error("Couldn't start the scoreboards, the mod is useless!");

        // when a hbm:bobblehead is crouch right-clicked, I remove the item and set a flag as true
        if (stack.getItem().equals(bob.getItem()) && player.isSneaking()) {
            switch (stack.getItemDamage()) {
                case 1: {
                    if (strengthScore.getScorePoints() >= funLimit) {
                        Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
                    } else {
                        usedStrengthBob = true;
                        stack.setCount(stack.getCount() - 1);
                    }
                    break;
                }
                case 2: {
                    if (perceptionScore.getScorePoints() >= funLimit) {
                        Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
                    } else {
                        usedPerceptionBob = true;
                        stack.setCount(stack.getCount() - 1);
                    }
                    break;
                }
                case 3: {
                    if (enduranceScore.getScorePoints() >= funLimit) {
                        Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
                    } else {
                        usedEnduranceBob = true;
                        stack.setCount(stack.getCount() - 1);
                    }
                    break;
                }
                case 4: {
                    if (charismaScore.getScorePoints() >= funLimit) {
                        Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
                    } else {
                        usedCharismaBob = true;
                        stack.setCount(stack.getCount() - 1);
                    }
                    break;
                }
                case 5: {
                    if (intelligenceScore.getScorePoints() >= funLimit) {
                        Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
                    } else {
                        usedIntelligenceBob = true;
                        stack.setCount(stack.getCount() - 1);
                    }
                    break;
                }
                case 6: {
                    if (agilityScore.getScorePoints() >= funLimit) {
                        Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
                    } else {
                        usedAgilityBob = true;
                        stack.setCount(stack.getCount() - 1);
                    }
                    break;
                }
                case 8: {
                    if (luckScore.getScorePoints() >= funLimit) {
                        Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
                    } else {
                        usedLuckBob = true;
                        stack.setCount(stack.getCount() - 1);
                    }
                    break;
                }
            }
        }


        // comes with stat increases then has perks at X amount consumed, usually 5 or 10
        // total fallout bobbleheads is 7
        if (usedStrengthBob) {
            if (strengthScore.getScorePoints() < funLimit) {
                strengthScore.setScorePoints(strengthScore.getScorePoints() + 1);
            } else {
                strengthScore.setScorePoints(funLimit);
            }

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

            if (enduranceScore.getScorePoints() >= 20 && enduranceBonusScore.getScorePoints() != 1) { // perk, 5 bobs
                if (enduranceScore.getScorePoints() < funLimit) {
                    enduranceScore.setScorePoints(enduranceScore.getScorePoints() + 10);
                    enduranceBonusScore.setScorePoints(1);
                } else {
                    enduranceScore.setScorePoints(funLimit);
                }
            } else {
                if (enduranceScore.getScorePoints() < funLimit) {
                    enduranceScore.setScorePoints(enduranceScore.getScorePoints() + 4);
                } else {
                    enduranceScore.setScorePoints(funLimit);
                }
            }

            usedEnduranceBob = false;
        }

        if (usedCharismaBob) { // plot armor
            // todo perk
            if (charismaScore.getScorePoints() < funLimit) {
                charismaScore.setScorePoints(charismaScore.getScorePoints() + 1);
            } else {
                charismaScore.setScorePoints(funLimit);
            }

            usedCharismaBob = false;
        }

        if (usedIntelligenceBob) { // increases gun damage
            // limits
            if (intelligenceScore.getScorePoints() < funLimit) {
                intelligenceScore.setScorePoints(intelligenceScore.getScorePoints() + 1);
            } else {
                intelligenceScore.setScorePoints(funLimit);
            }

            usedIntelligenceBob = false;
        }


        if (usedAgilityBob) { // percentage
            // todo keybind for tweaking current move speed after acquiring 5 agility bob

            usedAgilityBob = false;
        }

        if (usedLuckBob) { // increases the chance of reloading rounds for free,

            usedAgilityBob = false;
        }
    }
}
