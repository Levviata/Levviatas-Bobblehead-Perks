package com.levviata.lspecial;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Objects;

import static com.levviata.lspecial.GuiSPECIAL.hasInit;
import static com.levviata.lspecial.LSPECIALMod.*;
import static com.levviata.lspecial.SPECIALScoreboard.inst;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID, value = Side.CLIENT)
public class BobbleheadUse {
    private static final String LIMIT_MESSAGE = "I have reached my limit, I can't consume more.";

    int funLimit = 10;

    @SubscribeEvent
    public void use(PlayerInteractEvent.RightClickItem event) {
        EntityPlayer player = event.getEntityPlayer();

        if (player.world.isRemote) {
            return;
        }

        ItemStack bob = new ItemStack(Objects.requireNonNull(Item.getByNameOrId("hbm:bobblehead")));
        //ItemStack bob = new ItemStack(Objects.requireNonNull(Item.getByNameOrId("minecraft:dye"))); // test

        ItemStack stack = event.getItemStack();

        inst.scoreboard = player.getEntityWorld().getScoreboard();

        // WARNING if the scores or scoreboards don't start, the mod is useless
        inst.startBoards();
        inst.startScores(player);
        GuiSPECIAL.init();

        //LOGGER.error("Couldn't start the scoreboards, the mod is useless!"); how do i check this

        // when a hbm:bobblehead is crouch right-clicked, I remove the item and set a flag as true
        if (stack.getItem().equals(bob.getItem()) && player.isSneaking()) {
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
        }
    }

    private void processBob(Score score, ItemStack stack, EntityPlayer player) {
        if (score.getScorePoints() >= funLimit) {
            player.sendStatusMessage(new TextComponentString(LIMIT_MESSAGE), true);
            //Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
        } else {
            stack.setCount(stack.getCount() - 1);
            score.setScorePoints(score.getScorePoints() + 1);
        }
    }

    private void processBobAndLesserPerk(Score score, Score bonus, ItemStack stack, EntityPlayer player) {
        if (score.getScorePoints() >= funLimit) {
            player.sendStatusMessage(new TextComponentString(LIMIT_MESSAGE), true);
            //Minecraft.getMinecraft().ingameGUI.setOverlayMessage(new TextComponentString(LIMIT_MESSAGE), false);
            return;
        }

        stack.setCount(stack.getCount() - 1);
        score.setScorePoints(score.getScorePoints() + 1);
        if (score.getScorePoints() >= 4 && bonus.getScorePoints() != 1) {
            bonus.setScorePoints(1);
        }
    }
}
