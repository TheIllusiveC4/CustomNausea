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

  static final ForgeConfigSpec CONFIG_SPEC;
  static final DoubleValue     NAUSEA_MODIFIER;
  static final DoubleValue     PORTAL_MODIFIER;
  static final BooleanValue    STUMBLING;

  private static final String CONFIG_PREFIX =
          "gui." + CustomNausea.MODID + ".config.";

  static {
    Builder builder = new Builder();

    {
      builder.push(ConfigCategories.NAUSEA.name());

      NAUSEA_MODIFIER = builder.comment(ConfigProp.NAUSEA_MOD.comment)
                               .translation(ConfigProp.NAUSEA_MOD.translation)
                               .defineInRange(ConfigProp.NAUSEA_MOD.path, 1.0d,
                                              0.0d, 10.0d);
      STUMBLING = builder.comment(ConfigProp.STUMBLING.comment)
                         .translation(ConfigProp.STUMBLING.translation)
                         .define(ConfigProp.STUMBLING.path, false);

      builder.pop();
    }

    {
      builder.push(ConfigCategories.PORTAL.name());

      PORTAL_MODIFIER = builder.comment(ConfigProp.PORTAL_MOD.comment)
                               .translation(ConfigProp.PORTAL_MOD.translation)
                               .defineInRange(ConfigProp.PORTAL_MOD.path, 1.0d,
                                              0.0d, 10.0d);

      builder.pop();
    }

    CONFIG_SPEC = builder.build();
  }

  private enum ConfigCategories {
    NAUSEA,
    PORTAL
  }

  private enum ConfigProp {
    NAUSEA_MOD("nauseaModifier", "The strength of the Nausea effect"),
    STUMBLING("stumbling",
              "Set to true to active stumbling movement when nauseous"),
    PORTAL_MOD("portalModifier",
               "The strength of the portal distortion effect");

    final String path;
    final String translation;
    final String comment;

    ConfigProp(String path, String comment) {
      this.path = path;
      this.translation = CONFIG_PREFIX + path;
      this.comment = comment;
    }
  }
}
