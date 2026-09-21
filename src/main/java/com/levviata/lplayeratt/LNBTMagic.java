package com.levviata.lplayeratt;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.UUID;

import static com.levviata.lplayeratt.LeviathanPlayerAttributes.LOGGER;
import static com.levviata.lplayeratt.PlayerUseBobblehead.*;

public class LNBTMagic {
    //vanilla uuids
    private static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
    // randomized uuids, same UUIDs as my mod Attribute Modifier
    private static final UUID MAX_HEALTH_UUID =
            UUID.fromString("5b94c2f0-6a6e-4b7d-9f6f-8d2a4d7c1e01");
    private static final UUID FOLLOW_RANGE_UUID =
            UUID.fromString("7d31a6c2-15bb-47d5-aef5-3c94a87f3202");
    private static final UUID KNOCKBACK_RESISTANCE_UUID =
            UUID.fromString("93ef45e8-12c0-4f17-8c3e-61d2a94b5f03");
    private static final UUID MOVEMENT_SPEED_UUID =
            UUID.fromString("b7d3a6f9-58d4-4b2f-a0f7-9c13e4d8a904");
    private static final UUID FLYING_SPEED_UUID =
            UUID.fromString("d2f9c781-7b48-4c81-93ae-0d7f2b6e1505");
    private static final UUID ARMOR_UUID =
            UUID.fromString("e5a14d92-4f33-4d0f-b1ce-7a8d0f2c3606");
    private static final UUID ARMOR_TOUGHNESS_UUID =
            UUID.fromString("f84c7b13-2d75-4d8b-9ef4-4b0a91d54707");
    private static final UUID LUCK_UUID =
            UUID.fromString("18b4f6d0-8ec1-4cba-a57e-52d6f83a7808");
    private static final String nameIn = "Lev Attribute Modifier";

    public static final String MAX_HEALTH_TAG = "Lmax_health";
    public static final String FOLLOW_RANGE_TAG = "Lfollow_range";
    public static final String KNOCKBACK_RESISTANCE_TAG = "Lknockback_resistance";
    public static final String MOVEMENT_SPEED_TAG = "Lmovement_speed";
    public static final String FLYING_SPEED_TAG = "Lflying_speed";
    public static final String ATTACK_DAMAGE_TAG = "Lattack_damage";
    public static final String ATTACK_SPEED_TAG = "Lattack_speed";
    public static final String ARMOR_TAG = "Larmor";
    public static final String ARMOR_TOUGHNESS_TAG = "Larmor_toughness";
    public static final String LUCK_TAG = "Lluck";

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.player == null || mc.world == null) {
            return;
        }

        EntityPlayer player = mc.player;

        if (!player.getTags().contains(MAX_HEALTH_TAG) && usedHealthBob) {
            LOGGER.info("player doesn't have {}, im adding it", MAX_HEALTH_TAG);
            NBTTagCompound maxHealth = event.getEntityPlayer().getEntityData();
            // set an integer value adding 1
            maxHealth.setInteger(MAX_HEALTH_TAG, maxHealth.getInteger(MAX_HEALTH_TAG) + 1);
        }

        for (String tag : player.getTags()) {
            if (tag.equals(MAX_HEALTH_TAG)) {
                player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(MAX_HEALTH_UUID, nameIn, 1, 0));
            }
        }
    }
}
