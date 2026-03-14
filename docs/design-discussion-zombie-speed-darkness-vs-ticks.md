# Zombie Speed Boost: Darkness vs. Tick-Based Night

## The Question

Should zombies get their speed boost because it's **dark where they are**, or because the **clock says it's nighttime**?

Right now it's the clock. This document lays out how it currently works, why darkness-based might be better, and the tradeoffs — so we can decide together.

---

## Background: Where This Question Came From

While auditing how the mod's systems use light vs. the tick-based clock, we found an inconsistency worth discussing. Here's the full breakdown:

### Spawning — it's a mix of both

Not all zombie spawns work the same way:

- **Heatmap mini-hordes** (the ambient zombie waves that happen around players) **do check light level**. They look for dark spots to spawn, so they naturally follow the sun's rhythm. Once the sky rendering is fully synced to the 48,000-tick cycle, these will correctly ramp up during the longer night.

- **Scouts/Screamers** (heatmap-triggered, spawn when your activity heat gets high) **ignore light level**. They spawn based on your activity heat, day or night. Doesn't matter if it's bright or dark.

- **Blood Moon hordes** **ignore light level**. They spawn no matter what. That's by design — Blood Moon is supposed to be unavoidable.

### Night speed bonus — uses tick count, not light

The speed boost checks if `dayTime % 48000` falls between tick 26,000 and 46,000. It's purely clock-based. It doesn't look at the light where the zombie is standing at all.

### The inconsistency

So spawning partially cares about light, but speed never does. A zombie that spawned *because* it was dark will then run at the same speed as one standing in full daylight — the speed system doesn't know or care about light. Is that the right design, or should speed also respond to darkness?

---

## How the Speed Bonus Works Today

The zombie speed bonus is **purely time-based**. Every tick, each zombie checks the world clock:

- The day/night cycle is **48,000 ticks** long.
- "Night" is defined as tick **26,000 to 46,000** (about 20,000 ticks of night).
- During that window, all zombies everywhere get a speed multiplier: `baseSpeed * (1.0 + nightSpeedBonus)`.
- The default `nightSpeedBonus` is **1.25**, so zombies move at **2.25x** their base speed at night.

**Key point:** It doesn't matter where the zombie is. A zombie standing in a fully lit room gets the same speed boost as one in a pitch-black cave. A zombie deep underground at noon is slow. A zombie on the surface at midnight is fast. It's all about the clock, not the light.

---

## The Case for Darkness-Based Speed

### The argument

In 7 Days to Die, darkness = danger. That's the core feel. Zombies are scarier in the dark. So the speed boost should come from **actual darkness**, not an arbitrary time window.

### What would change

Instead of checking "is it between tick 26,000 and 46,000?", each zombie would check the **light level at its position**. If it's dark enough, it gets the speed boost.

This means:

- **Caves at noon:** Zombies are fast (it's dark down there).
- **Well-lit base at midnight:** Zombies are slower (you've lit the area up).
- **Open surface at night:** Zombies are fast (no sky light at night).
- **Underground bunker with torches:** Zombies are slower (you invested in lighting).

### Why this might be better

1. **Caves become scarier.** Right now, cave zombies during the day are slow and unthreatening. In 7DTD, underground zombies are always dangerous. Darkness-based speed fixes this automatically.

2. **Lighting matters as a defense.** Players can use torches and light sources strategically. Light your perimeter, slow down approaching zombies. This adds a layer of base defense strategy that currently doesn't exist.

3. **It's more intuitive.** "Zombies are fast in the dark" is a simpler mental model than "zombies are fast between tick 26,000 and 46,000." Players don't see a clock — they see darkness.

4. **Consistency with spawning.** The heatmap spawner already uses light levels to decide where zombies spawn. It checks both block light and sky light, and prefers spawning in dark areas (block light <= 7 AND sky light <= 7). Having speed also respect light creates a consistent system.

---

## The Case for Keeping Tick-Based

1. **Predictability.** Players know that night = danger. It's a clear, global state. Everyone experiences it at the same time. With darkness-based speed, it could feel inconsistent — "why is this zombie fast and that one slow?"

2. **Performance.** Checking light level per zombie per tick is more expensive than one global time check. With many zombies active, this could matter. (Though Minecraft already calculates light levels, so the data is there — it's just a lookup.)

3. **7DTD fidelity.** In the original game, the day/night cycle is the primary driver, not per-zombie light checks. Night = run. Day = walk. It's a global rule.

4. **Exploitability.** If light slows zombies, players might just spam torches everywhere and trivialize the night threat. The speed boost's whole purpose is to make nights dangerous — tying it to light could undermine that.

---

## Possible Approaches

### Option A: Pure Darkness (Light-Level Only)
- Speed bonus based entirely on the light level at each zombie's feet.
- Threshold could match the existing spawn logic: dark = block light <= 7 AND sky light <= 7.
- Night is dangerous because it's dark. Caves are dangerous because they're dark. Well-lit areas are safe(r).
- Torches become a strategic resource.

### Option B: Hybrid (Time + Light)
- Keep the night speed bonus as-is (time-based, all zombies speed up at night).
- **Add** a darkness bonus for zombies in dark areas during the day.
- So: daytime cave zombies are still fast, nighttime zombies are always fast, and well-lit areas don't help at night.
- This preserves the "night is always scary" guarantee while making caves scarier too.

### Option C: Keep Current (Tick-Based Only)
- Don't change anything. Night = fast. Day = slow. Simple and predictable.
- Caves during the day remain low-threat.

### Option D: Darkness with a Night Floor
- Use light levels as the primary driver (like Option A).
- But during the night window, zombies get **at minimum** the night speed bonus regardless of light.
- So lighting your base helps during the day (and in caves), but at night they're fast no matter what.
- Prevents the "spam torches to trivialize night" exploit.

---

## Things to Decide

1. **Should cave zombies be fast during the day?** If yes, some form of light-level checking is needed (Options A, B, or D).

2. **Should lighting your base slow down zombies?** If yes, that's Options A or D. If that feels too exploitable, Option B or C avoids it.

3. **How important is the "night is always dangerous" guarantee?** If it's sacred, Options B or D protect it. Option A could potentially let players light-cheese their way through nights.

4. **Performance budget?** Light-level lookups per zombie per tick — is this a concern with the zombie counts we're targeting?

5. **Does this affect Blood Moon?** During Blood Moon, should zombies always be at max speed regardless of light? Probably yes.

---

## Technical Notes (For the Devs)

- Light level data is already available via `level.getBrightness(LightLayer.BLOCK, pos)` and `level.getBrightness(LightLayer.SKY, pos)`.
- The heatmap spawner already uses this exact pattern (see `HeatmapSpawner.java` lines 202-204).
- The speed bonus logic lives in `BaseSevenDaysZombie.applyNightSpeedBonus()` — this is where the change would go.
- The check runs every tick per zombie, so the light lookup would happen frequently. Consider caching or checking every N ticks if performance is a concern.
- `ZombieConfig` already has a `nightSpeedBonus` config value. A `darknessSpeedBonus` could be added alongside it.

---

## Open for Discussion

What feels right for the mod? Drop your thoughts.

---

## Relevant files
- `src/main/java/com/sevendaystominecraft/entity/zombie/BaseSevenDaysZombie.java:208-231`
- `src/main/java/com/sevendaystominecraft/heatmap/HeatmapSpawner.java:202-208`
- `src/main/java/com/sevendaystominecraft/SevenDaysConstants.java`
- `src/main/java/com/sevendaystominecraft/config/ZombieConfig.java`
