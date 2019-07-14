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

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class CustomNauseaConfig {

  public static final ForgeConfigSpec CONFIG_SPEC;

  static final DoubleValue  NAUSEA_MODIFIER;
  static final DoubleValue  PORTAL_MODIFIER;
  static final BooleanValue STUMBLING;

  private static final String CONFIG_PREFIX =
          "gui." + CustomNausea.MODID + ".config.";

  static {
    Builder builder = new Builder();

    {
      builder.push("Nausea");

      NAUSEA_MODIFIER = builder.comment("The strength of the Nausea effect")
                               .translation(CONFIG_PREFIX + "nauseaModifier")
                               .defineInRange("nauseaModifier", 1.0d, 0.0d,
                                              10.0d);
      STUMBLING = builder.comment(
              "Set to true to activate stumbling movement when nauseous")
                         .translation(CONFIG_PREFIX + "stumbling").define(
                      "stumbling", false);

      builder.pop();
    }

    {
      builder.push("Portal");

      PORTAL_MODIFIER = builder.comment(
              "The strength of the portal distortion effect").translation(
              CONFIG_PREFIX + "portalModifier").defineInRange("portalModifier",
                                                              1.0d, 0.0d,
                                                              10.0d);

      builder.pop();
    }

    CONFIG_SPEC = builder.build();
  }
}
