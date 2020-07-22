package top.theillusivec4.customnausea;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Random;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import top.theillusivec4.customnausea.config.CustomNauseaConfig;
import top.theillusivec4.customnausea.config.CustomNauseaConfig.Stumbling;
import top.theillusivec4.customnausea.impl.mixin.EntityAccessor;

public class CustomNauseaHooks {

  private static final Random RANDOM = new Random();

  public static float delta = 0.0F;

  private static float portalCounter = 0.0F;
  private static float prevPortalCounter = 0.0F;
  private static int stumbleCooldown = 0;
  private static int stumbleStrafe = 0;
  private static int stumbleForward = 0;

  @SuppressWarnings("deprecation")
  public static boolean renderModifiedPortalOverlay(int scaledWidth, int scaledHeight) {

    if (CustomNauseaConfig.getPortalModifier() == 1.0D) {
      return true;
    }
    float timeInPortal = MathHelper.lerp(delta, prevPortalCounter, portalCounter);
    MinecraftClient client = MinecraftClient.getInstance();

    if (timeInPortal < 1.0F) {
      timeInPortal *= timeInPortal;
      timeInPortal *= timeInPortal;
      timeInPortal = timeInPortal * 0.8F + 0.2F;
    }

    RenderSystem.disableAlphaTest();
    RenderSystem.disableDepthTest();
    RenderSystem.depthMask(false);
    RenderSystem.defaultBlendFunc();
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, timeInPortal);
    client.getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
    Sprite sprite = client.getBlockRenderManager().getModels()
        .getSprite(Blocks.NETHER_PORTAL.getDefaultState());
    float g = sprite.getMinU();
    float h = sprite.getMinV();
    float i = sprite.getMaxU();
    float j = sprite.getMaxV();
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferBuilder = tessellator.getBuffer();
    bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE);
    bufferBuilder.vertex(0.0D, scaledHeight, -90.0D).texture(g, j).next();
    bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0D).texture(i, j).next();
    bufferBuilder.vertex(scaledWidth, 0.0D, -90.0D).texture(i, h).next();
    bufferBuilder.vertex(0.0D, 0.0D, -90.0D).texture(g, h).next();
    tessellator.draw();
    RenderSystem.depthMask(true);
    RenderSystem.enableDepthTest();
    RenderSystem.enableAlphaTest();
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    return false;
  }

  public static void onTick(ClientPlayerEntity playerEntity) {

    if (playerEntity != null) {
      boolean inPortal = ((EntityAccessor) playerEntity).getInNetherPortal();
      double maxEffectTime = inPortal ? CustomNauseaConfig.getPortalModifier()
          : CustomNauseaConfig.getNauseaModifier();
      playerEntity.nextNauseaStrength = (float) Math
          .min(playerEntity.nextNauseaStrength, maxEffectTime);
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

  public static void onInputTick(ClientPlayerEntity playerEntity) {
    Input input = playerEntity.input;

    if (CustomNauseaConfig.getStumbling() == Stumbling.DISABLED || !playerEntity
        .hasStatusEffect(StatusEffects.NAUSEA)) {
      return;
    }

    if (stumbleCooldown < 10) {
      stumbleCooldown++;
    } else {
      stumbleCooldown = 0;
      stumbleStrafe = RANDOM.nextInt(3) - 1;
      stumbleForward = RANDOM.nextInt(3) - 1;
    }
    boolean alwaysStumbling = CustomNauseaConfig.getStumbling() == Stumbling.ADVANCED;

    if (alwaysStumbling || input.pressingLeft || input.pressingRight) {
      input.movementForward += stumbleForward;
    }

    if (alwaysStumbling || input.pressingForward || input.pressingBack) {
      input.movementSideways += stumbleStrafe;
    }
  }
}
