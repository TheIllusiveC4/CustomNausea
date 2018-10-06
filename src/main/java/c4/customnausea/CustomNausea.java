package c4.customnausea;

import c4.customnausea.core.EventHandlerClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

@Mod(   modid = CustomNausea.MODID,
        name = CustomNausea.NAME,
        version = "@VERSION@",
        dependencies = "required-after:forge@[14.23.5.2768,)",
        acceptedMinecraftVersions = "[1.12, 1.13)",
        certificateFingerprint = "@FINGERPRINT@",
        clientSideOnly = true)
public class CustomNausea
{
    public static final String MODID = "customnausea";
    public static final String NAME = "Custom Nausea";

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt)
    {
        logger = evt.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent evt) {
        MinecraftForge.EVENT_BUS.register(new EventHandlerClient());
    }

    @EventHandler
    public void onFingerPrintViolation(FMLFingerprintViolationEvent evt) {
        FMLLog.log.log(Level.ERROR, "Invalid fingerprint detected! The file " + evt.getSource().getName()
                + " may have been tampered with. This version will NOT be supported by the author!");
    }
}
