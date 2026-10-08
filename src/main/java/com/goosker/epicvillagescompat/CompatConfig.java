package com.goosker.epicvillagescompat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Small properties config. Restart Minecraft after changing values. */
final class CompatConfig {
    static final Path PATH = Path.of("config", "gooskers_epic_villages_compat.properties");

    private static final String EPIC_PLAINS = "epic_plains";
    private static final String EPIC_SNOWY = "epic_snowy";
    private static final String EPIC_TAIGA = "epic_taiga";
    private static final String NO_CENTERS = "no_center_structure_gen";

    private CompatConfig() {}

    static Settings load() {
        Map<String, String> values = new LinkedHashMap<>();
        // New-install defaults: all Epic village types enabled.
        values.put(EPIC_PLAINS, "true");
        values.put(EPIC_SNOWY, "true");
        values.put(EPIC_TAIGA, "true");
        values.put(NO_CENTERS, "false");

        if (Files.isRegularFile(PATH)) {
            try {
                for (String rawLine : Files.readAllLines(PATH, StandardCharsets.UTF_8)) {
                    String line = rawLine.trim();
                    if (line.isEmpty() || line.startsWith("#") || line.startsWith("!")) {
                        continue;
                    }

                    int equals = line.indexOf('=');
                    if (equals < 0) {
                        continue;
                    }

                    String key = line.substring(0, equals).trim();
                    if (!values.containsKey(key)) {
                        continue;
                    }

                    String rawValue = line.substring(equals + 1).trim();
                    values.put(key, rawValue);
                }
            } catch (IOException e) {
                System.err.println("[Goosker's Epic Villages Compat] Could not read config; using defaults where needed: " + e.getMessage());
            }
        }

        Settings settings = new Settings(
                VillageMode.parse(values.get(EPIC_PLAINS), VillageMode.EPIC),
                VillageMode.parse(values.get(EPIC_SNOWY), VillageMode.EPIC),
                VillageMode.parse(values.get(EPIC_TAIGA), VillageMode.EPIC),
                parseBoolean(values.get(NO_CENTERS), false)
        );
        writeCanonical(settings);
        return settings;
    }

    private static boolean parseBoolean(String raw, boolean fallback) {
        if (raw == null) {
            return fallback;
        }
        if ("true".equalsIgnoreCase(raw.trim())) {
            return true;
        }
        if ("false".equalsIgnoreCase(raw.trim())) {
            return false;
        }
        return fallback;
    }

    private static void writeCanonical(Settings settings) {
        try {
            Files.createDirectories(PATH.getParent());
            List<String> lines = List.of(
                    "# Village values: true = Enable generation | false = Disable generation | 50_50 = equal chance per each village",
                    "",
                    EPIC_PLAINS + "=" + settings.plainsMode().configValue(),
                    EPIC_SNOWY + "=" + settings.snowyMode().configValue(),
                    EPIC_TAIGA + "=" + settings.taigaMode().configValue(),
                    "",
                    "# [Bonus Feature] Luki's Grand Capitals",
                    NO_CENTERS + "=" + settings.noCenterStructureGen(),
                    ""
            );
            Files.write(
                    PATH,
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            );
        } catch (IOException e) {
            System.err.println("[Goosker's Epic Villages Compat] Could not write config: " + e.getMessage());
        }
    }

    enum VillageMode {
        LUKI("false"),
        EPIC("true"),
        MIXED("50_50");

        private final String configValue;

        VillageMode(String configValue) {
            this.configValue = configValue;
        }

        String configValue() {
            return configValue;
        }

        static VillageMode parse(String raw, VillageMode fallback) {
            if (raw == null) {
                return fallback;
            }

            return switch (raw.trim().toLowerCase(Locale.ROOT)) {
                case "true", "epic", "100", "100%" -> EPIC;
                case "false", "luki", "0", "0%" -> LUKI;
                case "50_50", "50/50", "50", "50%", "mixed", "mix" -> MIXED;
                default -> fallback;
            };
        }
    }

    record Settings(
            VillageMode plainsMode,
            VillageMode snowyMode,
            VillageMode taigaMode,
            boolean noCenterStructureGen
    ) {}
}
