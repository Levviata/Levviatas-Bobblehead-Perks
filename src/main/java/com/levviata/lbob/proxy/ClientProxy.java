package com.levviata.lbob.proxy;

import com.levviata.lbob.Tags;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID, value = Side.CLIENT)
public class ClientProxy extends CommonProxy { // client only

    public static ScoreObjective objStrength;
    public static ScoreObjective objPerception;
    public static ScoreObjective objEndurance;
    public static ScoreObjective objEnduranceBonus;
    public static ScoreObjective objIntelligence;
    public static ScoreObjective objCharisma;
    public static ScoreObjective objCharismaBonus;
    public static ScoreObjective objAgility;
    public static ScoreObjective objLuck;
    public static Score strengthScore;
    public static Score perceptionScore;
    public static Score enduranceScore;
    public static Score enduranceBonusScore;
    public static Score intelligenceScore;
    public static Score charismaScore;
    public static Score charismaBonusScore;
    public static Score agilityScore;
    public static Score luckScore;

    public static void startScores(EntityPlayer player) {
        objStrength = scoreboard.getObjective(STRENGTH_BOARD);
        objPerception = scoreboard.getObjective(PERCEPTION_BOARD);
        objEndurance = scoreboard.getObjective(ENDURANCE_BOARD);
        objEnduranceBonus = scoreboard.getObjective(ENDURANCE_BONUS_BOARD);
        objCharisma = scoreboard.getObjective(CHARISMA_BOARD);
        objCharismaBonus = scoreboard.getObjective(CHARISMA_BONUS_BOARD);
        objIntelligence = scoreboard.getObjective(INTELLIGENCE_BOARD);
        objAgility = scoreboard.getObjective(AGILITY_BOARD);
        objLuck = scoreboard.getObjective(LUCK_BOARD);

        strengthScore = scoreboard.getOrCreateScore(player.getName(), objStrength);
        perceptionScore = scoreboard.getOrCreateScore(player.getName(), objPerception);
        enduranceScore = scoreboard.getOrCreateScore(player.getName(), objEndurance);
        enduranceBonusScore = scoreboard.getOrCreateScore(player.getName(), objEnduranceBonus);
        charismaScore = scoreboard.getOrCreateScore(player.getName(), objCharisma);
        charismaBonusScore = scoreboard.getOrCreateScore(player.getName(), objCharismaBonus);
        intelligenceScore = scoreboard.getOrCreateScore(player.getName(), objIntelligence);
        agilityScore = scoreboard.getOrCreateScore(player.getName(), objAgility);
        luckScore = scoreboard.getOrCreateScore(player.getName(), objLuck);
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
