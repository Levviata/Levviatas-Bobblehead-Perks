package com.levviata.lplayeratt;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class LeviathanPlayerAttributes {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    private static Map<String, PlayerAttributeValues> configMap;

    public static Map<String, PlayerAttributeValues> getAttributeMap() {
        return configMap;
    }

    private File attributeConfig;
    /**
     * <a href="https://cleanroommc.com/wiki/forge-mod-development/event#overview">
     *     Take a look at how many FMLStateEvents you can listen to via the @Mod.EventHandler annotation here
     * </a>
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
       MinecraftForge.EVENT_BUS.register(new PlayerUseBobblehead());

        try {
            Gson attributeGson = (new GsonBuilder()).setLenient().setPrettyPrinting().registerTypeAdapter(PlayerAttributeValues.class, new PlayerAttValuesSerializer()).create();
            //Gson baubleAttGson = (new GsonBuilder()).setLenient().setPrettyPrinting().registerTypeAdapter(AttributeValues.class, new BaubleAttSerializer()).create();

            this.attributeConfig = new File("config/attributeModifiers.json");
            //this.baubleAttConfig = new File("config/attributeModifiers/baubleAttModifiers.json");

            if (!this.attributeConfig.exists()) { // make config and examples
                this.attributeConfig.createNewFile();
                configMap = new HashMap<>();
                configMap.put("minecraft:diamond_hoe", new PlayerAttributeValues(
                        10,
                        60,
                        2,
                        0.7F,
                        0.15F,
                        10.0F,
                        2.0F,
                        2.0F,
                        1.0F,
                        5.0F,
                        50052,
                        4,
                        0,
                        0,
                        0
                ));

                FileUtils.writeStringToFile(this.attributeConfig, attributeGson.toJson(configMap), StandardCharsets.UTF_8);
            } else { // read and write as normal
                Type mapType = (new TypeToken<HashMap<String, PlayerAttributeValues>>() {}).getType();

                configMap = attributeGson.fromJson(FileUtils.readFileToString(this.attributeConfig, StandardCharsets.UTF_8), mapType);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
