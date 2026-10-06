package com.levviata.lspecial.network;

import com.levviata.lspecial.LSPECIALMod;
import com.levviata.lspecial.SPECIALScoreboard;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RequestSPECIALStatsPacket implements IMessage {
    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<RequestSPECIALStatsPacket, IMessage> {
        @Override
        public IMessage onMessage(RequestSPECIALStatsPacket message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                SPECIALScoreboard.inst.scoreboard = player.world.getScoreboard();
                SPECIALScoreboard.inst.startBoards();
                SPECIALScoreboard.inst.startScores(player);
                LSPECIALMod.syncStats(player);
            });
            return null;
        }
    }
}