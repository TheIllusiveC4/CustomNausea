package top.theillusivec4.customnausea.impl.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.customnausea.CustomNauseaHooks;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

  @Shadow
  ClientPlayerEntity player;

  @Inject(at = @At("TAIL"), method = "tick")
  public void tick(CallbackInfo cb) {
    CustomNauseaHooks.onTick(player);
  }
}
