# Goosker's Epic Villages Compat

Fabric 1.20.1 compatibility layer for **Epic Structures: Villages 2.0.0** and **Luki's Grand Capitals**.

Version: **1.2.0-alpha+1.20.1**

## What changed in 1.2.0

Epic Structures: Villages 2.0.0 now supplies all five vanilla village families, so this compat mod now exposes independent controls for:

- Plains
- Desert
- Savanna
- Snowy
- Taiga

Each village family can be set to:

- `true` = Epic Structures: Villages
- `false` = Luki's Grand Capitals
- `50_50` = equal Epic/Luki chance for each newly generated village

The existing optional `no_center_structure_gen` setting is preserved for the Luki half of Plains, Desert, and Savanna villages.

## Mod Menu

When Mod Menu 7.2.2 is installed, **Goosker's Epic Villages Compat** has a Configure button with controls for all five village families plus the Luki center-removal option.

Mod Menu is optional. Dedicated servers do not need it.

World-generation pack selection is established during startup, so restart Minecraft after changing these settings.

## Configuration

The file is generated at:

```text
config/gooskers_epic_villages_compat.properties
```

Default configuration:

```properties
# Village values: true = Epic | false = Luki | 50_50 = equal chance per newly generated village
# Epic Structures: Villages 2.0.0 provides all five vanilla village families.

epic_plains=true
epic_desert=true
epic_savanna=true
epic_snowy=true
epic_taiga=true

# [Bonus Feature] Luki's Grand Capitals
# Removes the decorative center from the Luki half of Plains, Desert, and Savanna villages.
no_center_structure_gen=false
```

Existing 1.1.5 configs migrate automatically: the old Plains/Snowy/Taiga values are retained and the new Desert/Savanna settings default to Epic.

## 50/50 behavior

Plains, Snowy, and Taiga retain the proven 1.1.5 mixed-start logic.

Desert and Savanna use paired Epic/Luki structure definitions inside the vanilla village structure set. Only the two definitions valid for that biome can generate, giving an equal style choice while keeping the selected village internally consistent.

## Requirements

- Minecraft 1.20.1
- Fabric Loader
- Fabric API
- Epic Structures: Villages 2.0.0 or newer compatible 2.x build
- Luki's Grand Capitals
- Mod Menu 7.2.2 (optional, client-side configuration UI)

## Building

The project targets Java 17 bytecode and can be built on JDK 21.

```bash
gradle build
```

The JAR will be placed in `build/libs/`.

## License

MIT. See [LICENSE](LICENSE).
