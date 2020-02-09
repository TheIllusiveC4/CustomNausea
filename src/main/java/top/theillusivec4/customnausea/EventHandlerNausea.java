/*
 * Copyright (C) 2018-2019 C4
 *
 * This file is part of Custom Nausea, a mod made for Minecraft.
 *
 * Custom Nausea is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Custom Nausea is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Custom Nausea.  If not, see
 * <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.customnausea;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Random;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class EventHandlerNausea {

  private static final Random RAND = new Random();

  private static float portalCounter = 0.0f;
  private static float prevPortalCounter = 0.0f;
  private static int stumbleCooldown = 0;
  private static int stumbleStrafe = 0;
  private static int stumbleForward = 0;

  @SuppressWarnings("deprecation")
  private static void renderPortal(float timeInPortal) {

    if (timeInPortal < 1.0F) {
      timeInPortal = timeInPortal * timeInPortal;
      timeInPortal = timeInPortal * timeInPortal;
      timeInPortal = timeInPortal * 0.8F + 0.2F;
    }

    Minecraft mc = Minecraft.getInstance();
    double scaledHeight = mc.getMainWindow().getScaledHeight();
    double scaledWidth = mc.getMainWindow().getScaledWidth();

    RenderSystem.disableAlphaTest();
    RenderSystem.disableDepthTest();
    RenderSystem.depthMask(false);
    RenderSystem.defaultBlendFunc();
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, timeInPortal);
    mc.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
    TextureAtlasSprite textureatlassprite = mc.getBlockRendererDispatcher().getBlockModelShapes()
        .getTexture(Blocks.NETHER_PORTAL.getDefaultState());
    float f = textureatlassprite.getMinU();
    float f1 = textureatlassprite.getMinV();
    float f2 = textureatlassprite.getMaxU();
    float f3 = textureatlassprite.getMaxV();
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
    bufferbuilder.pos(0.0D, scaledHeight, -90.0D).tex(f, f3).endVertex();
    bufferbuilder.pos(scaledWidth, scaledHeight, -90.0D).tex(f2, f3).endVertex();
    bufferbuilder.pos(scaledWidth, 0.0D, -90.0D).tex(f2, f1).endVertex();
    bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(f, f1).endVertex();
    tessellator.draw();
    RenderSystem.depthMask(true);
    RenderSystem.enableDepthTest();
    RenderSystem.enableAlphaTest();
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
  }

  @SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent evt) {
    ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;

    if (evt.phase != TickEvent.Phase.END || clientPlayer == null) {
      return;
    }

    Boolean inPortal = ObfuscationReflectionHelper
        .getPrivateValue(Entity.class, clientPlayer, "field_71087_bX");

    if (inPortal == null) {
      return;
    }

    double maxEffectTime = inPortal ? CustomNauseaConfig.PORTAL_MODIFIER.get()
        : CustomNauseaConfig.NAUSEA_MODIFIER.get();
    clientPlayer.timeInPortal = (float) Math.min(clientPlayer.timeInPortal, maxEffectTime);
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

  @SubscribeEvent
  public void onMovementInput(InputUpdateEvent evt) {
    MovementInput input = evt.getMovementInput();
    PlayerEntity player = evt.getPlayer();

    if (!CustomNauseaConfig.STUMBLING.get()
        || player.getActivePotionEffect(Effects.NAUSEA) == null) {
      return;
    }

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

  @SubscribeEvent
  public void onPortalRender(RenderGameOverlayEvent.Pre evt) {

    if (evt.getType() != RenderGameOverlayEvent.ElementType.PORTAL
        || CustomNauseaConfig.PORTAL_MODIFIER.get() == 1.0d) {
      return;
    }

    evt.setCanceled(true);
    float timeInPortal =
        prevPortalCounter + (portalCounter - prevPortalCounter) * evt.getPartialTicks();

    if (timeInPortal > 0.0F) {
      renderPortal(timeInPortal);
    }
  }
}
