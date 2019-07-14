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

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


@Mod(CustomNausea.MODID)
public class CustomNausea {

  public static final String MODID = "customnausea";

  public CustomNausea() {
    FMLJavaModLoadingContext.get()
                            .getModEventBus()
                            .addListener(this::setupClient);
    ModLoadingContext.get()
                     .registerConfig(ModConfig.Type.CLIENT,
                                     CustomNauseaConfig.CONFIG_SPEC);
  }

  private void setupClient(final FMLClientSetupEvent evt) {
    MinecraftForge.EVENT_BUS.register(new EventHandlerNausea());
  }
}
