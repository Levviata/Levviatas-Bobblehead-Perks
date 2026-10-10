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
        int width = 220;
        int height = 160;
        int x = (this.width - width) / 2;
        int y = (this.height - height) / 2;

        drawRect(x, y, x + width, y + height, 0xFF000000);

        int textY = y + 10;
        drawString(this.fontRenderer, "Strength: " + inst.getClientStrength(), x + 10, textY, 0xFFFFFFFF);
        textY += 15;
        drawString(this.fontRenderer, "Perception: " + inst.getClientPerception(), x + 10, textY, 0xFFFFFFFF);
        textY += 15;
        drawString(this.fontRenderer, "Endurance: " + inst.getClientEndurance(), x + 10, textY, 0xFFFFFFFF);
        textY += 15;
        drawString(this.fontRenderer, "Endurance Bonus: " + (inst.getClientEnduranceBonus() == 1 ? "true" : "false"), x + 10, textY, 0xFFFFFFFF);
        textY += 15;
        drawString(this.fontRenderer, "Charisma: " + inst.getClientCharisma(), x + 10, textY, 0xFFFFFFFF);
        textY += 15;
        drawString(this.fontRenderer, "Charisma Bonus: " + (inst.getClientCharismaBonus() == 1 ? "true" : "false"), x + 10, textY, 0xFFFFFFFF);
        textY += 15;
        drawString(this.fontRenderer, "Intelligence: " + inst.getClientIntelligence(), x + 10, textY, 0xFFFFFFFF);
        textY += 15;
        drawString(this.fontRenderer, "Agility: " + inst.getClientAgility(), x + 10, textY, 0xFFFFFFFF);
        textY += 15;
        drawString(this.fontRenderer, "Luck: " + inst.getClientLuck(), x + 10, textY, 0xFFFFFFFF);
        textY += 15;
        drawString(this.fontRenderer, "Stat Limit: " + inst.getClientLimit(), x + 10, textY, 0xFFFFFFFF);
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
                LSPECIALMod.NETWORK.sendToServer(new RequestSPECIALStatsPacket());
                mc.displayGuiScreen(new GuiSPECIAL());
            }
        }
    }
}