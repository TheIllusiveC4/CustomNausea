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

package top.theillusivec4.customnausea.impl;

import io.github.fablabsmc.fablabs.api.fiber.v1.builder.ConfigTreeBuilder;
import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import top.theillusivec4.customnausea.config.CustomNauseaConfig;
import top.theillusivec4.customnausea.config.CustomNauseaConfig.Stumbling;

public class ConfigHandler {

  static final PropertyMirror<Double> NAUSEA_MODIFIER = PropertyMirror.create(ConfigTypes.DOUBLE);
  static final PropertyMirror<Double> PORTAL_MODIFIER = PropertyMirror.create(ConfigTypes.DOUBLE);
  static final PropertyMirror<Stumbling> STUMBLING = PropertyMirror
      .create(ConfigTypes.makeEnum(Stumbling.class));

  private static final ConfigTree INSTANCE;

  static {
    ConfigTreeBuilder builder = ConfigTree.builder();

    builder.beginValue("nauseaModifier", ConfigTypes.DOUBLE, 1.0D)
        .withComment("A multiplier for the strength of the nausea effect")
        .finishValue(NAUSEA_MODIFIER::mirror);

    builder.beginValue("portalModifier", ConfigTypes.DOUBLE, 1.0D)
        .withComment("A multiplier for the strength of the portal distortion effect")
        .finishValue(PORTAL_MODIFIER::mirror);

    builder.beginValue("stumbling", ConfigTypes.makeEnum(Stumbling.class), Stumbling.DISABLED)
        .withComment("NORMAL - Players stumble when moving"
            + "\nADVANCED - Players stumble even when standing still")
        .finishValue(STUMBLING::mirror);

    INSTANCE = builder.build();
  }

  @SuppressWarnings("ResultOfMethodCallIgnored")
  public static void init() {
    JanksonValueSerializer serializer = new JanksonValueSerializer(false);
    Path config = Paths.get("config");

    if (!Files.exists(config)) {
      new File("config").mkdir();
    }
    Path path = Paths.get("config", CustomNauseaMod.MODID + ".json5");

    try (OutputStream stream = new BufferedOutputStream(
        Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW))) {
      FiberSerialization.serialize(INSTANCE, stream, serializer);
    } catch (FileAlreadyExistsException e) {
      CustomNauseaMod.LOGGER
          .debug(CustomNauseaMod.MODID + " config already exists, skipping new config creation...");
    } catch (IOException e) {
      CustomNauseaMod.LOGGER.error("Error serializing new config!");
      e.printStackTrace();
    }

    try (InputStream stream = new BufferedInputStream(
        Files.newInputStream(path, StandardOpenOption.READ, StandardOpenOption.CREATE))) {
      FiberSerialization.deserialize(INSTANCE, stream, serializer);
    } catch (IOException | ValueDeserializationException e) {
      CustomNauseaMod.LOGGER.error("Error deserializing config!");
      e.printStackTrace();
    }
    bake();
  }

  private static void bake() {
    CustomNauseaConfig.setNauseaModifier(NAUSEA_MODIFIER.getValue());
    CustomNauseaConfig.setPortalModifier(PORTAL_MODIFIER.getValue());
    CustomNauseaConfig.setStumbling(STUMBLING.getValue());
  }
}
