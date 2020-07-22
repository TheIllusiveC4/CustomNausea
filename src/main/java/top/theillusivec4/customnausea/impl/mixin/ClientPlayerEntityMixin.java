/*
 * Copyright (c) 2018-2020 C4
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
