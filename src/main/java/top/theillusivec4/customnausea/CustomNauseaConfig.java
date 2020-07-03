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

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import org.apache.commons.lang3.tuple.Pair;

public class CustomNauseaConfig {

  static final ForgeConfigSpec CONFIG_SPEC;
  static final Config CONFIG;
  private static final String CONFIG_PREFIX = "gui." + CustomNausea.MODID + ".config.";

  static {
    final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder()
        .configure(Config::new);
    CONFIG_SPEC = specPair.getRight();
    CONFIG = specPair.getLeft();
  }

  public static double nauseaModifier;
  public static double portalModifier;
  public static Stumbling stumbling;

  public static void bake() {
    nauseaModifier = CONFIG.nauseaModifier.get();
    portalModifier = CONFIG.portalModifier.get();
    stumbling = CONFIG.stumbling.get();
  }

  public static class Config {

    public final DoubleValue nauseaModifier;
    public final DoubleValue portalModifier;
    public final EnumValue<Stumbling> stumbling;

    public Config(ForgeConfigSpec.Builder builder) {
      nauseaModifier = builder.comment("A multiplier for the strength of the nausea effect")
          .translation(CONFIG_PREFIX + "nauseaModifier")
          .defineInRange("nauseaModifier", 1.0d, 0.0d, 10.0d);

      portalModifier = builder
          .comment("A multiplier for the strength of the portal distortion effect")
          .translation(CONFIG_PREFIX + "portalModifier")
          .defineInRange("portalModifier", 1.0d, 0.0d, 10.0d);

      stumbling = builder.comment(
          "NORMAL - Players stumble when moving\nADVANCED - Players stumble even when standing still")
          .translation(CONFIG_PREFIX + "stumbling").defineEnum("stumbling", Stumbling.DISABLED);
    }
  }

  enum Stumbling {
    DISABLED, NORMAL, ADVANCED
  }
}
