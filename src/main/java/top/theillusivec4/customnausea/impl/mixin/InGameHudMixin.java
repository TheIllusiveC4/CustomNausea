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
