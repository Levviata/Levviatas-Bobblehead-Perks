package com.levviata.lspecial;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FlySpeedFix {
    @SubscribeEvent
    public void onClientTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            // FLY SPEED FIX //
            EntityPlayer player = event.player;
            ItemStack heldMainHand = player.getHeldEquipment().iterator().next();

            float defaultFlyingSpeed = 0.05F;
            if (heldMainHand != ItemStack.EMPTY) {
                Multimap<String, AttributeModifier> attributes = heldMainHand.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
                if (attributes.containsKey("generic.flyingSpeed")) {
                    player.capabilities.setFlySpeed((float) attributes.get("generic.flyingSpeed").iterator().next().getAmount());
                }
                else player.capabilities.setFlySpeed(defaultFlyingSpeed);
            }
        }
    }
}
