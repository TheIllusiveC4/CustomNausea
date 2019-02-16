/*
 * Copyright (C) 2018-2019  C4
 *
 * This file is part of Custom Nausea, a mod made for Minecraft.
 *
 * Custom Nausea is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Custom Nausea is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Custom Nausea.  If not, see <https://www.gnu.org/licenses/>.
 */

package c4.customnausea.core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;

public class EventHandler {

    private static float portalCounter = 0.0f;
    private static float prevPortalCounter = 0.0f;
    private static int stumbleCooldown = 0;
    private static int stumbleStrafe = 0;
    private static int stumbleForward = 0;

    private static final Random RAND = new Random();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent evt) {

        if (evt.phase == TickEvent.Phase.END) {
            EntityPlayerSP sp = Minecraft.getInstance().player;

            if (sp != null) {
                boolean inPortal = ObfuscationReflectionHelper.getPrivateValue(Entity.class, sp, "field_71087_bX");
                double max = inPortal ? NauseaConfig.portalModifier.get() : NauseaConfig.nauseaModifier.get();
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

        if (NauseaConfig.stumbling.get() && player.getActivePotionEffect(MobEffects.NAUSEA) != null) {

            if (stumbleCooldown < 10) {
                stumbleCooldown++;
            } else {
                stumbleCooldown = 0;
                stumbleStrafe = RAND.nextInt(3) - 1;
                stumbleForward = RAND.nextInt(3) - 1;
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

        if (evt.getType() == RenderGameOverlayEvent.ElementType.PORTAL && NauseaConfig.portalModifier.get() != 1.0D) {
            evt.setCanceled(true);
            float f1 = prevPortalCounter + (portalCounter - prevPortalCounter) * evt.getPartialTicks();

            if (f1 > 0.0F) {
                renderPortal(f1);
            }
        }
    }

    private static void renderPortal(float timeInPortal) {

        if (timeInPortal < 1.0F) {
            timeInPortal = timeInPortal * timeInPortal;
            timeInPortal = timeInPortal * timeInPortal;
            timeInPortal = timeInPortal * 0.8F + 0.2F;
        }
        Minecraft mc = Minecraft.getInstance();
        double scaledHeight = mc.mainWindow.getScaledHeight();
        double scaledWidth = mc.mainWindow.getScaledWidth();
        GlStateManager.disableAlphaTest();
        GlStateManager.disableDepthTest();
        GlStateManager.depthMask(false);
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, timeInPortal);
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite textureatlassprite = mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.NETHER_PORTAL.getDefaultState());
        float f = textureatlassprite.getMinU();
        float f1 = textureatlassprite.getMinV();
        float f2 = textureatlassprite.getMaxU();
        float f3 = textureatlassprite.getMaxV();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(0.0D, scaledHeight, -90.0D).tex((double)f, (double)f3).endVertex();
        bufferbuilder.pos(scaledWidth, scaledHeight, -90.0D).tex((double)f2, (double)f3).endVertex();
        bufferbuilder.pos(scaledWidth, 0.0D, -90.0D).tex((double)f2, (double)f1).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex((double)f, (double)f1).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepthTest();
        GlStateManager.enableAlphaTest();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
