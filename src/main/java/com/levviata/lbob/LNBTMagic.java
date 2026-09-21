package com.levviata.lbob;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.UUID;

import static com.levviata.lbob.PlayerUseBobblehead.*;

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

    // strength
    public static final String ATTACK_DAMAGE_TAG = "Lattack_damage"; // int
    public static final String ATTACK_DAMAGE_BONUS_TAG = "Lattack_damage_bonus"; // boolean

    // perception
    public static final String GUN_ACCURACY_TAG = "Lgun_accuracy"; // int
    public static final String NIGHT_VISION_TAG = "Lnight_vision"; // boolean

    // endurance
    public static final String MAX_HEALTH_TAG = "Lmax_health"; // int
    public static final String MAX_HEALTH_BONUS_TAG = "Lmax_health_bonus"; // boolean

    public static final String FOLLOW_RANGE_TAG = "Lfollow_range";
    public static final String KNOCKBACK_RESISTANCE_TAG = "Lknockback_resistance";
    public static final String MOVEMENT_SPEED_TAG = "Lmovement_speed";
    public static final String FLYING_SPEED_TAG = "Lflying_speed";


    public static final String GUN_DAMAGE_TAG = "Lgun_damage";
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

        // i write to the player data with my desired tags, this is persistent as its stored to the disk (i think)
        NBTTagCompound playerData = player.getEntityData();

        addTags(playerData);

        statsAndPerks(player);
    }

    private void addTags(NBTTagCompound playerDataIn) {
        // comes with stat increases then has perks at X amount consumed, usually 5 or 10
        // total fallout bobbleheads is 7
        if (usedStrengthBob) { // exponential, might get insane
            int damageFormula = (int) (playerDataIn.getInteger(ATTACK_DAMAGE_TAG) * 1.5F); // multiplies damage by 1.5, 50% damage increase for melee

            playerDataIn.setInteger(ATTACK_DAMAGE_TAG, damageFormula);

            if (playerDataIn.getInteger(ATTACK_DAMAGE_TAG) == 5) { // perk
                playerDataIn.setBoolean(ATTACK_DAMAGE_BONUS_TAG, true);
            }

            usedStrengthBob = false;
        }

        if (usedPerceptionBob) {
            playerDataIn.setInteger(GUN_ACCURACY_TAG, playerDataIn.getInteger(GUN_ACCURACY_TAG) + 1); // reduces spread

            if (playerDataIn.getInteger(GUN_ACCURACY_TAG) == 5) { // perk
                playerDataIn.setBoolean(NIGHT_VISION_TAG, true);
            }

            usedPerceptionBob = false;
        }

        if (usedEnduranceBob) {
            playerDataIn.setInteger(MAX_HEALTH_TAG, playerDataIn.getInteger(MAX_HEALTH_TAG) + 4); // +2 full hearts

            if (playerDataIn.getInteger(MAX_HEALTH_TAG) == 5) { // perk
                playerDataIn.setBoolean(MAX_HEALTH_BONUS_TAG, true);
            }

            usedEnduranceBob = false;
        }

        if (usedCharismaBob) { // plot armor
            playerDataIn.setInteger(ARMOR_TAG, playerDataIn.getInteger(ARMOR_TAG) + 2);
            playerDataIn.setInteger(ARMOR_TOUGHNESS_TAG, playerDataIn.getInteger(ARMOR_TOUGHNESS_TAG) + 1);

            usedCharismaBob = false;
        }

        if (usedIntelligenceBob) { // increases gun damage
            playerDataIn.setInteger(GUN_DAMAGE_TAG, playerDataIn.getInteger(GUN_DAMAGE_TAG) + 2);

            usedIntelligenceBob = false;
        }


        if (usedAgilityBob) { // percentage
            playerDataIn.setFloat(MOVEMENT_SPEED_TAG, playerDataIn.getFloat(MOVEMENT_SPEED_TAG) + 0.05F); // +5% movement speed

            // todo keybind for tweaking current move speed after acquiring 5 agility bob

            usedAgilityBob = false;
        }

        if (usedLuckBob) { // increases the chance of reloading rounds for free,
            playerDataIn.setInteger(LUCK_TAG, playerDataIn.getInteger(LUCK_TAG) + 1);

            usedAgilityBob = false;
        }
    }

    private void statsAndPerks(EntityPlayer playerIn) {
        // i get the tags from addTags() and handle the attribute modification or perks. The NBT tags are written to the player's data, its persistent and will (hopefully) be applied as long the game is running
        for (String tag : playerIn.getTags()) {

            // strength
            if (tag.equals(ATTACK_DAMAGE_TAG)) {
                playerIn.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier(ATTACK_DAMAGE_MODIFIER, nameIn, 1, 0));
            }
            //

            // perception
            // todo gun accuracy stat logic

            // perk
            if (tag.equals(NIGHT_VISION_TAG) && playerIn.getEntityData().getBoolean(NIGHT_VISION_TAG)) { // if i found NIGHT VISION TAG and its true
                playerIn.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(MAX_HEALTH_UUID, nameIn, playerIn.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH) + 1, 0));
            }
            //

            // endurance
            if (tag.equals(MAX_HEALTH_TAG)) {
                playerIn.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(MAX_HEALTH_UUID, nameIn, 1, 0));
            }
            //

            // charisma
            // two stats
            if (tag.equals(ARMOR_TAG)) {
                playerIn.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(new AttributeModifier(ARMOR_UUID, nameIn, 1, 0));
            }

        }
    }
}
