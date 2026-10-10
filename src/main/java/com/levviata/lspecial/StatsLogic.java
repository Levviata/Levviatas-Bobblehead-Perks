package com.levviata.lspecial;

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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

import static com.levviata.lspecial.LSPECIALMod.LOGGER;
import static com.levviata.lspecial.SPECIALScoreboard.inst;
import static com.levviata.lspecial.command.CommandSetFlySpeed.getPlayerFlySpeed;
import static com.levviata.lspecial.command.CommandSetSpeed.getPlayerSpeed;
import static com.levviata.lspecial.potion.PotionAmplifiedRegeneration.AMPLIFIED_REGENERATION_NAME;

public class StatsLogic {
    // todo add uuid getter helper from attribute modifier
    //vanilla uuids
    private static final UUID ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private static final UUID ATTACK_SPEED_UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
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

    private int previousHealth = -1;
    private int previousCharisma = -1;
    private int previousAgility = -1;

    private ItemStack modifiedGun = ItemStack.EMPTY;
    private Receiver modifiedReceiver;
    private boolean modified;
    private boolean installedMagazine;
    private float originalDamage;
    private int originalDelay;
    private int originalProjectileAmount;
    private float originalAmmoPiercing;
    private float originalDurability;
    private IMagazine originalMagazine;

    private int gunIndex = -1;
    int potionTime = 115;

    private ItemStack loggedSednaGun = ItemStack.EMPTY;
    private static final List<UUID> uuids = new ArrayList<>();

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
        g.add(new RecipesCommon.ComparableStack(ModItems.ammo_secret, 1, 3).getStack().getItem());
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

        if (player.world == null || player.world.isRemote) { // server sided
            return;
        }

        ItemStack heldStack = player.getHeldItemMainhand();

        inst.scoreboard = player.getEntityWorld().getScoreboard();

        // WARNING if the scores or scoreboards don't start, the mod is useless
        inst.startBoards();
        inst.startScores(player);

        // strength
        IAttributeInstance strength = player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        if (!heldStack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName()).isEmpty()) {
            Collection<AttributeModifier> damageCollection = heldStack.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName());

            double damage = 1 + damageCollection.iterator().next().getAmount();

            strength.removeModifier(ATTACK_DAMAGE_UUID);
            double newDamage = damage + inst.getStrengthScore().getScorePoints();
            strength.applyModifier(new AttributeModifier(ATTACK_DAMAGE_UUID, nameIn, newDamage, 0));
        }

        // endurance
        IAttributeInstance maxHealth = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
        if (inst.getEnduranceScore().getScorePoints() != previousHealth || player.getMaxHealth() != previousHealth) {
            previousHealth = inst.getEnduranceScore().getScorePoints();

            maxHealth.removeModifier(MAX_HEALTH_UUID);

            maxHealth.applyModifier(new AttributeModifier(MAX_HEALTH_UUID, nameIn, inst.getEnduranceScore().getScorePoints(), 0));
        }

        // charisma
        IAttributeInstance armor = player.getEntityAttribute(SharedMonsterAttributes.ARMOR);
        IAttributeInstance armorToughness = player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
        if (inst.getCharismaScore().getScorePoints() != previousCharisma) {
            previousCharisma = inst.getCharismaScore().getScorePoints();

            armor.removeModifier(ARMOR_UUID);
            armorToughness.removeModifier(ARMOR_TOUGHNESS_UUID);

            armor.applyModifier(new AttributeModifier(ARMOR_UUID, nameIn, inst.getCharismaScore().getScorePoints() * 2, 0));
            armorToughness.applyModifier(new AttributeModifier(ARMOR_TOUGHNESS_UUID, nameIn, inst.getCharismaScore().getScorePoints() * 0.5D, 0));
        }

        // agility
        IAttributeInstance movementSpeed = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (inst.getAgilityScore().getScorePoints() != previousAgility || getPlayerSpeed(player) != -1 && inst.getAgilityScore().getScorePoints() != getPlayerSpeed(player)) {
            if (getPlayerSpeed(player) > -1) {
                previousAgility = getPlayerSpeed(player);
            } else {
                previousAgility = inst.getAgilityScore().getScorePoints();
            }

            movementSpeed.removeModifier(MOVEMENT_SPEED_UUID);

            // operationIn: multiplicative
            // 7.5% increase per point up to 75%
            if (getPlayerSpeed(player) > -1) {
                movementSpeed.applyModifier(new AttributeModifier(MOVEMENT_SPEED_UUID, nameIn, getPlayerSpeed(player) * 0.075D, 1));
            } else {
                movementSpeed.applyModifier(new AttributeModifier(MOVEMENT_SPEED_UUID, nameIn, inst.getAgilityScore().getScorePoints() * 0.075D, 1));
            }
/*          this sucks, its either too fast or too slow
            float def = 0.05f;
            player.capabilities.setFlySpeed(def);
            // im not sure if this works, if it doesnt, maybe sync problems?
            if (getPlayerFlySpeed(player) > -1) {
                player.capabilities.setFlySpeed(player.capabilities.getFlySpeed() * (getPlayerFlySpeed(player) / 0.075f));
            } else {
                player.capabilities.setFlySpeed(player.capabilities.getFlySpeed() * (inst.getAgilityScore().getScorePoints() / 0.075f));
            }*/
        }
        //originalFlySpeed = player.capabilities.getFlySpeed();

       // LOGGER.info(player.capabilities.getFlySpeed());

        // perception, intelligence, and luck
        // todo make perception increase found loot

        if (inst.getIntelligenceScore().getScorePoints() >= 5) {
            if (heldStack.isItemStackDamageable()) {
                heldStack.getItem().setMaxDamage(-1);
            }
        }

        if (heldStack.getItem() instanceof ItemGunBaseSedna) {
            // to my knowledge, no guns inherit this class
            if (loggedSednaGun.isEmpty() || !ItemStack.areItemStacksEqual(loggedSednaGun, heldStack)) {
                LOGGER.info("Your held gun inherits ItemGunBaseSedna.class which don't expose damage variables, can't modify it. Your gun is: {}", heldStack);

                loggedSednaGun = heldStack.copy();
            }
        }

        if (heldStack.getItem() instanceof ItemGunBaseNT) {
            if (!modified || !ItemStack.areItemStacksEqual(modifiedGun, heldStack)) {
                // clean for this cycle
                if (modified) {
                    cleanGun(heldStack, player);
                }

                ItemGunBaseNT gun = (ItemGunBaseNT) heldStack.getItem();
                int configs = gun.getConfigCount();
                for (gunIndex = 0; gunIndex < configs; gunIndex++) {
                    // modify guns using their unique gun config
                    // akimbo guns have two configs so this loop would trigger twice, modifying both
                    modifyGuns(heldStack, player, gun.getConfig(heldStack, gunIndex));
                }

                modifiedGun = heldStack.copy();
                modified = true;
            }
        } else if (modified) {
            //clean for next cycle
            cleanGun(heldStack, player);
        }

        givePotions(player);
    }

    public void modifyGuns(ItemStack heldStack, EntityPlayer player, GunConfig cfg) {
        Receiver[] receivers = cfg.getReceivers(heldStack);

        if (inst.getIntelligenceScore().getScorePoints() >= 5) {
            ItemGunBaseNT.setWear(heldStack, 0, 0);
        }

        modifiedReceiver = receivers[0];

        BulletConfig bCOne = (BulletConfig) modifiedReceiver.getMagazine(heldStack).getType(heldStack, player.inventory);

        setOriginalValues(bCOne, modifiedReceiver, heldStack);

        if (!installedMagazine) {
            if (inst.getLuckScore().getScorePoints() > 0) { // if i have luck, install a magazine that can refund bullets
                modifiedReceiver.mag(
                        new RefundMagazine(originalMagazine)
                );

                installedMagazine = true;
            }
        }

        processReceiver(modifiedReceiver, originalDamage, originalDelay, heldStack, player);
    }

    private void processReceiver(Receiver recIn, float ogDmg, int ogDelay, ItemStack heldStackIn, EntityPlayer player) {
        ItemGunBaseNT gun = (ItemGunBaseNT) heldStackIn.getItem();

        recIn.dmg(ogDmg + inst.getPerceptionScore().getScorePoints());
        //LOGGER.info(recIn.getInnateSpread(heldStackIn));

        BulletConfig bC = (BulletConfig) recIn.getMagazine(heldStackIn).getType(heldStackIn, player.inventory);
        ItemStack ammoStack;
        if (bC.ammo != null) {
            ItemStack c = bC.ammo.getStack();

            ammoStack = player.inventory.mainInventory.stream()
                    .filter(stack -> stack.getItem() == c.getItem())
                    .findFirst()
                    .orElse(ItemStack.EMPTY);
        } else {
            ammoStack = ItemStack.EMPTY;
        }

        int modifiedDelay = ogDelay - (inst.getIntelligenceScore().getScorePoints() / 10);

        if (modifiedDelay > 0) {
            int formula = ogDelay - (inst.getIntelligenceScore().getScorePoints() / 100);
            if (/*recIn.getRefireOnHold(heldStackIn) ||*/ g.contains(ammoStack.getItem())) { // automatic or loaded with buckshots (usually shotguns)
                modifiedDelay = formula;
                //LOGGER.info("setting a nerfed delay");
            }
        } else {
            modifiedDelay = 0;
        }
        recIn.delay(modifiedDelay);

        if (inst.getIntelligenceScore().getScorePoints() >= 5) { // lesser perk
            recIn.auto(true);
            if (!heldStackIn.isEmpty()) {
                String rName = String.valueOf(heldStackIn.getItem().getRegistryName());
                if (rName.equals("hbm:gun_pepperbox")) {
                    recIn.rounds(6);
                }
                if (rName.equals("hbm:gun_lag")) {
                    // todo make right click shift click run empty mag animation to then kill yourself
                }
            }
        }
        if (inst.getIntelligenceScore().getScorePoints() >= 10) { // higher perk
            if (!heldStackIn.isEmpty()) {
                String rName = String.valueOf(heldStackIn.getItem().getRegistryName());
                if (r.contains(rName) ) { // match to all revolvers, thought of madness combat revolvers doing hell damage, so it's in
                    /*int piercing = 25;
                    bC.armorThresholdNegation = piercing;
                    bC.armorPiercingPercent = ogPiercing + piercing; // since guns dont have piercing in their receivers, this adds onto the ammo's piercing*/
                    bC.doesPenetrate = true;
                    bC.setHeadshot(3);
                }
            }
        }
    }

    public void cleanGun(ItemStack heldStack, EntityPlayer player) {
        BulletConfig bCOne = (BulletConfig) modifiedReceiver.getMagazine(heldStack).getType(heldStack, player.inventory);

        if (gunIndex != -1) {
            for (int i = 0; i < gunIndex; i++) { // run through all gun configs
                ItemGunBaseNT.setWear(heldStack, i, originalDurability);
            }
        }
        bCOne.armorPiercingPercent = originalAmmoPiercing;
        modifiedReceiver.dmg(originalDamage);
        modifiedReceiver.delay(originalDelay);
        modifiedReceiver.rounds(originalProjectileAmount);
        modifiedReceiver.mag(originalMagazine);

        gunIndex = 0;
        installedMagazine = false;
        modifiedGun = ItemStack.EMPTY;
        modifiedReceiver = null;
        modified = false;
    }

    public void setOriginalValues(BulletConfig bCIn, Receiver rIn, ItemStack heldStack) {
        ItemGunBaseNT gun = (ItemGunBaseNT) heldStack.getItem();
        GunConfig cfg = gun.getConfig(heldStack, gunIndex);

        originalDurability = cfg.getDurability(heldStack);
        originalMagazine = rIn.getMagazine(heldStack);
        originalAmmoPiercing = bCIn.armorPiercingPercent;
        originalDamage = rIn.getBaseDamage(heldStack);
        originalDelay = rIn.getDelayAfterFire(heldStack);
        originalProjectileAmount = rIn.getRoundsPerCycle(heldStack);
    }

    public void givePotions(EntityPlayer player) {
        if (inst.getEnduranceBonusScore().getScorePoints() == 1) {
            String p = Tags.MOD_ID + ":" + AMPLIFIED_REGENERATION_NAME;
            if (Potion.getPotionFromResourceLocation("regeneration") != null && Potion.getPotionFromResourceLocation(p) != null) {
                Potion a = Potion.getPotionFromResourceLocation("regeneration");
                Potion b = Potion.getPotionFromResourceLocation(p);
                if (player.isPotionActive(a)) {
                    // todo config to opt out of AMPLIFIED_REGENERATION and simply overwrite bonus if current has higher amplifier effect
                    int cAmplifier;
                    PotionEffect ae = player.getActivePotionEffect(a);
                    if (ae != null) {
                        cAmplifier = ae.getAmplifier();
                        player.removePotionEffect(a);
                        player.addPotionEffect(new PotionEffect(b, ae.getDuration(), cAmplifier + 1));
                    }
                } else if (!player.isPotionActive(b)) {
                    player.addPotionEffect(new PotionEffect(b, potionTime, 1));
                }
            }
        }

        if (inst.getCharismaBonusScore().getScorePoints() == 1) {
            if (Potion.getPotionFromResourceLocation("resistance") != null && !player.isPotionActive(Potion.getPotionFromResourceLocation("resistance"))) {
                player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("resistance"), potionTime, 0));
            }
        }
    }


    public static List<UUID> getUUIDs() {
        if (uuids.isEmpty()) {
            uuids.add(ATTACK_DAMAGE_UUID);
            uuids.add(ATTACK_SPEED_UUID);
            uuids.add(MAX_HEALTH_UUID);
            uuids.add(FOLLOW_RANGE_UUID);
            uuids.add(KNOCKBACK_RESISTANCE_UUID);
            uuids.add(MOVEMENT_SPEED_UUID);
            uuids.add(FLYING_SPEED_UUID);
            uuids.add(ARMOR_UUID);
            uuids.add(ARMOR_TOUGHNESS_UUID);
            uuids.add(LUCK_UUID);
        }
        return uuids;
    }
}
