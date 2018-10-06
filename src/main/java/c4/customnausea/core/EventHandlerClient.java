package c4.customnausea.core;

import c4.customnausea.CustomNausea;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Level;

import java.lang.reflect.Field;
import java.util.Random;

public class EventHandlerClient {

    private static final Field IN_PORTAL = ReflectionHelper.findField(Entity.class, "inPortal", "field_71087_bX",
            "ak");

    private static float portalCounter = 0.0f;
    private static float prevPortalCounter = 0.0f;
    private static int stumbleCooldown = 0;
    private static int stumbleStrafe = 0;
    private static int stumbleForward = 0;

    private static Random rand = new Random();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent evt) {

        if (evt.phase == TickEvent.Phase.END) {
            EntityPlayerSP sp = Minecraft.getMinecraft().player;

            if (sp != null) {
                boolean inPortal = false;

                try {
                    inPortal = IN_PORTAL.getBoolean(sp);
                } catch (IllegalAccessException e) {
                    CustomNausea.logger.log(Level.ERROR, "Error accessing inPortal field of player " + sp.getName());
                }
                double max = inPortal ? ConfigHandler.portal.modifier : ConfigHandler.nausea.modifier;
                sp.timeInPortal = (float) Math.min(sp.timeInPortal, max);

                prevPortalCounter = portalCounter;

                if (inPortal) {
                    portalCounter += 0.0125F;

                    if (portalCounter >= 1.0F) {
                        portalCounter = 1.0F;
                    }
                } else {

                    if (portalCounter > 0.0F) {
                        portalCounter -= 0.05F;
                    }

                    if (portalCounter < 0.0F) {
                        portalCounter = 0.0F;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onMovementInput(InputUpdateEvent evt) {
        MovementInput input = evt.getMovementInput();
        EntityPlayer player = evt.getEntityPlayer();

        if (ConfigHandler.nausea.stumbling && player.getActivePotionEffect(MobEffects.NAUSEA) != null) {

            if (stumbleCooldown < 10) {
                stumbleCooldown++;
            } else {
                stumbleCooldown = 0;
                stumbleStrafe = rand.nextInt(3) - 1;
                stumbleForward = rand.nextInt(3) - 1;
            }

            if (input.leftKeyDown || input.rightKeyDown) {
                input.moveForward += stumbleForward;
            }

            if (input.forwardKeyDown || input.backKeyDown) {
                input.moveStrafe += stumbleStrafe;
            }
        }
    }

    @SubscribeEvent
    public void onPortalRender(RenderGameOverlayEvent.Pre evt) {

        if (evt.getType() == RenderGameOverlayEvent.ElementType.PORTAL && ConfigHandler.portal.modifier != 1.0D) {
            evt.setCanceled(true);
            float f1 = prevPortalCounter + (portalCounter - prevPortalCounter) * evt.getPartialTicks();

            if (f1 > 0.0F) {
                renderPortal(f1, evt.getResolution());
            }
        }
    }

    private static void renderPortal(float timeInPortal, ScaledResolution scaledRes) {

        if (timeInPortal < 1.0F) {
            timeInPortal = timeInPortal * timeInPortal;
            timeInPortal = timeInPortal * timeInPortal;
            timeInPortal = timeInPortal * 0.8F + 0.2F;
        }
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, timeInPortal);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite textureatlassprite = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.PORTAL.getDefaultState());
        float f = textureatlassprite.getMinU();
        float f1 = textureatlassprite.getMinV();
        float f2 = textureatlassprite.getMaxU();
        float f3 = textureatlassprite.getMaxV();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, (double)scaledRes.getScaledHeight(), -90.0D).tex((double)f, (double)f3).endVertex();
        bufferbuilder.pos((double)scaledRes.getScaledWidth(), (double)scaledRes.getScaledHeight(), -90.0D).tex((double)f2, (double)f3).endVertex();
        bufferbuilder.pos((double)scaledRes.getScaledWidth(), 0.0D, -90.0D).tex((double)f2, (double)f1).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex((double)f, (double)f1).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
