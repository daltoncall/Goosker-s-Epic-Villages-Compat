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

/** Small properties config. Worldgen changes take effect after a Minecraft restart. */
final class CompatConfig {
    static final Path PATH = Path.of("config", "gooskers_epic_villages_compat.properties");

    private static final String EPIC_PLAINS = "epic_plains";
    private static final String EPIC_DESERT = "epic_desert";
    private static final String EPIC_SAVANNA = "epic_savanna";
    private static final String EPIC_SNOWY = "epic_snowy";
    private static final String EPIC_TAIGA = "epic_taiga";
    private static final String NO_CENTERS = "no_center_structure_gen";

    private CompatConfig() {}

    static Settings load() {
        Map<String, String> values = new LinkedHashMap<>();
        // New-install defaults: all five Epic 2.0 village types enabled.
        values.put(EPIC_PLAINS, "true");
        values.put(EPIC_DESERT, "true");
        values.put(EPIC_SAVANNA, "true");
        values.put(EPIC_SNOWY, "true");
        values.put(EPIC_TAIGA, "true");
        values.put(NO_CENTERS, "false");

        if (Files.isRegularFile(PATH)) {
            try {
                for (String rawLine : Files.readAllLines(PATH, StandardCharsets.UTF_8)) {
                    String line = rawLine.trim();
                    if (line.isEmpty() || line.startsWith("#") || line.startsWith("!")) continue;
                    int equals = line.indexOf('=');
                    if (equals < 0) continue;
                    String key = line.substring(0, equals).trim();
                    if (!values.containsKey(key)) continue;
                    values.put(key, line.substring(equals + 1).trim());
                }
            } catch (IOException e) {
                System.err.println("[Goosker's Epic Villages Compat] Could not read config; using defaults where needed: " + e.getMessage());
            }
        }

        Settings settings = new Settings(
                VillageMode.parse(values.get(EPIC_PLAINS), VillageMode.EPIC),
                VillageMode.parse(values.get(EPIC_DESERT), VillageMode.EPIC),
                VillageMode.parse(values.get(EPIC_SAVANNA), VillageMode.EPIC),
                VillageMode.parse(values.get(EPIC_SNOWY), VillageMode.EPIC),
                VillageMode.parse(values.get(EPIC_TAIGA), VillageMode.EPIC),
                parseBoolean(values.get(NO_CENTERS), false)
        );
        save(settings);
        return settings;
    }

    static void save(Settings settings) {
        try {
            Files.createDirectories(PATH.getParent());
            List<String> lines = List.of(
                    "# Village values: true = Epic | false = Luki | 50_50 = equal chance per newly generated village",
                    "# Epic Structures: Villages 2.0.0 provides all five vanilla village families.",
                    "",
                    EPIC_PLAINS + "=" + settings.plainsMode().configValue(),
                    EPIC_DESERT + "=" + settings.desertMode().configValue(),
                    EPIC_SAVANNA + "=" + settings.savannaMode().configValue(),
                    EPIC_SNOWY + "=" + settings.snowyMode().configValue(),
                    EPIC_TAIGA + "=" + settings.taigaMode().configValue(),
                    "",
                    "# [Bonus Feature] Luki's Grand Capitals",
                    "# Removes the decorative center from the Luki half of Plains, Desert, and Savanna villages.",
                    NO_CENTERS + "=" + settings.noCenterStructureGen(),
                    "",
                    "# Restart Minecraft after changing village generation settings."
            );
            Files.write(PATH, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        } catch (IOException e) {
            System.err.println("[Goosker's Epic Villages Compat] Could not write config: " + e.getMessage());
        }
    }

    private static boolean parseBoolean(String raw, boolean fallback) {
        if (raw == null) return fallback;
        if ("true".equalsIgnoreCase(raw.trim())) return true;
        if ("false".equalsIgnoreCase(raw.trim())) return false;
        return fallback;
    }

    enum VillageMode {
        LUKI("false", "Luki"),
        EPIC("true", "Epic"),
        MIXED("50_50", "50/50");

        private final String configValue;
        private final String displayName;

        VillageMode(String configValue, String displayName) {
            this.configValue = configValue;
            this.displayName = displayName;
        }

        String configValue() { return configValue; }
        String displayName() { return displayName; }

        VillageMode next() {
            return switch (this) {
                case EPIC -> MIXED;
                case MIXED -> LUKI;
                case LUKI -> EPIC;
            };
        }

        static VillageMode parse(String raw, VillageMode fallback) {
            if (raw == null) return fallback;
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
            VillageMode desertMode,
            VillageMode savannaMode,
            VillageMode snowyMode,
            VillageMode taigaMode,
            boolean noCenterStructureGen
    ) {
        Settings withPlainsMode(VillageMode value) { return new Settings(value, desertMode, savannaMode, snowyMode, taigaMode, noCenterStructureGen); }
        Settings withDesertMode(VillageMode value) { return new Settings(plainsMode, value, savannaMode, snowyMode, taigaMode, noCenterStructureGen); }
        Settings withSavannaMode(VillageMode value) { return new Settings(plainsMode, desertMode, value, snowyMode, taigaMode, noCenterStructureGen); }
        Settings withSnowyMode(VillageMode value) { return new Settings(plainsMode, desertMode, savannaMode, value, taigaMode, noCenterStructureGen); }
        Settings withTaigaMode(VillageMode value) { return new Settings(plainsMode, desertMode, savannaMode, snowyMode, value, noCenterStructureGen); }
        Settings withNoCenterStructureGen(boolean value) { return new Settings(plainsMode, desertMode, savannaMode, snowyMode, taigaMode, value); }
    }
}
