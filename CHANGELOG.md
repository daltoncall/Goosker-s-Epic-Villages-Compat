# Changelog

## 1.2.0-alpha+1.20.1

- Updated compatibility for Epic Structures: Villages 2.0.0.
- Added independent Desert and Savanna configuration alongside Plains, Snowy, and Taiga.
- Added `true`, `false`, and `50_50` behavior for all five village families.
- Added whole-village 50/50 selection for Desert and Savanna without mixing Epic and Luki buildings inside one village.
- Preserved the existing 1.1.5 Plains/Snowy/Taiga mixed-generation resources.
- Preserved optional centerless Luki generation for Plains, Desert, and Savanna, including when Desert/Savanna are in 50/50 mode.
- Split Desert and Savanna centerless overrides so one style can no longer accidentally override the other family's Epic selection.
- Added optional Mod Menu 7.2.2 configuration UI.
- Existing 1.1.5 properties files migrate automatically; new Desert/Savanna keys default to Epic.

## 1.1.5-alpha

- Added `50_50` village selection for Plains, Snowy, and Taiga.
- Kept each generated village internally consistent between Epic and Luki.
- Added centerless handling for the Luki half of mixed Plains villages.
