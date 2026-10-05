package com.levviata.lbob;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class LeviathanPlayerAttributes {

    // todo: 1. bobbleheads above meta 7 augment limit. 2. recipes for them to reduce grinding

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    public static Scoreboard scoreboard;
    public static ScoreObjective objStrength;
    public static ScoreObjective objPerception;
    public static ScoreObjective objEndurance;
    public static ScoreObjective objEnduranceBonus;
    public static ScoreObjective objIntelligence;
    public static ScoreObjective objCharisma;
    public static ScoreObjective objAgility;
    public static ScoreObjective objLuck;
    public static Score strengthScore;
    public static Score perceptionScore;
    public static Score enduranceScore;
    public static Score enduranceBonusScore;
    public static Score intelligenceScore;
    public static Score charismaScore;
    public static Score agilityScore;
    public static Score luckScore;

    public static final String STRENGTH_BOARD = "Strength";
    public static final String PERCEPTION_BOARD = "Perception";
    public static final String ENDURANCE_BOARD = "Endurance";
    public static final String ENDURANCE_BONUS_BOARD = "Endurance_Bonus";
    public static final String INTELLIGENCE_BOARD = "Intelligence";
    public static final String CHARISMA_BOARD = "Charisma";
    public static final String AGILITY_BOARD = "Agility";
    public static final String LUCK_BOARD = "Luck";

    public static final String LIMIT_MESSAGE = "I have reached my limit, I can't consume more.";

    public static void startBoards() {
        if (scoreboard.getObjective(STRENGTH_BOARD) == null) {
            scoreboard.addScoreObjective(STRENGTH_BOARD, IScoreCriteria.DUMMY);
        }
        if (scoreboard.getObjective(PERCEPTION_BOARD) == null) {
            scoreboard.addScoreObjective(PERCEPTION_BOARD, IScoreCriteria.DUMMY);
        }
        if (scoreboard.getObjective(ENDURANCE_BOARD) == null) {
            scoreboard.addScoreObjective(ENDURANCE_BOARD, IScoreCriteria.DUMMY);
        }
        if (scoreboard.getObjective(ENDURANCE_BONUS_BOARD) == null) {
            scoreboard.addScoreObjective(ENDURANCE_BONUS_BOARD, IScoreCriteria.DUMMY);
        }
        if (scoreboard.getObjective(CHARISMA_BOARD) == null) {
            scoreboard.addScoreObjective(CHARISMA_BOARD, IScoreCriteria.DUMMY);
        }
        if (scoreboard.getObjective(INTELLIGENCE_BOARD) == null) {
            scoreboard.addScoreObjective(INTELLIGENCE_BOARD, IScoreCriteria.DUMMY);
        }
        if (scoreboard.getObjective(AGILITY_BOARD) == null) {
            scoreboard.addScoreObjective(AGILITY_BOARD, IScoreCriteria.DUMMY);
        }
        if (scoreboard.getObjective(LUCK_BOARD) == null) {
            scoreboard.addScoreObjective(LUCK_BOARD, IScoreCriteria.DUMMY);
        }
    }
    
    public static void startScores(EntityPlayer player) {
        objStrength = scoreboard.getObjective(STRENGTH_BOARD);
        objPerception = scoreboard.getObjective(PERCEPTION_BOARD);
        objEndurance = scoreboard.getObjective(ENDURANCE_BOARD);
        objEnduranceBonus = scoreboard.getObjective(ENDURANCE_BONUS_BOARD);
        objCharisma = scoreboard.getObjective(CHARISMA_BOARD);
        objIntelligence = scoreboard.getObjective(INTELLIGENCE_BOARD);
        objAgility = scoreboard.getObjective(AGILITY_BOARD);
        objLuck = scoreboard.getObjective(LUCK_BOARD);

        strengthScore = scoreboard.getOrCreateScore(player.getName(), objStrength);
        perceptionScore = scoreboard.getOrCreateScore(player.getName(), objPerception);
        enduranceScore = scoreboard.getOrCreateScore(player.getName(), objEndurance);
        enduranceBonusScore = scoreboard.getOrCreateScore(player.getName(), objEnduranceBonus);
        charismaScore = scoreboard.getOrCreateScore(player.getName(), objCharisma);
        intelligenceScore = scoreboard.getOrCreateScore(player.getName(), objIntelligence);
        agilityScore = scoreboard.getOrCreateScore(player.getName(), objAgility);
        luckScore = scoreboard.getOrCreateScore(player.getName(), objLuck);
    }

    /**
     * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
     *     Take a look at how many FMLStateEvents you can listen to via the @Mod.EventHandler annotation here
     * </a>
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PlayerUseBobblehead());
        MinecraftForge.EVENT_BUS.register(new PlayerStats());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PlayerStats.init();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandSetSpeed());
    }
}
