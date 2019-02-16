/*
 * Copyright (C) 2018-2019  C4
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

package c4.customnausea.core;

import c4.customnausea.CustomNausea;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;

public class NauseaConfig {

    static final DoubleValue nauseaModifier;
    static final BooleanValue stumbling;

    static final DoubleValue portalModifier;

    public static final ForgeConfigSpec spec;

    private static final String CONFIG_PREFIX = "gui." + CustomNausea.MODID + ".config.";

    static {
        Builder builder = new Builder();

        {
            builder.push("Nausea");

            nauseaModifier = builder
                    .comment("The strength of the Nausea effect")
                    .translation(CONFIG_PREFIX + "nauseaModifier")
                    .defineInRange("nauseaModifier", 1.0d, 0.0d, 10.0d);

            stumbling = builder
                    .comment("Set to true to activate stumbling movement when nauseous")
                    .translation(CONFIG_PREFIX + "stumbling")
                    .define("stumbling", false);

            builder.pop();
        }

        {
            builder.push("Portal");

            portalModifier = builder
                    .comment("The strength of the portal distortion effect")
                    .translation(CONFIG_PREFIX + "portalModifier")
                    .defineInRange("portalModifier", 1.0d, 0.0d, 10.0d);

            builder.pop();
        }

        spec = builder.build();
    }
}
