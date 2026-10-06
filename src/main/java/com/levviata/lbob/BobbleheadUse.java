package com.levviata.lbob;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

import static com.levviata.lbob.LSPECIALMod.*;
import static com.levviata.lbob.proxy.ClientProxy.*;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID, value = Side.CLIENT)
public class BobbleheadUse {
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
        ItemStack bob = new ItemStack(Objects.requireNonNull(Item.getByNameOrId("hbm:bobblehead")));
        //ItemStack bob = new ItemStack(Objects.requireNonNull(Item.getByNameOrId("minecraft:dye"))); // test

        ItemStack stack = event.getItemStack();
        EntityPlayer player = event.getEntityPlayer();

        scoreboard = player.getEntityWorld().getScoreboard();

        // WARNING if the scores or scoreboards don't start, the mod is useless
        startBoards();
        startScores(player);

        //LOGGER.error("Couldn't start the scoreboards, the mod is useless!"); how do i check this

        // when a hbm:bobblehead is crouch right-clicked, I remove the item and set a flag as true
        if (stack.getItem().equals(bob.getItem()) && player.isSneaking()) {
            switch (stack.getItemDamage()) {
                case 1: {
                    if (strengthScore.getScorePoints() >= funLimit) {
                        Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
                    } else if (strengthScore.getScorePoints() < funLimit) {
                        stack.setCount(stack.getCount() - 1);
                    }
                    usedStrengthBob = true;
                    break;
                }
                case 2: {
                    if (perceptionScore.getScorePoints() >= funLimit) {
                        Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
                    } else if (perceptionScore.getScorePoints() < funLimit) {
                        stack.setCount(stack.getCount() - 1);
                    }
                    usedPerceptionBob = true;
                    break;
                }
                case 3: {
                    if (enduranceScore.getScorePoints() >= funLimit) {
                        Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
                    } else if (enduranceScore.getScorePoints() < funLimit) {
                        stack.setCount(stack.getCount() - 1);
                    }
                    usedEnduranceBob = true;
                    break;
                }
                case 4: {
                    if (charismaScore.getScorePoints() >= funLimit) {
                        Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
                    } else if (charismaScore.getScorePoints() < funLimit) {
                        stack.setCount(stack.getCount() - 1);
                    }
                    usedCharismaBob = true;
                    break;
                }
                case 5: {
                    if (intelligenceScore.getScorePoints() >= funLimit) {
                        Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
                    } else if (intelligenceScore.getScorePoints() < funLimit) {
                        stack.setCount(stack.getCount() - 1);
                    }
                    usedIntelligenceBob = true;
                    break;
                }
                case 6: {
                    if (agilityScore.getScorePoints() >= funLimit) {
                        Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
                    }  else if (agilityScore.getScorePoints() < funLimit) {
                        stack.setCount(stack.getCount() - 1);
                    }
                    usedAgilityBob = true;
                    break;
                }
                case 7: {
                    if (luckScore.getScorePoints() >= funLimit) {
                        Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
                    } else if (luckScore.getScorePoints() < funLimit) {
                        stack.setCount(stack.getCount() - 1);
                    }
                    usedLuckBob = true;
                    break;
                }
            }
        }

        // comes with stat increases then has perks at X amount consumed, usually 5 or 10
        // total fallout bobbleheads is 7
        if (usedStrengthBob) {
            if (strengthScore.getScorePoints() >= funLimit) {
                strengthScore.setScorePoints(funLimit);
            } else {
                strengthScore.setScorePoints(strengthScore.getScorePoints() + 1);
            }

            usedStrengthBob = false;
        }

        if (usedPerceptionBob) {
            if (perceptionScore.getScorePoints() >= funLimit) {
                perceptionScore.setScorePoints(funLimit);
            } else {
                perceptionScore.setScorePoints(perceptionScore.getScorePoints() + 1);
            }

            usedPerceptionBob = false;
        }

        if (usedEnduranceBob) {

            if (enduranceScore.getScorePoints() >= 5 && enduranceBonusScore.getScorePoints() != 1) {
                if (enduranceScore.getScorePoints() >= funLimit) {
                    enduranceScore.setScorePoints(funLimit);
                } else {
                    enduranceScore.setScorePoints(enduranceScore.getScorePoints() + 1);
                    enduranceBonusScore.setScorePoints(1);
                }
            } else {
                if (enduranceScore.getScorePoints() >= funLimit) {
                    enduranceScore.setScorePoints(funLimit);
                } else {
                    enduranceScore.setScorePoints(enduranceScore.getScorePoints() + 1);
                }
            }

            usedEnduranceBob = false;
        }

        if (usedCharismaBob) { // plot armor
            if (charismaScore.getScorePoints() >= 5 && charismaBonusScore.getScorePoints() != 1) {
                if (charismaScore.getScorePoints() >= funLimit) {
                    charismaScore.setScorePoints(funLimit);
                } else {
                    charismaScore.setScorePoints(charismaScore.getScorePoints() + 1);
                    charismaBonusScore.setScorePoints(1);
                }
            } else {
                if (charismaScore.getScorePoints() >= funLimit) {
                    charismaScore.setScorePoints(funLimit);
                } else {
                    charismaScore.setScorePoints(charismaScore.getScorePoints() + 1);
                }
            }

            usedCharismaBob = false;
        }

        if (usedIntelligenceBob) { // increases gun damage
            if (intelligenceScore.getScorePoints() >= funLimit) {
                intelligenceScore.setScorePoints(funLimit);
            } else {
                intelligenceScore.setScorePoints(intelligenceScore.getScorePoints() + 1);
            }

            usedIntelligenceBob = false;
        }


        if (usedAgilityBob) { // percentage
            if (agilityScore.getScorePoints() >= funLimit) {
                agilityScore.setScorePoints(funLimit);
            } else {
                agilityScore.setScorePoints(agilityScore.getScorePoints() + 1);
            }

            usedAgilityBob = false;
        }

        if (usedLuckBob) { // increases the chance of reloading rounds for free,
            if (luckScore.getScorePoints() >= funLimit) {
                luckScore.setScorePoints(funLimit);
            } else {
                luckScore.setScorePoints(luckScore.getScorePoints() + 1);
            }

            usedLuckBob = false;
        }
    }
}
