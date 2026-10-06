package com.levviata.lspecial.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncSPECIALStatsPacket implements IMessage {
    public int strength;
    public int perception;
    public int endurance;
    public int enduranceBonus;
    public int charisma;
    public int charismaBonus;
    public int intelligence;
    public int agility;
    public int luck;

    public SyncSPECIALStatsPacket() {
    }

    public SyncSPECIALStatsPacket(int strength, int perception, int endurance, int enduranceBonus, int charisma, int charismaBonus, int intelligence, int agility, int luck) {
        this.strength = strength;
        this.perception = perception;
        this.endurance = endurance;
        this.enduranceBonus = enduranceBonus;
        this.charisma = charisma;
        this.charismaBonus = charismaBonus;
        this.intelligence = intelligence;
        this.agility = agility;
        this.luck = luck;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        strength = buf.readInt();
        perception = buf.readInt();
        endurance = buf.readInt();
        enduranceBonus = buf.readInt();
        charisma = buf.readInt();
        charismaBonus = buf.readInt();
        intelligence = buf.readInt();
        agility = buf.readInt();
        luck = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(strength);
        buf.writeInt(perception);
        buf.writeInt(endurance);
        buf.writeInt(enduranceBonus);
        buf.writeInt(charisma);
        buf.writeInt(charismaBonus);
        buf.writeInt(intelligence);
        buf.writeInt(agility);
        buf.writeInt(luck);
    }
}