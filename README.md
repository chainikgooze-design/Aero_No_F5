# No Aero F5

[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-brightgreen.svg)](https://minecraft.net/)
[![NeoForge](https://img.shields.io/badge/NeoForge-21.1+-orange.svg)](https://neoforged.net/)
[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://adoptium.net/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Side: Client](https://img.shields.io/badge/Side-Client-informational.svg)]()

A lightweight, client-side utility mod for Minecraft 1.21.1 (NeoForge) that preserves standard vanilla third-person perspective (F5) and prevents forced whole-structure or vehicle camera zooms when interacting with Create contraptions and Create: Aeronautics (Sable) airships.

---

## 💡 The Problem

When playing modpacks with **Create** and **Create: Aeronautics** (Sable):
1. **Forced Ship Overview (`SUB_LEVEL_VIEW`)**: Pressing `F5` while steering or riding an airship forces the camera into modded modes that zoom out hundreds of blocks to encompass the entire construction bounding box, making pilot orientation and localized building difficult.
2. **Extreme Contraption Zoom**: Mounting seats on trains, carriages, or mechanical contraptions applies aggressive `mountedZoomMultiplier` logic, pulling the camera back far beyond vanilla player perspective.

**No Aero F5** cleanly resolves these issues client-side, ensuring your `F5` key always behaves predictably.

---

## ✨ Features

- **Standard Vanilla Cycle**: Guarantees standard cycling: `First Person` ➔ `Third Person Back` ➔ `Third Person Front` ➔ `First Person`.
- **Blocks Injected Camera Modes**: Prevents third-party mods from injecting custom camera types into your cycling pipeline or setting them directly.
- **Contraption Distance Clamping**: Normalizes third-person camera distance back to standard player scale (4.0 blocks) when riding trains, contraptions, or airships.
- **Purely Client-Side**: Only required on the client. Fully safe to connect to vanilla or modded servers without needing the mod installed server-side.
- **Zero Runtime Overhead**: Uses targeted Mixin injections and standard NeoForge client events without polling overhead.
- **Soft Dependency**: Gracefully detects Sable/Aeronautics sub-level environments via reflection without requiring compile-time or runtime hard dependencies.

---

## 🏗️ Technical Architecture

| Component | Responsibility | Target |
|---|---|---|
| [`CameraTypeMixin`](src/main/java/com/minechainix/noaerof5/mixin/CameraTypeMixin.java) | Intercepts `CameraType#cycle()` to enforce vanilla enum values and drop custom injected camera states. | `net.minecraft.client.CameraType` |
| [`OptionsMixin`](src/main/java/com/minechainix/noaerof5/mixin/OptionsMixin.java) | Guards `Options#setCameraType` against programmatic overrides from external mods. | `net.minecraft.client.Options` |
| [`CameraMixin`](src/main/java/com/minechainix/noaerof5/mixin/CameraMixin.java) | Normalizes camera distance back to vanilla scale during camera setup before terrain raycasting. | `net.minecraft.client.Camera` |
| [`ClientEvents`](src/main/java/com/minechainix/noaerof5/client/ClientEvents.java) | Subscribes to NeoForge's `CalculateDetachedCameraDistanceEvent` at lowest priority to clamp excessive distance. | NeoForge Client Event Bus |
| [`NoAeroF5Helper`](src/main/java/com/minechainix/noaerof5/NoAeroF5Helper.java) | Safe reflection and passenger inspection utility for Create contraption and Sable sub-level detection. | Internal Utility |
| [`NoAeroF5Config`](src/main/java/com/minechainix/noaerof5/NoAeroF5Config.java) | Manages client-side configurable thresholds (`config/no_aero_f5-client.toml`). | NeoForge ModConfig |

---

## ⚙️ Configuration

The configuration file is located at `.minecraft/config/no_aero_f5-client.toml`:

```toml
[general]
    # Prevent F5 from cycling into custom non-vanilla camera types (such as Sable Sub-Level / Ship view).
    disableCustomCameraTypes = true

    # Prevent third-person camera from zooming out to frame Create contraptions, trains, or Sable ships.
    disableContraptionZoom = true

    # Clamp any third-person camera distance that exceeds standard distance back to normal.
    alwaysClampExcessiveDistance = true

    # Standard third-person camera distance in blocks (Vanilla is 4.0).
    # Range: 1.0 ~ 32.0
    defaultCameraDistance = 4.0
```

---

## 📦 Building from Source

### Prerequisites
- **JDK 21** or higher
- Git

### Build Steps
```bash
git clone https://github.com/chainikgooze-design/Aero_No_F5.git
cd Aero_No_F5
./gradlew build
```

The compiled mod JAR will be located in `build/libs/`:
```text
build/libs/no_aero_f5-<version>+mc.1.21.1.jar
```

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
