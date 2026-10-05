package com.levviata.lbob.proxy;

import com.levviata.lbob.BobbleheadUse;
import com.levviata.lbob.StatsLogic;
import com.levviata.lbob.Tags;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;


@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class CommonProxy { // server and client
    public static Scoreboard scoreboard;

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

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new BobbleheadUse());
        MinecraftForge.EVENT_BUS.register(new StatsLogic());
    }

    public void postInit(FMLPostInitializationEvent event) {
        StatsLogic.init();
    }

    public void serverStarting(FMLServerStartingEvent event) {

    }
}
