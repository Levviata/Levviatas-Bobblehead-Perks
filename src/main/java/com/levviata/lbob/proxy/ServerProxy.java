package com.levviata.lbob.proxy;

import com.levviata.lbob.CommandSetSpeed;
import com.levviata.lbob.Tags;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID, value = Side.SERVER)
public class ServerProxy extends CommonProxy { // server only
    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandSetSpeed());
    }
}
