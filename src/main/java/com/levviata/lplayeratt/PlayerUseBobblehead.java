package com.levviata.lplayeratt;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;
import java.util.UUID;

import static com.levviata.lplayeratt.LeviathanPlayerAttributes.*;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class PlayerUseBobblehead {

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

    @SubscribeEvent
    public void use(PlayerInteractEvent.RightClickItem event) {
        ItemStack key = new ItemStack(Objects.requireNonNull(Item.getByNameOrId("minecraft:iron_ingot")));
        LOGGER.info("Hi");
        LOGGER.info(key);
        String s = String.valueOf(event.getItemStack().getItem().getRegistryName());
        LOGGER.info(s);
        if (event.getItemStack().getItem().equals(key.getItem())) {
            // the goal is when a hbm:bobblehead is crouch right-clicked, I remove the item and give the player a nbt tag which is handled by LNBTMagic.class
            LOGGER.info("i got the iron_ingot");

            if (!event.getEntityPlayer().getTags().contains(MAX_HEALTH_TAG)) {

            }

            /*for (String tag : event.getEntityPlayer().getTags()) {
                if (!tag.equals(MAX_HEALTH_TAG)) {
                    event.getEntityPlayer().addTag();
                }
            }*/

            LOGGER.info("i got the iron_ingot");
            /*
            // for int operationIn value:
            // 0 addition, 1 multiply base, 2 multiply total
            event.getEntityPlayer().getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier(MAX_HEALTH_UUID, nameIn, 1, 0));*/
        }
    }
}
