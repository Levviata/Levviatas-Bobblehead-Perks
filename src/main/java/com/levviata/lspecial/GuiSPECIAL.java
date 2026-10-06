package com.levviata.lspecial;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.scoreboard.Score;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

import static com.levviata.lspecial.LSPECIALMod.*;
import static com.levviata.lspecial.SPECIALScoreboard.inst;

public class GuiSPECIAL extends GuiScreen {

    private static final Map<String, Score> scores = new LinkedHashMap<>();
    public static boolean hasInit = false;

    public static void init() {
        //scores.clear();
        scores.put("Strength", inst.getStrengthScore());
        scores.put("Perception", inst.getPerceptionScore());
        scores.put("Endurance", inst.getEnduranceScore());
        scores.put("Endurance Bonus", inst.getEnduranceBonusScore());
        scores.put("Charisma", inst.getCharismaScore());
        scores.put("Charisma Bonus", inst.getCharismaBonusScore());
        scores.put("Intelligence", inst.getIntelligenceScore());
        scores.put("Agility", inst.getAgilityScore());
        scores.put("Luck", inst.getLuckScore());
        hasInit = true;
    }
    // todo maybe command to reset and rearrange stats, which this GUI would take to render a special interactable screen

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int width = 220;
        int height = 160;
        int x = (this.width - width) / 2;
        int y = (this.height - height) / 2;

        drawRect(x, y, x + width, y + height, 0xFF000000);

        int textY = y + 10;

        for (Map.Entry<String, Score> entry : scores.entrySet()) {
            Score score = entry.getValue();

            if (score != null) {
                if (score == inst.getCharismaBonusScore() || score == inst.getEnduranceBonusScore()) {
                    bonusRender(entry.getKey(), score, this.fontRenderer, x, textY);
                    LOGGER.info("bonusrender");
                } else {
                    this.fontRenderer.drawString(entry.getKey() + ": " + score.getScorePoints(), x + 10, textY, 0xFFFFFFFF);
                }

                textY += 15;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (toggleSpecial.isPressed()) {
            Minecraft mc = Minecraft.getMinecraft();

            if (mc.currentScreen instanceof GuiSPECIAL) {
                mc.displayGuiScreen(null);
            } else {
                scores.clear();
                inst.scoreboard = mc.world.getScoreboard();
                inst.startBoards();
                inst.startScores(mc.player);
                init();
                mc.displayGuiScreen(new GuiSPECIAL());
            }
        }
    }

    private void bonusRender(String name, Score score, FontRenderer renderer, int x, int textY) {
        if (score.getScorePoints() == 1) {
            renderer.drawString(name + ": true", x + 10, textY, 0xFFFFFFFF);
        } else if (score.getScorePoints() == 0) {
            renderer.drawString(name + ": false", x + 10, textY, 0xFFFFFFFF);
        }
    }
}
