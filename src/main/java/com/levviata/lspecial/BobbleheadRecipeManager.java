
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
        registerConfiguredEntries(LSPECIALConfig.oreAnyBobblehead, LSPECIALConfig.oreAnyBobbleheadEntries);
        registerConfiguredEntries(LSPECIALConfig.oreAppleLead, LSPECIALConfig.oreAppleLeadEntries);
        registerConfiguredEntries(LSPECIALConfig.oreAmmo, LSPECIALConfig.oreAmmoEntries);
        registerConfiguredEntries(LSPECIALConfig.oreLuckAccessory, LSPECIALConfig.oreLuckAccessoryEntries);
        registerConfiguredEntries(LSPECIALConfig.oreLuckyGem, LSPECIALConfig.oreLuckyGemEntries);
    }

    private static void registerConfiguredEntries(String oreName, String entries) {
        for (String raw : entries.split(",")) {
            String token = raw.trim(); if (token.isEmpty()) continue;
            String[] parts = token.split(":");
            if (parts.length < 3 || parts.length > 3) throw new IllegalArgumentException("Ore entry must be namespace:item:meta or namespace:item:start-end: " + token);
            Item item = getItem(parts[0], parts[1]);
            String meta = parts[2];
            if (meta.contains("-")) {
                String[] range = meta.split("-"); int min = Integer.parseInt(range[0]); int max = Integer.parseInt(range[1]);
                if (max < min || max - min > 4096) throw new IllegalArgumentException("Invalid metadata range: " + token);
                for (int m = min; m <= max; m++) OreDictionary.registerOre(oreName, new ItemStack(item, 1, m));
            } else OreDictionary.registerOre(oreName, new ItemStack(item, 1, Integer.parseInt(meta)));
        }
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

        for (int i = 0; i < 7; i++) {
            if (!LSPECIALConfig.recipeEnabled[i]) continue;
            try {
                addConfiguredRecipe(event, i + 1, LSPECIALConfig.recipeIngredients[i]);
            } catch (RuntimeException ex) {
                LSPECIALMod.LOGGER.error("Could not register configurable bobblehead recipe {}. Keeping it disabled for this launch: {}", i + 1, ex.getMessage());
            }
        }
    }

    private void addConfiguredRecipe(RegistryEvent.Register<net.minecraft.item.crafting.IRecipe> event, int index, String spec) {
        List<Ingredient> ingredients = new java.util.ArrayList<>();
        for (String raw : spec.split(",")) {
            String token = raw.trim();
            if (token.isEmpty()) continue;
            if (token.startsWith("potion:")) {
                String[] names = token.substring("potion:".length()).split("\\|");
                PotionType[] types = new PotionType[names.length];
                for (int i = 0; i < names.length; i++) {
                    String name = names[i].trim();
                    ResourceLocation id = name.contains(":") ? new ResourceLocation(name) : new ResourceLocation("minecraft", name);
                    types[i] = ForgeRegistries.POTION_TYPES.getValue(id);
                    if (types[i] == null) throw new IllegalArgumentException("Unknown potion type " + id);
                }
                ingredients.add(potion(types));
            } else if (!token.contains(":")) {
                String oreName = token;
                if (token.equals("anyBobblehead")) oreName = LSPECIALConfig.oreAnyBobblehead;
                else if (token.equals("appleLead")) oreName = LSPECIALConfig.oreAppleLead;
                else if (token.equals("ammo")) oreName = LSPECIALConfig.oreAmmo;
                else if (token.equals("luckAccessory")) oreName = LSPECIALConfig.oreLuckAccessory;
                else if (token.equals("luckyGem")) oreName = LSPECIALConfig.oreLuckyGem;
                ingredients.add(ore(oreName));
            } else {
                String[] parts = token.split(":");
                if (parts.length < 2 || parts.length > 3) throw new IllegalArgumentException("Expected namespace:item[:metadata], got " + token);
                int meta = parts.length == 3 ? Integer.parseInt(parts[2]) : 0;
                ingredients.add(item(parts[0], parts[1], meta));
            }
        }
        if (ingredients.isEmpty()) throw new IllegalArgumentException("No ingredients specified");
        addRecipe(event, "bobblehead_" + index, index, ingredients.toArray(new Ingredient[0]));
    }

}
