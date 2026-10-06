package com.levviata.lspecial.proxy;

import com.levviata.lspecial.StatsLogic;
import com.levviata.lspecial.Tags;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public class CommonProxy { // server and client

    public void preInit(FMLPreInitializationEvent event) {

    }

    public void postInit(FMLPostInitializationEvent event) {
        StatsLogic.init();
    }

    public void serverStarting(FMLServerStartingEvent event) {

    }
}
