package com.levviata.lspecial.proxy;

import com.levviata.lspecial.Tags;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID, value = Side.SERVER)
public class ServerProxy extends CommonProxy { // server only

}
