# Project Notes — Brutal Zombie Horde Survival (BZHS)

---

## Development Workflow (Permanent)

**Primary coding environment:** Replit Agent
**Testing environment:** Antigravity (local PC)

| Environment | Role | Push Frequency |
|-------------|------|---------------|
| **Replit** | Main coding, building, pushing to GitHub | Most pushes |
| **Antigravity** | Pull, test mod in-game on local PC, occasional code + push | Less frequent |

**Sync Rules:**
1. **Before coding in Antigravity:** Always `git pull` first to grab latest from Replit.
2. **Before starting work in Replit after an Antigravity session:** Run `git fetch origin && git reset --hard origin/master` to pick up anything pushed from Antigravity.
3. **Never squash or rebase from Replit** — Replit cannot force-push to GitHub. Do history cleanup from Antigravity only.
4. **Replit auto-creates checkpoint commits** ("Transitioned from Plan to Build mode", task completion commits). This is normal. Squash from Antigravity if cleaner history is desired, then sync Replit with step 2.
5. **Replit creates `subrepl-*` branches** for parallel task agents. These are temporary and can be deleted from Antigravity after tasks are merged.

---

## Current Status & Next Up

**Completed Milestones:** 1 (Scaffold), 2 (Player Stats), 3 (Debuffs — all 12 types), 4 (Temperature — partial), 5 (Heatmap), 6 (Loot & Crafting), 7 (XP/Leveling/Perks), 8 (Blood Moon/Horde Night), 9 (HUD — compass, minimap, stats overlay).

**March 16–17 Major Completed Work:**
- Zombie AI behavior tree refactored (#72) — fully layered, priority-ordered, conditions-checked AI system
- Gameplay bugfixes (#76) — assorted client/server crashes and balance corrections
- Textures, models, and blockstates added and organized (#77)
- New world startup fixes — mod now correctly initializes a fresh world without errors (#78)
- Zombie block breaking AI fix (#79) — path-to-target correctly triggers block break behavior
- Minimap fix (#80) — terrain rendering and player tracking corrected
- HP display fix (#81) — health readout now accurate at all HP values
- Health and combat rebalance (#82, #83) — zombie HP/damage tuned, player survivability improved
- Item texture fixes (#84) — missing/broken item textures resolved
- Container GUI fixes (#85) — workstation and loot container GUIs stable
- Language file updates (#89) — all new items, blocks, and UI strings localized
- Deprecated API fixes (#91) — NeoForge API call sites updated to current 1.21.4 signatures
- Legacy config cleanup (#92) — stale config keys removed, config files reorganized
- Zombie AI special abilities (#93) — block breaking, heatmap investigation trigger, horde pathfinding, variant-specific abilities (acid spit, charge, ground pound, etc.)
- Workstation recipe processing (#94) — all 7 workstations now process recipes with correct fuel logic and output
- Basic weapons system (#95) — melee and ranged weapons implemented with damage, range, and attack speed
- Placeholder texture audit (#97) — full audit run; 349 of 388 textures flagged as placeholder (report at `docs/texture_audit.md`)
- Icon-based HUD (#100) — stat bars replaced with icon rows (hearts, food, water, armor icons)
- Texture processing tool (#101) — batch tool for generating and validating texture assets
- Funding page (#104) — Ko-fi/Patreon support page added to landing site

**Current Focus / In Progress:**
- Sound system foundation (in progress)
- Territory POIs — location-specific points of interest for world generation
- 3D weapon animations via Geckolib

**Next Up:**
- Merge sound system and territory POI tasks once complete
- Geckolib integration for animated weapon/zombie models
- Sprint bug fix (client-side Mixin on `LocalPlayer.aiStep()`)
- Custom textures — replace 349 placeholder textures with real pixel art (prioritize HUD icons, weapons, workstations)

---

## Known Bugs / Polish To Address

1. **SPRINT BUG — FIX ANOTHER DAY** (unresolved since Milestone 2):
   - Sprint can get stuck — holding W alone gives infinite sprint. Stamina drains but sprint doesn't cancel. Needs a client-side Mixin on `LocalPlayer.aiStep()`.
   - **Not on today's test list.** This is a known issue that requires a proper client-side fix.

2. **F3 DEBUG SCREEN DAY COUNTER** (likely resolved):
   - After the slower-tick refactor (#60), `dayTime` stays on a vanilla 24k scale. F3 day counter should now match the HUD. Verify during next test session.

3. **PLACEHOLDER TEXTURES (349 of 388)** — Most item, GUI, and some block textures are auto-generated colored squares. Gameplay is functional but visually unpolished. Full list in `docs/texture_audit.md`.

---

## Next Session — Debug & Test Checklist (March 17+)

- **P1 — Build & Launch:** Does the mod build cleanly? Any Mixin or registry errors on startup?
- **P2 — Basic Weapons (melee):** Stone Axe, Wooden Club, Baseball Bat, Sledgehammer — do they deal correct damage with quality scaling? Attack speed correct?
- **P3 — Basic Weapons (ranged):** Pipe Pistol, Primitive Bow — do they fire? Ammo consumption correct? Hit detection working?
- **P4 — Workstation Recipe Processing:** Campfire, Forge, Workbench — do recipes process with correct fuel consumption and output? Does Forge smelting take the right time?
- **P5 — Zombie AI Special Abilities:** Cop acid spit, Demolisher ground pound, Spider climbing, Charged chain lightning — do variant abilities trigger correctly? Does block breaking AI activate on targeted blocks?
- **P6 — Icon-Based HUD:** Hearts, food, water, armor shown as icon rows (not stat bars)? Icons update correctly as values change? No overlap with compass or minimap?
- **P7 — Heatmap + Zombie Investigation:** Does horde pathfinding respond to heat? Do zombies investigate high-heat areas?
- **P8 — Container GUIs:** Open all workstation and loot container GUIs — do they render correctly and accept/process items?
- **P9 — World Startup:** Does a fresh new world generate and load without errors or crashes?
- **P10 — Blood Moon:** Every 7th night still triggers correctly with the rebalanced zombie HP/damage?
- **P11 — Territory POIs (once merged):** Do POI structures spawn in expected locations and biomes?
- **P12 — Sound System (once merged):** Do zombie sounds, combat sounds, and ambient sounds play correctly? Any missing sound events?
- **P13 — Geckolib Animations (once merged):** Do animated weapon and zombie models render without errors?
- **P14 — Sprint Bug (deferred):** Sprint still broken? (Expected — not fixed yet.)
- **P15 — Landing Page + Funding:** Funding page loads? Ko-fi/Patreon links work?

---

## Recent Completed Work

**March 16–17 Session (Tasks #72–#104)**
- Zombie AI behavior tree refactored (#72)
- Gameplay bugfixes (#76)
- Textures/models/blockstates (#77)
- New world startup fixes (#78)
- Zombie block breaking AI fix (#79)
- Minimap fix (#80)
- HP display fix (#81)
- Health and combat rebalance (#82, #83)
- Item texture fixes (#84)
- Container GUI fixes (#85)
- Language file updates (#89)
- Deprecated API fixes (#91)
- Legacy config cleanup (#92)
- Zombie AI special abilities (#93)
- Workstation recipe processing (#94)
- Basic weapons system (#95)
- Placeholder texture audit (#97) — `docs/texture_audit.md`
- Icon-based HUD (#100)
- Texture processing tool (#101)
- Funding page (#104)

**March 14–15 Session**
- Download button upgraded to fetch latest JAR from GitHub Releases API (#55)
- Download button updated to support pre-releases (#56)
- Mod JAR and metadata renamed to match BZHS branding (#57)
- One-line install tutorial added above download button (#58)
- LevelTimeOfDayMixin crash fix — target corrected from `getTimeOfDay` to `getSunAngle` (#59)
- Day cycle refactored to slower-tick approach — 0.5 ticks/server tick via `setDayTime`, no custom sky rendering (#60)
- Vanilla mob damage scaling extended to all vanilla mobs for 100 HP balance (#61)

**March 14 Session**
- Project rebranded to "Brutal Zombie Horde Survival" (BZHS) — mod ID, display name, and all user-facing references updated (#43)
- Landing page created and published (#40, #41)
- Landing page upgraded to V2 with improved layout and design (#45)
- `.gitignore` updated to exclude `public/` folder build artifacts (#46)
- README.md status section restructured with milestone tracking table (#47)
- PROJECT_NOTES.md updated with March 14 status (#48)

**March 13 Session**
- Vanilla damage scaling to 100 HP (fall, drowning, fire, lava, cactus proportionally scaled)
- 48,000-tick day cycle sky fix — **superseded by slower-tick refactor (#60)**
- Darkness-based zombie speed — **later expanded to dual system: night dayTime check + darkness light-level check (#66)**
- Coal vein nerf (reduced ore vein sizes)

**March 12 Late-Session Work [MERGED]**
- Zombie name tag occlusion fix (name tags and HP bars not visible through walls)
- HUD compass and minimap with player tracking
- Sunlight burning disabled for all BZHS zombies
- Zombie behavior summary added to `docs/zombie_guide.md`
- Loot and crafting system (Milestone 6) — loot tables, crafting recipes, item progression tiers
- XP, leveling & perk system — kill XP, level-up notifications, perk unlocks
- Debuffs system — Bleeding, Infection, Dysentery, Sprain, Fracture with triggers and cures
- Stats HUD overlap fix (moved down below compass, removed background)
- Night zombie speed increased to 2.25x
- Day cycle doubled to 48,000 ticks — **superseded by slower-tick refactor (#60)**
- Debuffs persistence bug fixed (twice — `/bzhs cleardebuffs` command + `copyOnDeath` removal)
- Debuffs guide created (`docs/debuffs_guide.md`)
- Player base health set to 100 HP
