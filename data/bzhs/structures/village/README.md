# NBT Structure Templates for Village Buildings

## Overview
This directory holds `.nbt` structure template files for village buildings. When a village
generates, the system checks for templates matching each building type. If a template
exists, it is placed instead of the procedural fallback.

The system loads templates via the Minecraft StructureTemplateManager, which reads from
the datapack path `data/bzhs/structures/village/`. Place `.nbt` files directly in this
directory.

## How to Create Templates

### Step 1: Build the Structure In-Game
1. Enter Creative Mode.
2. Build your structure on flat ground. The bottom-left corner of the build will be the
   placement origin, so align accordingly.
3. Include loot containers (kitchen cabinets, bookshelves, tool crates, etc.) inside the
   structure — the system detects them automatically and initializes their loot tier.

### Step 2: Use Structure Blocks to Save
1. Place a Structure Block (give yourself one with `/give @p structure_block`).
2. Set the Structure Block to **Save** mode.
3. Enter a structure name (e.g., `residential_1`).
4. Set the bounding box to encompass the entire build. Use the corner mode or manually
   enter the size and offset.
5. Press **SAVE**.

### Step 3: Export the `.nbt` File
The saved structure is stored in your world's `generated` folder:
```
<world>/generated/minecraft/structures/<name>.nbt
```
Copy the `.nbt` file into the datapack structure path:
```
data/bzhs/structures/village/<name>.nbt
```

### File Naming Convention
Files must follow this pattern:
```
<building_type>_<variant_number>.nbt
```

Building type names (lowercase):
- `residential_1.nbt`, `residential_2.nbt`, ...
- `crack_a_book_1.nbt`
- `working_stiffs_1.nbt`
- `pass_n_gas_1.nbt`
- `pop_n_pills_1.nbt`
- `farm_1.nbt`
- `utility_1.nbt`
- `trader_outpost_1.nbt`

Up to 10 variants per type are supported (`_1` through `_10`). When multiple variants
exist, one is chosen at random during generation.

## Datapack Integration
Templates are loaded as `bzhs:village/<type>_<n>` via StructureTemplateManager. The
corresponding file path in a datapack is:
```
data/bzhs/structures/village/<type>_<n>.nbt
```

## Tips
- Keep structures relatively small (7-20 blocks per side) to fit village layouts.
- Include interior air space so zombies can spawn inside.
- Place loot containers from the mod (not vanilla chests) for proper loot integration.
- Leave the ground floor at y=0 relative to the structure origin for correct terrain
  alignment.
