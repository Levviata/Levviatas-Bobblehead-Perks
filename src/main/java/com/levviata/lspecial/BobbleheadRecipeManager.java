
package com.levviata.lspecial;

import java.util.Arrays;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BobbleheadRecipeManager {

    private static final String HBM = "hbm";

    private static boolean oreDictRegistered = false;

    public static void registerOreDict() {
        registerRange("anyBobblehead", "bobblehead", 8, 28);
        registerRange("appleLead", "apple_lead", 0, 2);
        registerRange("ammo", "ammo_standard", 0, 94);

        registerSingle("luckAccessory", "bandaid", 0);
        registerSingle("luckAccessory", "serum", 0);
        registerSingle("luckAccessory", "spider_milk", 0);
        registerSingle("luckAccessory", "heart_piece", 0);
        registerSingle("luckAccessory", "wd40", 0);

        OreDictionary.registerOre("luckyGem", new ItemStack(Items.EMERALD));
        OreDictionary.registerOre("luckyGem", new ItemStack(Items.DIAMOND));
    }

    private static void registerRange(String oreName, String itemName, int min, int max) {
        Item item = getItem(HBM, itemName);

        for (int meta = min; meta <= max; meta++) {
            OreDictionary.registerOre(oreName, new ItemStack(item, 1, meta));
        }
    }

    private static void registerSingle(String oreName, String itemName, int meta) {
        OreDictionary.registerOre(oreName, new ItemStack(getItem(HBM, itemName), 1, meta));
    }

    private static Item getItem(String namespace, String name) {
        ResourceLocation id = new ResourceLocation(namespace, name);
        Item item = ForgeRegistries.ITEMS.getValue(id);

        if (item == null || item == Items.AIR) {
            throw new IllegalStateException("Missing item: " + id);
        }

        return item;
    }

    private Ingredient item(String namespace, String name, int meta) {
        return Ingredient.fromStacks(new ItemStack(getItem(namespace, name), 1, meta));
    }

    private Ingredient hbm(String name) {
        return item(HBM, name, 0);
    }

    private Ingredient hbmMeta(String name, int meta) {
        return item(HBM, name, meta);
    }

    private Ingredient ore(String name) {
        List<ItemStack> stacks = OreDictionary.getOres(name);

        if (stacks.isEmpty()) {
            throw new IllegalStateException("Ore Dictionary entry is empty: " + name);
        }

        return Ingredient.fromStacks(stacks.toArray(new ItemStack[0]));
    }

    private Ingredient potion(PotionType... types) {
        ItemStack[] stacks = new ItemStack[types.length];

        for (int i = 0; i < types.length; i++) {
            stacks[i] = net.minecraft.potion.PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), types[i]);
        }

        return IngredientNBT.fromStacks(stacks);
    }

    private void addRecipe(RegistryEvent.Register<net.minecraft.item.crafting.IRecipe> event, String name, int outputMeta, Ingredient... ingredients) {
        NonNullList<Ingredient> input = NonNullList.create();
        input.addAll(Arrays.asList(ingredients));

        ItemStack output = new ItemStack(getItem(HBM, "bobblehead"), 1, outputMeta);

        ShapelessOreRecipe recipe = new ShapelessOreRecipe(new ResourceLocation(Tags.MOD_ID, name), input, output);

        recipe.setRegistryName(new ResourceLocation(Tags.MOD_ID, name));
        event.getRegistry().register(recipe);
    }


    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<net.minecraft.item.crafting.IRecipe> event) {
        // item registration has progressed after preInit
        if (!oreDictRegistered) {
            registerOreDict();
            oreDictRegistered = true;
        }

        Ingredient bobblehead = ore("anyBobblehead");

        addRecipe(event, "bobblehead_1", 1,
                bobblehead,
                hbm("schnitzel_vegan"),
                ore("appleLead"),
                hbm("ingot_u235")
        );

        addRecipe(event, "bobblehead_2", 2,
                bobblehead,
                hbmMeta("weapon_mod_special", 1),
                hbmMeta("weapon_mod_special", 6),
                ore("ammo")
        );

        addRecipe(event, "bobblehead_3", 3,
                bobblehead,
                hbm("med_bag"),
                hbm("crackpipe"),
                potion(PotionTypes.HEALING, PotionTypes.STRONG_HEALING)
        );

        addRecipe(event, "bobblehead_4", 4,
                bobblehead,
                hbm("cobalt_helmet"),
                hbm("steel_helmet"),
                hbm("titanium_helmet")
        );

        addRecipe(event, "bobblehead_5", 5,
                bobblehead,
                hbmMeta("part_mechanism", 34),
                hbmMeta("circuit", 9),
                hbm("gun_kit_2")
        );

        addRecipe(event, "bobblehead_6", 6,
                bobblehead,
                hbm("cotton_candy"),
                potion(
                        PotionTypes.SWIFTNESS,
                        PotionTypes.LONG_SWIFTNESS,
                        PotionTypes.STRONG_SWIFTNESS
                ),
                Ingredient.fromStacks(new ItemStack(Items.SUGAR))
        );

        addRecipe(event, "bobblehead_7", 7,
                bobblehead,
                ore("ammo"),
                ore("luckyGem"),
                ore("luckAccessory")
        );
    }

}
