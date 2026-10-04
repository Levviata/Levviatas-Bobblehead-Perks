package com.levviata.lbob;

import com.hbm.inventory.RecipesCommon;
import com.hbm.items.ModItems;
import com.hbm.items.weapon.sedna.*;
import com.hbm.items.weapon.sedna.mags.IMagazine;
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

import java.util.*;

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


    private int previousHealth = -1;
    private int previousCharisma = -1;

    private ItemStack modifiedGun = ItemStack.EMPTY;
    private Receiver modifiedReceiverOne;
    private Receiver modifiedReceiverTwo;
    private float originalDamageOne;
    private float originalDamageTwo;
    private int originalDelayOne;
    private int originalDelayTwo;
    private int originalProjectileAmountOne;
    private int originalProjectileAmountTwo;
    private float originalAmmoPiercingOne;
    private float originalAmmoPiercingTwo;
    private boolean modified;

    private int timer;

    private ItemStack loggedSednaGun = ItemStack.EMPTY;

    static List<Item> g = new ArrayList<>();
    static List<String> r = new ArrayList<>();
    public static void init() {
        g.add(new RecipesCommon.ComparableStack(ModItems.ammo_standard, 1, 41).getStack().getItem());
        g.add(new RecipesCommon.ComparableStack(ModItems.ammo_standard, 1, 42).getStack().getItem());
        g.add(new RecipesCommon.ComparableStack(ModItems.ammo_standard, 1, 43).getStack().getItem());
        g.add(new RecipesCommon.ComparableStack(ModItems.ammo_standard, 1, 44).getStack().getItem());
        g.add(new RecipesCommon.ComparableStack(ModItems.ammo_standard, 1, 45).getStack().getItem());
        g.add(new RecipesCommon.ComparableStack(ModItems.ammo_standard, 1, 46).getStack().getItem());
        g.add(new RecipesCommon.ComparableStack(ModItems.ammo_standard, 1, 47).getStack().getItem());
        g.add(new RecipesCommon.ComparableStack(ModItems.ammo_standard, 1, 48).getStack().getItem());
        g.add(new RecipesCommon.ComparableStack(ModItems.ammo_standard, 1, 49).getStack().getItem());
        g.add(new RecipesCommon.ComparableStack(ModItems.ammo_standard, 1, 78).getStack().getItem());
        g.add(new RecipesCommon.ComparableStack(ModItems.ammo_standard, 1, 79).getStack().getItem());
        g.add(new RecipesCommon.ComparableStack(ModItems.ammo_standard, 1, 80).getStack().getItem());
        g.add(new RecipesCommon.ComparableStack(ModItems.ammo_standard, 1, 81).getStack().getItem());
        g.add(new RecipesCommon.ComparableStack(ModItems.ammo_standard, 1, 84).getStack().getItem());
        r.add("hbm:gun_flaregun");
        r.add("hbm:gun_heavy_revolver");
        r.add("hbm:gun_light_revolver_atlas");
        r.add("hbm:gun_light_revolver");
    }


    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }


        EntityPlayer player = event.player;

        if (player.world == null || player.world.isRemote) {
            return;
        }

        if (player.inventory == null) {
            LOGGER.info("inv null top");
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
        // perk 5 1 more projectile per 5 shots and 10% damage pen
        // perk 10 1 more projectile every shot with a 15% physical
        // single fire same

        ItemStack heldStack = player.getHeldItemMainhand();

        if (heldStack.getItem() instanceof ItemGunBaseSedna) {
            // to my knowledge, no guns inherit this class
            if (loggedSednaGun.isEmpty() || !ItemStack.areItemStacksEqual(loggedSednaGun, heldStack)) {
                LOGGER.info("Your held gun inherits ItemGunBaseSedna.class which don't expose damage variables, can't modify it. Your gun is: {}", heldStack);

                loggedSednaGun = heldStack.copy();
            }
        }

        if (heldStack.getItem() instanceof ItemGunBaseNT) {
            if (!modified || !ItemStack.areItemStacksEqual(modifiedGun, heldStack)) {
                if (modified) {
                    modifiedReceiverOne.dmg(originalDamageOne);

                    if (modifiedReceiverTwo != null) {
                        modifiedReceiverTwo.dmg(originalDamageTwo);
                    }

                    modified = false;
                }

                ItemGunBaseNT gun = (ItemGunBaseNT) heldStack.getItem();
                GunConfig config = gun.getConfig(heldStack, 0);
                Receiver[] receivers = config.getReceivers(heldStack);

                modifiedReceiverOne = receivers[0];

                BulletConfig bCOne = (BulletConfig) modifiedReceiverOne.getMagazine(heldStack).getType(heldStack, player.inventory);

                originalAmmoPiercingOne = bCOne.armorPiercingPercent;
                originalDamageOne = modifiedReceiverOne.getBaseDamage(heldStack);
                originalDelayOne = modifiedReceiverOne.getDelayAfterFire(heldStack);
                originalProjectileAmountOne = modifiedReceiverOne.getRoundsPerCycle(heldStack);

                // 1
                processReceiver(modifiedReceiverOne, originalDamageOne, originalDelayOne, originalAmmoPiercingOne, intelligenceScore, heldStack, player);

                // 2
                modifiedReceiverTwo = null;
                if (receivers.length > 1) {
                    modifiedReceiverTwo = receivers[1];

                    BulletConfig bCTwo = (BulletConfig) modifiedReceiverTwo.getMagazine(heldStack).getType(heldStack, player.inventory);

                    originalAmmoPiercingTwo = bCTwo.armorPiercingPercent;
                    originalDamageTwo = modifiedReceiverTwo.getBaseDamage(heldStack);
                    originalDelayTwo = modifiedReceiverTwo.getDelayAfterFire(heldStack);
                    originalProjectileAmountTwo = modifiedReceiverTwo.getRoundsPerCycle(heldStack);

                    processReceiver(modifiedReceiverTwo, originalDamageTwo, originalDelayTwo, originalAmmoPiercingTwo, intelligenceScore, heldStack, player);
                }

                modifiedGun = heldStack.copy();
                modified = true;
            }
        } else if (modified) {
            BulletConfig bCOne = (BulletConfig) modifiedReceiverOne.getMagazine(heldStack).getType(heldStack, player.inventory);

            bCOne.armorPiercingPercent = originalAmmoPiercingOne;
            modifiedReceiverOne.dmg(originalDamageOne);
            modifiedReceiverOne.delay(originalDelayOne);
            modifiedReceiverOne.rounds(originalProjectileAmountOne);


            if (modifiedReceiverTwo != null) {
                BulletConfig bCTwo = (BulletConfig) modifiedReceiverTwo.getMagazine(heldStack).getType(heldStack, player.inventory);

                bCTwo.armorPiercingPercent = originalAmmoPiercingTwo;
                modifiedReceiverTwo.dmg(originalDamageTwo);
                modifiedReceiverTwo.delay(originalDelayTwo);
                modifiedReceiverTwo.rounds(originalProjectileAmountTwo);
            }

            modifiedGun = ItemStack.EMPTY;
            modifiedReceiverOne = null;
            modifiedReceiverTwo = null;
            modified = false;
        }
    }

    private void processReceiver(Receiver recIn, float ogDmg, int ogDelay, float ogPiercing, Score intelligenceScoreIn, ItemStack heldStackIn, EntityPlayer player) {
        recIn.dmg(ogDmg + intelligenceScoreIn.getScorePoints());

        BulletConfig bC = (BulletConfig) recIn.getMagazine(heldStackIn).getType(heldStackIn, player.inventory);

        int modifiedDelay = ogDelay - (intelligenceScoreIn.getScorePoints() / 10);


        if (modifiedDelay > 0) {
            int formula = ogDelay - (intelligenceScoreIn.getScorePoints() / 100);
            if (recIn.getRefireOnHold(heldStackIn)) { // automatic
                modifiedDelay = formula;
                //LOGGER.info("setting a nerfed delay");
            }
            if (bC.ammo != null) {
                if (g.contains(bC.ammo.getStack().getItem())) { // loaded with buckshots (usually shotguns)
                    modifiedDelay = formula;
                }
            }

        } else {
            modifiedDelay = 1;
        }
        recIn.delay(modifiedDelay);

        if (intelligenceScoreIn.getScorePoints() >= 5) {
            if (!heldStackIn.isEmpty()) {
                String rName = String.valueOf(heldStackIn.getItem().getRegistryName());
                if (rName.equals("hbm:gun_pepperbox")) {
                    recIn.rounds(6);
                }
                if (r.contains(rName) ) { // match to all revolvers, thought of madness combat revolvers doing hell damage, so it's in
                    int piercing = 25;
                    bC.armorThresholdNegation = piercing;
                    bC.armorPiercingPercent = ogPiercing + piercing; // since guns dont have piercing in their receivers, this adds onto the ammo's piercing
                    bC.doesPenetrate = true;
                    bC.setHeadshot(2);
                }
            }
        }
    }
}
