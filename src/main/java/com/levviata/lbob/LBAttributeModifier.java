package com.levviata.lbob;

import com.expandedevents.api.event.ItemAttributeModifierEvent;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.*;
import java.util.stream.Collectors;

import static com.levviata.lbob.LeviathanPlayerAttributes.LOGGER;
import static com.levviata.lbob.PlayerUseBobblehead.usedStrengthBob;

public class LBAttributeModifier {
    //vanilla uuids
    private static final UUID ATTACK_DAMAGE_MODIFIER = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private static final UUID ATTACK_SPEED_MODIFIER = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
    // randomized uuids, same UUIDs as my mod Attribute Modifier
    public static final UUID MAX_HEALTH_UUID =
            UUID.fromString("5b94c2f0-6a6e-4b7d-9f6f-8d2a4d7c1e01");
    private static final UUID FOLLOW_RANGE_UUID =
            UUID.fromString("7d31a6c2-15bb-47d5-aef5-3c94a87f3202");
    private static final UUID KNOCKBACK_RESISTANCE_UUID =
            UUID.fromString("93ef45e8-12c0-4f17-8c3e-61d2a94b5f03");
    public static final UUID MOVEMENT_SPEED_UUID =
            UUID.fromString("b7d3a6f9-58d4-4b2f-a0f7-9c13e4d8a904");
    private static final UUID FLYING_SPEED_UUID =
            UUID.fromString("d2f9c781-7b48-4c81-93ae-0d7f2b6e1505");
    public static final UUID ARMOR_UUID =
            UUID.fromString("e5a14d92-4f33-4d0f-b1ce-7a8d0f2c3606");
    public static final UUID ARMOR_TOUGHNESS_UUID =
            UUID.fromString("f84c7b13-2d75-4d8b-9ef4-4b0a91d54707");
    public static final UUID LUCK_UUID =
            UUID.fromString("18b4f6d0-8ec1-4cba-a57e-52d6f83a7808");
    public static final String nameIn = "Lev Attribute Modifier";

    // strength
    public static final String ATTACK_DAMAGE_TAG = "Lattack_damage"; // int
    public static final String ATTACK_DAMAGE_BONUS_TAG = "Lattack_damage_bonus"; // boolean

    // perception
    public static final String GUN_ACCURACY_TAG = "Lgun_accuracy"; // int
    public static final String NIGHT_VISION_TAG = "Lnight_vision"; // boolean

    // endurance
    public static final String MAX_HEALTH_TAG = "Lmax_health"; // double
    public static final String MAX_HEALTH_BONUS_TAG = "Lmax_health_bonus"; // boolean

    // charisma
    public static final String ARMOR_TAG = "Larmor";
    public static final String ARMOR_TOUGHNESS_TAG = "Larmor_toughness";

    public static final String FOLLOW_RANGE_TAG = "Lfollow_range";
    public static final String KNOCKBACK_RESISTANCE_TAG = "Lknockback_resistance";
    public static final String MOVEMENT_SPEED_TAG = "Lmovement_speed";
    public static final String FLYING_SPEED_TAG = "Lflying_speed";


    public static final String GUN_DAMAGE_TAG = "Lgun_damage";
    public static final String ATTACK_SPEED_TAG = "Lattack_speed";

    public static final String LUCK_TAG = "Lluck";
    @SubscribeEvent
    public void onAttributeModifier(ItemAttributeModifierEvent event) {
        if (Minecraft.getMinecraft().player == null) {
            return;
        }

        boolean hasRunStrength = false;

        EntityPlayer player = Minecraft.getMinecraft().player;

        NBTTagCompound data = player.getEntityData();

        for (String tag : player.getEntityData().getKeySet()) { // browse all keys in the players's nbt
            // tags from PlayerUseBobblehead.addTags()

            // strength
            if (tag.equals(ATTACK_DAMAGE_TAG) && !hasRunStrength) {
                if (!event.getModifiers().get(SharedMonsterAttributes.ATTACK_DAMAGE.getName()).isEmpty()) {
                    Collection<AttributeModifier> damageCollection = event.getModifiers().get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());

                    double damage = 1 + damageCollection.iterator().next().getAmount();

                    LOGGER.info("going through with dmg modification {}", damage);

                    event.removeAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

                    LOGGER.info("tag damage {}", player.getEntityData().getDouble(tag));
                    event.addModifier(SharedMonsterAttributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, nameIn, damage + data.getDouble(tag), 0));

                    hasRunStrength = true;
                }
            }

           // perception
           // todo gun accuracy stat logic

           // endurance, done at PlayerUseBobblehead
           // charisma
        }
    }
}
