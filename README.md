# Goosker's Epic Villages Compat

Fabric 1.20.1 compatibility layer for **Epic Structures: Villages** and **Luki's Grand Capitals**.

Current version: **1.1.5-alpha**

## What it does

The mod lets Plains, Snowy, and Taiga villages use Epic, Luki, or a true per-village 50/50 selection while keeping each generated village internally consistent.

- `true` = Epic Structures: Villages
- `false` = Luki's Grand Capitals
- `50_50` = equal chance for each newly generated village to start as Epic or Luki
- Desert and Savanna stay Luki because Epic does not provide those village styles
- Optional centerless mode removes Luki's decorative village centers from Plains, Desert, and Savanna
- Structurify settings are not modified

The 50/50 mode mixes only the **starting pool**. Epic starting pieces continue into Minecraft's vanilla village pools whose NBT pieces are replaced by Epic, while Luki starting pieces continue into `revampedvillages:*` pools. This avoids mixing Epic and Luki buildings inside the same village.

## Configuration

The config is created at:

```text
config/gooskers_epic_villages_compat.properties
```

Default configuration:

```properties
# Village values: true = Enable generation | false = Disable generation | 50_50 = equal chance per each village

epic_plains=true
epic_snowy=true
epic_taiga=true

# [Bonus Feature] Luki's Grand Capitals
no_center_structure_gen=false
```

Restart Minecraft after changing the config.

When `epic_plains=50_50` and `no_center_structure_gen=true`, only the Luki half of Plains villages becomes centerless.

## Requirements

- Minecraft 1.20.1
- Fabric Loader
- Fabric API
- Epic Structures: Villages
- Luki's Grand Capitals

## Building

This repository targets Java 17 bytecode and can be built on JDK 21.

With Gradle 8.8 installed:

```bash
gradle build
```

The built mod JAR will be in `build/libs/`.

GitHub Actions is included and builds automatically on pushes and pull requests.

## Project layout

```text
src/main/java/        Java source
src/main/resources/   fabric.mod.json and built-in data packs
```

## License

MIT. See [LICENSE](LICENSE).
