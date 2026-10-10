package com.levviata.lspecial;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

import static com.levviata.lspecial.SPECIALScoreboard.inst;

public class BobbleheadUse {
    private static final String LIMIT_MESSAGE = "I have reached my limit, I can't consume more.";
    private int augLimit = 0;

    @SubscribeEvent
    public void use(PlayerInteractEvent.RightClickItem event) {
        EntityPlayer player = event.getEntityPlayer();

        if (player.world.isRemote) { // server sided
            return;
        }

        ItemStack bob = new ItemStack(Objects.requireNonNull(Item.getByNameOrId("hbm:bobblehead")));

        ItemStack stack = event.getItemStack();

        inst.scoreboard = player.getEntityWorld().getScoreboard();

        // WARNING if the scores or scoreboards don't start, the mod is useless
        inst.startBoards();
        inst.startScores(player);

        //LOGGER.error("Couldn't start the scoreboards, the mod is useless!"); how do i check this

        // when a hbm:bobblehead is crouch right-clicked, I remove the item and set a flag as true
        if (stack.getItem().equals(bob.getItem()) && player.isSneaking()) {
            // these if statement are retarded but dont bark at it for me ok?
            if (stack.getItemDamage() == 1) {
                processBob(inst.getStrengthScore(), stack, player);
            }
            if (stack.getItemDamage() == 2) {
                processBob(inst.getPerceptionScore(), stack, player);
            }
            if (stack.getItemDamage() == 3) {
                processBobAndLesserPerk(inst.getEnduranceScore(), inst.getEnduranceBonusScore(), stack, player);
            }
            if (stack.getItemDamage() == 4) {
                processBobAndLesserPerk(inst.getCharismaScore(), inst.getCharismaBonusScore(), stack, player);
            }
            if (stack.getItemDamage() == 5) {
                processBob(inst.getIntelligenceScore(), stack, player);
            }
            if (stack.getItemDamage() == 6) {
                processBob(inst.getAgilityScore(), stack, player);
            }
            if (stack.getItemDamage() == 7) {
                processBob(inst.getLuckScore(), stack, player);
            }
            if (stack.getItemDamage() > 7) {
                stack.setCount(stack.getCount() - 1);
                inst.getLimitScore().setScorePoints(inst.getLimitScore().getScorePoints() + 1);

                augLimit++;
                if (augLimit >= 20) {
                    player.sendStatusMessage(new TextComponentString("Limit++"), true);
                } else if (augLimit >= 10) {
                    player.sendStatusMessage(new TextComponentString("SPECIAL limit++"), true);
                } else if (augLimit >= 5) {
                    player.sendStatusMessage(new TextComponentString("Augmented SPECIAL limit by one"), true);
                } else {
                    player.sendStatusMessage(new TextComponentString("Augmented my SPECIAL limit by one, cool"), true);
                }
            }

            if (player instanceof EntityPlayerMP) {
                LSPECIALMod.syncStats((EntityPlayerMP) player); // server -> client sync
            }
        }
    }

    private void processBob(Score score, ItemStack stack, EntityPlayer player) {
        if (score.getScorePoints() >= inst.getLimitScore().getScorePoints()) {
            player.sendStatusMessage(new TextComponentString(LIMIT_MESSAGE), true);
        } else {
            stack.setCount(stack.getCount() - 1);
            score.setScorePoints(score.getScorePoints() + 1);
        }
    }

    private void processBobAndLesserPerk(Score score, Score bonus, ItemStack stack, EntityPlayer player) {
        if (score.getScorePoints() >= inst.getLimitScore().getScorePoints()) {
            player.sendStatusMessage(new TextComponentString(LIMIT_MESSAGE), true);
            return;
        }

        stack.setCount(stack.getCount() - 1);
        score.setScorePoints(score.getScorePoints() + 1);
        if (score.getScorePoints() >= 4 && bonus.getScorePoints() != 1) {
            bonus.setScorePoints(1);
        }
    }
}
