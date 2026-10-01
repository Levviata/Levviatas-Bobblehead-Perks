package com.levviata.lbob;

import com.hbm.items.weapon.sedna.GunConfig;
import com.hbm.items.weapon.sedna.ItemGunBaseNT;
import com.hbm.items.weapon.sedna.ItemGunBaseSedna;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import com.hbm.items.weapon.sedna.Receiver;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import static com.levviata.lbob.LeviathanPlayerAttributes.scoreboard;
import static com.levviata.lbob.PlayerUseBobblehead.*;
import static com.levviata.lbob.LeviathanPlayerAttributes.LOGGER;

public class PlayerStats {
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

    private int previousHealth = -1;
    private int previousCharisma = -1;
    float receiverOneLastDmg = -1;
    float receiverTwoLastDmg = -1;
    boolean hasTwoReceivers = false;
    boolean gotBaseDmg = false;

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        boolean hasRunStrength = false;

        EntityPlayer player = event.player;

        if (player.getEntityWorld().isRemote) {
            return;
        }

        scoreboard = player.getEntityWorld().getScoreboard();

        if (scoreboard.getObjective(STRENGTH_BOARD) == null) {
            scoreboard.addScoreObjective(STRENGTH_BOARD, IScoreCriteria.DUMMY);
        }
        if (scoreboard.getObjective(ENDURANCE_BOARD) == null) {
            scoreboard.addScoreObjective(ENDURANCE_BOARD, IScoreCriteria.DUMMY);
        }
        if (scoreboard.getObjective(CHARISMA_BOARD) == null) {
            scoreboard.addScoreObjective(CHARISMA_BOARD, IScoreCriteria.DUMMY);
        }
        if (scoreboard.getObjective(INTELLIGENCE_BOARD) == null) {
            scoreboard.addScoreObjective(INTELLIGENCE_BOARD, IScoreCriteria.DUMMY);
        }

        // strength
        ScoreObjective objStrength = scoreboard.getObjective(STRENGTH_BOARD);
        Score strengthScore = scoreboard.getOrCreateScore(player.getName(), objStrength);

        IAttributeInstance strength = player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        if (!player.getHeldItemMainhand().getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName()).isEmpty()) {
            ItemStack stack = player.getHeldItemMainhand();
            Collection<AttributeModifier> damageCollection = stack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());

            double damage = 1 + damageCollection.iterator().next().getAmount();

            strength.removeModifier(ATTACK_DAMAGE_MODIFIER);
            double newDamage = damage + strengthScore.getScorePoints();
            strength.applyModifier(new AttributeModifier(ATTACK_DAMAGE_MODIFIER, nameIn, newDamage, 0));
/*
            for (int i = 0; i < stack.getTooltip(player, () -> false).size(); i++) {
                if (stack.getTooltip(player, () -> false).get(i).contains("Damage")) {
                    String s = newDamage + " Attack Damage";
                    stack.getTooltip(player, () -> false).set(i, s);
                }
            }*/
        }

        // perception
        // todo gun accuracy stat logic

        // endurance
        ScoreObjective objEndurance = scoreboard.getObjective(ENDURANCE_BOARD);
        Score endurance = scoreboard.getOrCreateScore(player.getName(), objEndurance);

        IAttributeInstance maxHealth = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
        if (endurance.getScorePoints() != previousHealth || player.getMaxHealth() != previousHealth) {
            previousHealth = endurance.getScorePoints();
            maxHealth.removeModifier(MAX_HEALTH_UUID);
            maxHealth.applyModifier(new AttributeModifier(MAX_HEALTH_UUID, nameIn, endurance.getScorePoints(), 0));
        }

        // charisma
        ScoreObjective objCharisma = scoreboard.getObjective(CHARISMA_BOARD);
        Score charismaScore = scoreboard.getOrCreateScore(player.getName(), objCharisma);

        IAttributeInstance armor = player.getEntityAttribute(SharedMonsterAttributes.ARMOR);
        IAttributeInstance armorToughness = player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
        if (charismaScore.getScorePoints() != previousCharisma) {
            previousCharisma = charismaScore.getScorePoints();

            armor.removeModifier(ARMOR_UUID);
            armorToughness.removeModifier(ARMOR_TOUGHNESS_UUID);

            armor.applyModifier(new AttributeModifier(ARMOR_UUID, nameIn, charismaScore.getScorePoints() * 2, 0));
            armorToughness.applyModifier(new AttributeModifier(ARMOR_TOUGHNESS_UUID, nameIn, charismaScore.getScorePoints() * 0.5D, 0));
        }

        // intelligence
        ScoreObjective objIntelligence = scoreboard.getObjective(INTELLIGENCE_BOARD);
        Score intelligenceScore = scoreboard.getOrCreateScore(player.getName(), objIntelligence);

        // idea,
        // perk 5 1 more projectile per 5 shots,
        // perk 10 right and left duals interchange with double damage on each shot every 5 shots with 1 more projectile,
        // single fire same

        // to modify weapon damage i guess i can call setter Receiver dmg() method, set it, then set it back when item unequipped
        if (player.getHeldItemMainhand().getItem() instanceof ItemGunBaseSedna) {
            ItemGunBaseSedna gun = (ItemGunBaseSedna) player.getHeldItemMainhand().getItem();
            LOGGER.info("we have sedna gun {}", gun);
        }

        ItemStack gunStack = ItemStack.EMPTY;
        if (player.getHeldItemMainhand().getItem() instanceof ItemGunBaseNT) {
            ItemGunBaseNT currentGun = (ItemGunBaseNT) player.getHeldItemMainhand().getItem();
            gunStack = player.getHeldItemMainhand();

            LOGGER.info("we have nt gun {}", currentGun);

            GunConfig c = currentGun.getConfig(player.getHeldItemMainhand(), 0);

            // single receiver per gun
            // akimbo, or dual, guns have two receivers
            // test revolver has two as well but that doesn't exist in CE
            Receiver[] receivers = c.getReceivers(player.getHeldItemMainhand());
            Receiver one = new Receiver(0);
            Receiver two = new Receiver(0);
            hasTwoReceivers = false;

            for (int i = 0; i < receivers.length; i++) {
                one = receivers[0];
                if (receivers.length > 1) {
                    hasTwoReceivers = true;
                    two = receivers[1];
                }
            }

            if (!gotBaseDmg) {
                receiverOneLastDmg = one.getBaseDamage(gunStack);
                if (hasTwoReceivers) {
                    receiverTwoLastDmg = two.getBaseDamage(gunStack);
                }
                gotBaseDmg = true;
            }

            one.dmg(one.getBaseDamage(gunStack) + intelligenceScore.getScorePoints());
            if (hasTwoReceivers) {
                    two.dmg(two.getBaseDamage(gunStack) + intelligenceScore.getScorePoints());
            }

           /* if (one.getBaseDamage(gunStack) != receiverOneLastDmg) {
                one.dmg(one.getBaseDamage(gunStack) + intelligenceScore.getScorePoints());
            }

            if (hasTwoReceivers) {
                if (two.getBaseDamage(gunStack) != receiverTwoLastDmg) {
                    two.dmg(two.getBaseDamage(gunStack) + intelligenceScore.getScorePoints());
                }
            }*/


        }
        if (gunStack != ItemStack.EMPTY && gunStack != player.getHeldItemMainhand() && gunStack.getItem() instanceof ItemGunBaseNT) {
            ItemGunBaseNT heldOffGun = (ItemGunBaseNT) gunStack.getItem();

            GunConfig c = heldOffGun.getConfig(gunStack, 0);

            Receiver[] receivers = c.getReceivers(player.getHeldItemMainhand());
            hasTwoReceivers = false;

            for (int i = 0; i < receivers.length; i++) {
                receivers[0].dmg(receiverOneLastDmg);
                if (receivers.length > 1) {
                    hasTwoReceivers = true;
                    receivers[1].dmg(receiverTwoLastDmg);
                }
            }
            receiverOneLastDmg = -1;
            if (hasTwoReceivers) {
                receiverTwoLastDmg = -1;
            }
            gotBaseDmg = false;
        }
    }
}
