package c4.customnausea.core;

import c4.customnausea.CustomNausea;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.*;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = CustomNausea.MODID)
public class ConfigHandler {

    @Name("Nausea")
    @Comment("Configure nausea settings")
    public static final Nausea nausea = new Nausea();

    @Name("Portal")
    @Comment("Configure portal settings")
    public static final Portal portal = new Portal();

    public static class Nausea {

        @Name("Modifier")
        @Comment("The strength of the Nausea effect")
        @RangeDouble(min = 0.0D)
        public double modifier = 1.0D;

        @Name("Stumbling Movement")
        @Comment("Set to true to activate stumbling movement when nauseous")
        public boolean stumbling = false;
    }

    public static class Portal {

        @Name("Modifier")
        @Comment("The strength of the portal distortion effect")
        @RangeDouble(min = 0.0D)
        public double modifier = 1.0D;
    }

    @Mod.EventBusSubscriber(modid = CustomNausea.MODID)
    private static class ConfigEventHandler {

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent evt) {
            if (evt.getModID().equals(CustomNausea.MODID)) {
                ConfigManager.sync(CustomNausea.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
