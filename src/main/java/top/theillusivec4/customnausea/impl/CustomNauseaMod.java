package top.theillusivec4.customnausea.impl;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CustomNauseaMod implements ClientModInitializer {

  public static final String MODID = "customnausea";
  public static final Logger LOGGER = LogManager.getLogger();

  @Override
  public void onInitializeClient() {
    ConfigHandler.init();
  }
}
