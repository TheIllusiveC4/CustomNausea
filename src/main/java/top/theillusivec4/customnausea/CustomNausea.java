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

package top.theillusivec4.customnausea;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;


@Mod(CustomNausea.MODID)
public class CustomNausea {

  public static final String MODID = "customnausea";

  public CustomNausea() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::setupClient);
    eventBus.addListener(this::config);
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CustomNauseaConfig.CONFIG_SPEC);
    ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST,
        () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
  }

  private void setupClient(final FMLClientSetupEvent evt) {
    MinecraftForge.EVENT_BUS.register(new CustomNauseaEventHandler());
  }

  private void config(final ModConfigEvent evt) {

    if (evt.getConfig().getModId().equals(MODID)) {
      CustomNauseaConfig.bake();
    }
  }
}
