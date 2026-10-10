package com.levviata.lspecial;

import com.levviata.lspecial.network.RequestSPECIALStatsPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import static com.levviata.lspecial.LSPECIALMod.toggleSpecial;
import static com.levviata.lspecial.SPECIALScoreboard.inst;

public class GuiSPECIAL extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int panelWidth = 240;
        int rows = 0;

        boolean[] enabledStats = {
                LSPECIALConfig.strength,
                LSPECIALConfig.perception,
                LSPECIALConfig.endurance,
                LSPECIALConfig.charisma,
                LSPECIALConfig.intelligence,
                LSPECIALConfig.agility,
                LSPECIALConfig.luck
        };

        for (boolean enabled : enabledStats) {
            if (enabled || !LSPECIALConfig.hideDisabledStats) {
                rows++;
            }
        }

        if (LSPECIALConfig.enduranceBonus) {
            rows++;
        }

        if (LSPECIALConfig.charismaBonus) {
            rows++;
        }

        if (LSPECIALConfig.statLimit) {
            rows++;
        }

        int panelHeight = Math.max(40, 20 + rows * 15);
        int x = (this.width - panelWidth) / 2;
        int y = (this.height - panelHeight) / 2;

        drawRect(x, y, x + panelWidth, y + panelHeight, 0xFF000000);

        int textY = y + 10;

        drawStat("Strength", inst.getClientStrength(), LSPECIALConfig.strength, x, textY);
        if (LSPECIALConfig.strength || !LSPECIALConfig.hideDisabledStats) {
            textY += 15;
        }

        drawStat("Perception", inst.getClientPerception(), LSPECIALConfig.perception, x, textY);
        if (LSPECIALConfig.perception || !LSPECIALConfig.hideDisabledStats) {
            textY += 15;
        }

        drawStat("Endurance", inst.getClientEndurance(), LSPECIALConfig.endurance, x, textY);
        if (LSPECIALConfig.endurance || !LSPECIALConfig.hideDisabledStats) {
            textY += 15;
        }

        if (LSPECIALConfig.enduranceBonus) {
            drawString(
                    fontRenderer,
                    "Endurance Bonus: " + (inst.getClientEnduranceBonus() == 1),
                    x + 10,
                    textY,
                    0xFFFFFFFF
            );
            textY += 15;
        }

        drawStat("Charisma", inst.getClientCharisma(), LSPECIALConfig.charisma, x, textY);
        if (LSPECIALConfig.charisma || !LSPECIALConfig.hideDisabledStats) {
            textY += 15;
        }

        if (LSPECIALConfig.charismaBonus) {
            drawString(
                    fontRenderer,
                    "Charisma Bonus: " + (inst.getClientCharismaBonus() == 1),
                    x + 10,
                    textY,
                    0xFFFFFFFF
            );
            textY += 15;
        }

        drawStat("Intelligence", inst.getClientIntelligence(), LSPECIALConfig.intelligence, x, textY);
        if (LSPECIALConfig.intelligence || !LSPECIALConfig.hideDisabledStats) {
            textY += 15;
        }

        drawStat("Agility", inst.getClientAgility(), LSPECIALConfig.agility, x, textY);
        if (LSPECIALConfig.agility || !LSPECIALConfig.hideDisabledStats) {
            textY += 15;
        }

        drawStat("Luck", inst.getClientLuck(), LSPECIALConfig.luck, x, textY);
        if (LSPECIALConfig.luck || !LSPECIALConfig.hideDisabledStats) {
            textY += 15;
        }

        if (LSPECIALConfig.statLimit) {
            drawString(
                    fontRenderer,
                    "Stat Limit: " + inst.getClientLimit(),
                    x + 10,
                    textY,
                    0xFFFFFFFF
            );
        }
    }

    private void drawStat(String name, int value, boolean enabled, int x, int y) {
        if (!enabled && LSPECIALConfig.hideDisabledStats) {
            return;
        }

        String text = name + ": " + value;

        if (!enabled) {
            text = "§m" + text + "§r";
        }

        drawString(fontRenderer, text, x + 10, y, 0xFFFFFFFF);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!LSPECIALConfig.toggleSpecial || !toggleSpecial.isPressed()) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();

        if (minecraft.currentScreen instanceof GuiSPECIAL) {
            minecraft.displayGuiScreen(null);
        } else {
            LSPECIALMod.NETWORK.sendToServer(new RequestSPECIALStatsPacket());
            minecraft.displayGuiScreen(new GuiSPECIAL());
        }
    }
}
