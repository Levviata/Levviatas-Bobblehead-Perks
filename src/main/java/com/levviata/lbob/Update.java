package com.levviata.lbob;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static com.levviata.lbob.LBAttributeModifier.*;
import static com.levviata.lbob.LBAttributeModifier.MAX_HEALTH_BONUS_TAG;
import static com.levviata.lbob.LBAttributeModifier.MAX_HEALTH_TAG;
import static com.levviata.lbob.LBAttributeModifier.MAX_HEALTH_UUID;
import static com.levviata.lbob.LBAttributeModifier.nameIn;
import static com.levviata.lbob.LeviathanPlayerAttributes.scoreboard;
import static com.levviata.lbob.PlayerUseBobblehead.*;

public class Update {
    private int previousEndurance = -1;

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        EntityPlayer player = event.player;

        if (player.getEntityWorld().isRemote) {
            return;
        }

        scoreboard = player.getEntityWorld().getScoreboard();

        if (scoreboard.getObjective(ENDURANCE_BOARD) == null) {
            scoreboard.addScoreObjective(ENDURANCE_BOARD, IScoreCriteria.DUMMY);
        }

        ScoreObjective enduranceObj = scoreboard.getObjective(ENDURANCE_BOARD);
        Score endurance = scoreboard.getOrCreateScore(player.getName(), enduranceObj);

        if (endurance.getScorePoints() != previousEndurance || player.getMaxHealth() != previousEndurance) {
            IAttributeInstance maxHealth = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);

            previousEndurance = endurance.getScorePoints();
            maxHealth.removeModifier(MAX_HEALTH_UUID);
            maxHealth.applyModifier(new AttributeModifier(MAX_HEALTH_UUID, nameIn, endurance.getScorePoints(), 0));
        }
    }
}
