# 7 Days to Minecraft — NeoForge Mod

## Project Overview
A total conversion mod for Minecraft 1.21.4 (NeoForge) that brings 7 Days to Die survival gameplay into Minecraft. Aligned to 7 Days to Die 2.6 Experimental (Feb 2026).

**Mod ID**: `sevendaystominecraft`  
**Loader**: NeoForge 21.4.140  
**Java**: 21 (required)  
**Minecraft**: 1.21.4  

## Build System
- **Gradle 8.13** via wrapper (`./gradlew`)
- **NeoGradle 7.0+** plugin for NeoForge mod development
- Java toolchain set to Java 21 in `build.gradle`
- `JAVA_HOME` must point to Java 21 (jdk21 installed via Nix)

## Architecture

### Source Layout
```
src/main/java/com/sevendaystominecraft/
├── SevenDaysToMinecraft.java       — Main mod entry point (@Mod)
├── capability/
│   ├── ISevenDaysPlayerStats.java  — Player stats interface
│   ├── ModAttachments.java         — NeoForge data attachments registration
│   ├── PlayerStatsHandler.java     — Event handlers for player stats
│   └── SevenDaysPlayerStats.java   — Player stats implementation (Food, Water, Stamina, etc.)
├── client/
│   └── StatsHudOverlay.java        — HUD overlay for player stats
├── config/
│   └── SurvivalConfig.java         — Server-side survival config (survival.toml)
├── mixin/
│   ├── FoodDataMixin.java          — Cancels vanilla food saturation
│   ├── LivingEntityHurtMixin.java  — Custom damage handling
│   ├── PlayerHealMixin.java        — Blocks vanilla passive regen
│   └── SprintBlockMixin.java       — Sprint blocked when low stamina
└── network/
    ├── ModNetworking.java          — Packet channel registration
    └── SyncPlayerStatsPayload.java — Client/server stats sync packet
```

### Key Systems Implemented (Phase 1)
- **Player Stats**: Food, Water, Stamina, Temperature, Debuffs via NeoForge DataAttachments
- **Custom HUD**: StatsHudOverlay renders custom bars (replacing vanilla hearts/food)
- **Mixins**: Vanilla food, regen, sprint systems overridden
- **Networking**: Stats synced from server to client every tick
- **Config**: `survival.toml` for per-server tuning of survival parameters

## Workflow
- **Build Mod**: `export JAVA_HOME=$(dirname $(dirname $(which java))) && ./gradlew build --no-daemon`
  - Compiles Java sources, processes resources, assembles the mod JAR
  - Output JAR: `build/libs/sevendaystominecraft-0.1.0-alpha.jar`
  - First run downloads Minecraft + NeoForge dependencies (~several minutes)
  - This is a build workflow (console output type), not a web server

## Environment
- Java 21 installed via Nix (`jdk21` package)
- `gradlew` script created for Linux (gradlew.bat only existed for Windows)
- `gradle/wrapper/gradle-wrapper.jar` downloaded separately (excluded from git)

## Development Notes
- No frontend web server — this is a pure Java Minecraft mod
- Use `./gradlew build` to compile and package the mod JAR
- Use `./gradlew runClient` to launch Minecraft with the mod (requires display)
- Use `./gradlew runServer` to launch a Minecraft server with the mod
- Full spec in `docs/7dtm_final_spec.md` (2273 lines, 20 sections)
- See `archive/` for resolved spec drafts

## Spec / Roadmap
The full implementation is tracked in `docs/7dtm_final_spec.md` with 19 phases.
Currently at Phase 1 (Milestone 1-3): Core survival stats, HUD, config, networking.
