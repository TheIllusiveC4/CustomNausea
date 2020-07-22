package top.theillusivec4.customnausea.impl.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.customnausea.CustomNauseaHooks;

@Mixin(InGameHud.class)
public class InGameHudMixin {

  @Shadow
  int scaledHeight;

  @Shadow
  int scaledWidth;

  @Inject(at = @At("HEAD"), method = "render")
  public void render(MatrixStack matrices, float delta, CallbackInfo cb) {
    CustomNauseaHooks.delta = delta;
  }

  @Inject(at = @At("HEAD"), method = "renderPortalOverlay", cancellable = true)
  public void renderPortalOverlay(float timeInPortal, CallbackInfo cb) {

    if (!CustomNauseaHooks.renderModifiedPortalOverlay(scaledWidth, scaledHeight)) {
      cb.cancel();
    }
  }
}
