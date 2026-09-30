package com.levviata.lbob;

import com.expandedevents.api.event.ItemAttributeModifierEvent;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;
import java.util.stream.Collectors;

import static com.levviata.lbob.LeviathanPlayerAttributes.LOGGER;
import static com.levviata.lbob.LeviathanPlayerAttributes.scoreboard;
import static com.levviata.lbob.PlayerUseBobblehead.*;

public class LBAttributeModifier {

    @SubscribeEvent
    public void onAttributeModifier(ItemAttributeModifierEvent event) {
        if (Minecraft.getMinecraft().player == null) {
            return;
        }
/*
        boolean hasRunStrength = false;

        EntityPlayer player = Minecraft.getMinecraft().player;

        NBTTagCompound data = player.getEntityData();

        scoreboard = player.getEntityWorld().getScoreboard();

        event.removeAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

        LOGGER.info("tag damage {}", player.getEntityData().getDouble(tag));
        event.addModifier(SharedMonsterAttributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, nameIn, damage + strength.getScorePoints(), 0));

        for (String tag : player.getEntityData().getKeySet()) { // browse all keys in the players's nbt
            // tags from PlayerUseBobblehead.addTags()

            // strength
            if (tag.equals(ATTACK_DAMAGE_TAG) && !hasRunStrength) {
                if (!event.getModifiers().get(SharedMonsterAttributes.ATTACK_DAMAGE.getName()).isEmpty()) {
                    Collection<AttributeModifier> damageCollection = event.getModifiers().get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());

                    double damage = 1 + damageCollection.iterator().next().getAmount();

                    if (scoreboard.getObjective(STRENGTH_BOARD) == null) {
                        scoreboard.addScoreObjective(STRENGTH_BOARD, IScoreCriteria.DUMMY);
                    }

                    ScoreObjective obj = scoreboard.getObjective(STRENGTH_BOARD);
                    Score strength = scoreboard.getOrCreateScore(player.getName(), obj);

                    LOGGER.info("going through with dmg modification {}", damage);

                    event.removeAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

                    LOGGER.info("tag damage {}", player.getEntityData().getDouble(tag));
                    event.addModifier(SharedMonsterAttributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, nameIn, damage + strength.getScorePoints(), 0));

                    hasRunStrength = true;
                }
            }

           // perception
           // todo gun accuracy stat logic

           // endurance, done at PlayerUseBobblehead
           // charisma
        }*/
    }
}
