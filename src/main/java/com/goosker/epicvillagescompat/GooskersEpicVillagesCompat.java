package com.goosker.epicvillagescompat;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;

/** Compatibility layer for Epic Structures: Villages + Luki's Grand Capitals. */
public final class GooskersEpicVillagesCompat implements ModInitializer {
    public static final String MOD_ID = "gooskers_epic_villages_compat";

    private static final String EPIC_PLAINS_PACK = "epic_plains";
    private static final String EPIC_SNOWY_PACK = "epic_snowy";
    private static final String EPIC_TAIGA_PACK = "epic_taiga";

    private static final String MIXED_PLAINS_PACK = "mixed_plains";
    private static final String MIXED_PLAINS_NO_CENTER_PACK = "mixed_plains_no_center";
    private static final String MIXED_SNOWY_PACK = "mixed_snowy";
    private static final String MIXED_TAIGA_PACK = "mixed_taiga";

    private static final String NO_CENTERS_PLAINS_PACK = "no_luki_center_plains";
    private static final String NO_CENTERS_DESERT_SAVANNA_PACK = "no_luki_centers_desert_savanna";

    @Override
    public void onInitialize() {
        ModContainer container = FabricLoader.getInstance()
                .getModContainer(MOD_ID)
                .orElseThrow(() -> new IllegalStateException("Could not find mod container for " + MOD_ID));

        CompatConfig.Settings settings = CompatConfig.load();

        registerPlains(container, settings);
        registerSimpleMode(container, settings.snowyMode(), EPIC_SNOWY_PACK, MIXED_SNOWY_PACK);
        registerSimpleMode(container, settings.taigaMode(), EPIC_TAIGA_PACK, MIXED_TAIGA_PACK);

        if (settings.noCenterStructureGen()) {
            // Desert + Savanna are always Luki because Epic does not provide those styles.
            registerRequiredPack(container, NO_CENTERS_DESERT_SAVANNA_PACK);
        }

        System.out.println(
                "[Goosker's Epic Villages Compat] Plains=" + settings.plainsMode().configValue()
                        + ", Snowy=" + settings.snowyMode().configValue()
                        + ", Taiga=" + settings.taigaMode().configValue()
                        + "; Luki No Center Structure Gen=" + settings.noCenterStructureGen() + "."
        );
    }

    private static void registerPlains(ModContainer container, CompatConfig.Settings settings) {
        switch (settings.plainsMode()) {
            case EPIC -> registerRequiredPack(container, EPIC_PLAINS_PACK);
            case MIXED -> registerRequiredPack(
                    container,
                    settings.noCenterStructureGen() ? MIXED_PLAINS_NO_CENTER_PACK : MIXED_PLAINS_PACK
            );
            case LUKI -> {
                if (settings.noCenterStructureGen()) {
                    registerRequiredPack(container, NO_CENTERS_PLAINS_PACK);
                }
                // Otherwise do nothing: Luki's own minecraft:village_plains stays active.
            }
        }
    }

    private static void registerSimpleMode(
            ModContainer container,
            CompatConfig.VillageMode mode,
            String epicPack,
            String mixedPack
    ) {
        switch (mode) {
            case EPIC -> registerRequiredPack(container, epicPack);
            case MIXED -> registerRequiredPack(container, mixedPack);
            case LUKI -> {
                // Do nothing: Luki's own minecraft:village_* definition stays active.
            }
        }
    }

    private static void registerRequiredPack(ModContainer container, String packPath) {
        boolean registered = ResourceManagerHelper.registerBuiltinResourcePack(
                new Identifier(MOD_ID, packPath),
                container,
                ResourcePackActivationType.ALWAYS_ENABLED
        );

        if (!registered) {
            throw new IllegalStateException("Failed to register built-in village override data pack: " + packPath);
        }
    }
}
