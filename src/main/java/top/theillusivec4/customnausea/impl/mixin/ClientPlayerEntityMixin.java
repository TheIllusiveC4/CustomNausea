package top.theillusivec4.customnausea.impl.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.customnausea.CustomNauseaHooks;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

  @SuppressWarnings("ConstantConditions")
  @Inject(at = @At(value = "INVOKE", target = "net/minecraft/client/tutorial/TutorialManager.onMovement (Lnet/minecraft/client/input/Input;)V"), method = "tickMovement")
  public void onInputTick(CallbackInfo cb) {
    CustomNauseaHooks.onInputTick((ClientPlayerEntity) (Object) this);
  }
}
