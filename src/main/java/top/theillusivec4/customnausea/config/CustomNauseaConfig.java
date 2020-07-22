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

package top.theillusivec4.customnausea.config;

public class CustomNauseaConfig {

  private static double nauseaModifier;
  private static double portalModifier;
  private static Stumbling stumbling;

  public static double getNauseaModifier() {
    return nauseaModifier;
  }

  public static void setNauseaModifier(double nauseaModifier) {
    CustomNauseaConfig.nauseaModifier = nauseaModifier;
  }

  public static double getPortalModifier() {
    return portalModifier;
  }

  public static void setPortalModifier(double portalModifier) {
    CustomNauseaConfig.portalModifier = portalModifier;
  }

  public static Stumbling getStumbling() {
    return stumbling;
  }

  public static void setStumbling(Stumbling stumbling) {
    CustomNauseaConfig.stumbling = stumbling;
  }

  public enum Stumbling {
    DISABLED, NORMAL, ADVANCED
  }
}
