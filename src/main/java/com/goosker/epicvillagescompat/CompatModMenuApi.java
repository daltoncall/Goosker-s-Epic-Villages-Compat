package com.goosker.epicvillagescompat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

/** Optional Mod Menu integration. Mod Menu itself is not required on a dedicated server. */
public final class CompatModMenuApi implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return CompatConfigScreen::new;
    }

    private static final class CompatConfigScreen extends Screen {
        private final Screen parent;
        private CompatConfig.Settings settings;

        CompatConfigScreen(Screen parent) {
            super(Text.literal("Goosker's Epic Villages Compat"));
            this.parent = parent;
            this.settings = CompatConfig.load();
        }

        @Override
        protected void init() {
            int x = width / 2 - 100;
            addModeButton("Plains", 36, () -> settings.plainsMode(), value -> settings = settings.withPlainsMode(value));
            addModeButton("Desert", 60, () -> settings.desertMode(), value -> settings = settings.withDesertMode(value));
            addModeButton("Savanna", 84, () -> settings.savannaMode(), value -> settings = settings.withSavannaMode(value));
            addModeButton("Snowy", 108, () -> settings.snowyMode(), value -> settings = settings.withSnowyMode(value));
            addModeButton("Taiga", 132, () -> settings.taigaMode(), value -> settings = settings.withTaigaMode(value));

            addDrawableChild(ButtonWidget.builder(centerButtonText(), button -> {
                        settings = settings.withNoCenterStructureGen(!settings.noCenterStructureGen());
                        CompatConfig.save(settings);
                        button.setMessage(centerButtonText());
                    })
                    .dimensions(width / 2 - 150, 164, 300, 20)
                    .build());

            addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> close())
                    .dimensions(x, height - 28, 200, 20)
                    .build());
        }

        private void addModeButton(String label, int y, Supplier<CompatConfig.VillageMode> getter,
                                   Consumer<CompatConfig.VillageMode> setter) {
            addDrawableChild(ButtonWidget.builder(modeButtonText(label, getter.get()), button -> {
                        CompatConfig.VillageMode next = getter.get().next();
                        setter.accept(next);
                        CompatConfig.save(settings);
                        button.setMessage(modeButtonText(label, next));
                    })
                    .dimensions(width / 2 - 100, y, 200, 20)
                    .build());
        }

        private static Text modeButtonText(String label, CompatConfig.VillageMode mode) {
            return Text.literal(label + ": " + mode.displayName());
        }

        private Text centerButtonText() {
            return Text.literal("Remove Luki centers (Plains/Desert/Savanna): "
                    + (settings.noCenterStructureGen() ? "On" : "Off"));
        }

        @Override
        public void close() {
            CompatConfig.save(settings);
            if (client != null) client.setScreen(parent);
        }

        @Override
        public void render(DrawContext context, int mouseX, int mouseY, float delta) {
            renderBackground(context);
            context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 12, 0xFFFFFF);
            context.drawCenteredTextWithShadow(textRenderer,
                    Text.literal("Epic / 50-50 / Luki — restart Minecraft after changes"),
                    width / 2, 192, 0xA0A0A0);
            super.render(context, mouseX, mouseY, delta);
        }
    }
}
