package com.levviata.lspecial.network;

import com.levviata.lspecial.SPECIALScoreboard;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncSPECIALStatsSetter implements IMessageHandler<SyncSPECIALStatsPacket, IMessage> {
    @Override
    public IMessage onMessage(SyncSPECIALStatsPacket message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> SPECIALScoreboard.inst.setClientScores(message.strength, message.perception, message.endurance, message.enduranceBonus, message.charisma, message.charismaBonus, message.intelligence, message.agility, message.luck, message.limit));
        return null;
    }
}