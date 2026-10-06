package com.levviata.lspecial;

import com.levviata.lspecial.command.CommandSetFlySpeed;
import com.levviata.lspecial.command.CommandSetSpeed;
import com.levviata.lspecial.potion.PotionAmplifiedRegeneration;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import static com.levviata.lspecial.potion.PotionAmplifiedRegeneration.AMPLIFIED_REGENERATION_NAME;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class LSPECIALMod {

    // todo: 1. bobbleheads above meta 7 augment limit. 2. recipes for them to reduce grinding

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

   /* @SidedProxy(clientSide = "com.levviata.lspecial.proxy.ClientProxy", serverSide = "com.levviata.lspecial.proxy.ServerProxy")
    private static CommonProxy proxy;*/

    /*@Mod.Instance
    private static LSPECIALMod inst;*/

    //add main menu text "You are SPECIAL",

    public static KeyBinding toggleSpecial = new KeyBinding(
            "toggleSpecial",
            Keyboard.KEY_P,
            "key.categories.misc"
    );

    public static final Potion AMPLIFIED_REGENERATION = new PotionAmplifiedRegeneration();


    /**
     * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
     *     Take a look at how many FMLStateEvents you can listen to via the @Mod.EventHandler annotation here
     * </a>
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new BobbleheadUse());
        MinecraftForge.EVENT_BUS.register(new StatsLogic());
        MinecraftForge.EVENT_BUS.register(new FlySpeedFix());
        MinecraftForge.EVENT_BUS.register(new SPECIALRenamer());
        MinecraftForge.EVENT_BUS.register(new GuiSPECIAL());

        AMPLIFIED_REGENERATION.setRegistryName(Tags.MOD_ID, AMPLIFIED_REGENERATION_NAME);

        ForgeRegistries.POTIONS.register(AMPLIFIED_REGENERATION);

        ClientRegistry.registerKeyBinding(toggleSpecial);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        StatsLogic.init();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandSetSpeed());
        event.registerServerCommand(new CommandSetFlySpeed());
    }

/*

    public static String appendModID(String value) {
        return Tags.MOD_ID + ":" + value;
    }*/
}
