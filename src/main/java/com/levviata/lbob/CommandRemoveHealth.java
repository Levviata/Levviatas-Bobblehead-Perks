package com.levviata.lbob;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

import static com.levviata.lbob.LBAttributeModifier.*;

public class CommandRemoveHealth extends CommandBase {

    @Override
    public String getName() {
        return "removehealth";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/removehealth <amount>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
            throws CommandException {

        if (args.length != 1) {
            throw new CommandException("Usage: /removehealth <amount>");
        }

        EntityPlayer player = getCommandSenderAsPlayer(sender);

        double amount = parseDouble(args[0]);

        // Get the max health attribute
        IAttributeInstance maxHealth = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);

        // Get your existing modifier
        AttributeModifier modifier = maxHealth.getModifier(MAX_HEALTH_UUID);

        if (modifier == null) {
            throw new CommandException("You don't have a max health modifier.");
        }

        // Remove the old modifier
        maxHealth.removeModifier(modifier);

        // Reduce its value
        double newAmount = modifier.getAmount() - amount;

        // Add it back with the new value
        if (newAmount != 0) {
            maxHealth.applyModifier(new AttributeModifier(MAX_HEALTH_UUID, nameIn, newAmount, 0));
        }
    }
}