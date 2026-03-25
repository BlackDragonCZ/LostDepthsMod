# Lost Depths — KubeJS Integration Guide

Reference for modpack developers using KubeJS with Lost Depths.

---

## Java Class Access

All Lost Depths classes can be accessed via `Java.loadClass()`:

```js
const TextEffects = Java.loadClass('cz.blackdragoncz.lostdepths.util.TextEffects')
const LostdepthsModItems = Java.loadClass('cz.blackdragoncz.lostdepths.init.LostdepthsModItems')
const LostdepthsModBlocks = Java.loadClass('cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks')
const LostdepthsModOres = Java.loadClass('cz.blackdragoncz.lostdepths.init.LostdepthsModOres')
```

---

## Text Effects

### Rainbow Text
Cycles each character through rainbow colors. Pass tick for animation, 0 for static.

```js
const TextEffects = Java.loadClass('cz.blackdragoncz.lostdepths.util.TextEffects')

// Static rainbow (no animation)
TextEffects.rainbow('Hello World')

// Animated rainbow (pass game tick)
TextEffects.rainbow('Hello World', Client.player.age)
```

### Wave Shimmer
Brightness peak travels across text. Base color brightens toward white at the peak.

```js
// Wave left-to-right (cyan base)
TextEffects.wave('NuroTech', 0x00CCFF, Client.player.age, false)

// Wave bounce / ping-pong (gold base)
TextEffects.wave('OMEGA', 0xFFAA00, Client.player.age, true)
```

**Parameters:** `wave(text, baseColorRGB, tick, bounce)`
- `text` — the string
- `baseColorRGB` — hex color (e.g. `0xFF5555` for red)
- `tick` — game tick for animation (use `0` for static, `Client.player.age` for animated)
- `bounce` — `false` = left-to-right loop, `true` = ping-pong

**Note:** Text effects respect the player's Graphics setting. On Graphics: Fast, effects render as static flat color.

---

## Custom Tooltips

### Adding Styled Tooltips
```js
ItemEvents.tooltip(event => {
    // Simple colored tooltip
    event.add('lostdepths:nurotech_controller', Text.gold('Requires Nurostar energy'))

    // Rainbow tooltip
    const TextEffects = Java.loadClass('cz.blackdragoncz.lostdepths.util.TextEffects')
    event.addAdvanced('lostdepths:nurotech_controller', (item, advanced, text) => {
        text.add(TextEffects.rainbow('Powered by imagination'))
    })

    // Wave tooltip with custom color
    event.addAdvanced('lostdepths:nt_terminal', (item, advanced, text) => {
        text.add(TextEffects.wave('Network Online', 0x55FF55, 0, false))
    })

    // Combined vanilla + custom formatting
    event.addAdvanced('lostdepths:nt_drive', (item, advanced, text) => {
        let line = Text.empty()
        line.append(Text.gray('Status: '))
        line.append(TextEffects.wave('Active', 0x55FF55, 0, true))
        text.add(line)
    })
})
```

### Conditional Tooltips (Shift/Ctrl/Alt)
```js
ItemEvents.tooltip(event => {
    event.addAdvanced('lostdepths:nt_crystal_64k', (item, advanced, text) => {
        if (event.shift) {
            text.add(Text.gray('Capacity: 65,536 items'))
            text.add(Text.gray('Type limit: 64 unique items'))
        } else {
            text.add(Text.darkGray('Hold SHIFT for details'))
        }
    })
})
```

---

## Light Puzzle Recipes

Custom recipe type `lostdepths:light_puzzle`. Single input item → output item with count.

### Adding Recipes
```js
// server_scripts/light_puzzle.js
ServerEvents.recipes(event => {
    // Custom light puzzle recipe
    event.custom({
        type: 'lostdepths:light_puzzle',
        input: { item: 'minecraft:diamond' },
        output: { item: 'lostdepths:infused_crystal', count: 4 }
    })

    // Another example
    event.custom({
        type: 'lostdepths:light_puzzle',
        input: { item: 'lostdepths:carbonic_acid' },
        output: { item: 'lostdepths:infused_crystal', count: 2 }
    })
})
```

### Removing Recipes
```js
ServerEvents.recipes(event => {
    // Remove by output
    event.remove({ type: 'lostdepths:light_puzzle', output: 'lostdepths:infused_crystal' })

    // Remove by ID
    event.remove({ id: 'lostdepths:light_puzzle_carbonic_acid' })
})
```

---

## Compressor Recipes

Lost Depths has 3 compressor tiers. Recipe types: `lostdepths:v1_compressing`, `lostdepths:v2_compressing`, `lostdepths:v3_compressing`.

```js
ServerEvents.recipes(event => {
    // Tier 1 compressor recipe
    event.custom({
        type: 'lostdepths:v1_compressing',
        input: { item: 'minecraft:iron_ingot', count: 4 },
        output: { item: 'lostdepths:compressed_iron' }
    })
})
```

---

## Workstation Recipes

Galactic Workstation and Alloy Workstation use shaped recipes.
Recipe types: `lostdepths:galactic_workstation`, `lostdepths:alloy_workstation`.

```js
ServerEvents.recipes(event => {
    event.custom({
        type: 'lostdepths:galactic_workstation',
        pattern: ['ABA', 'CDC', 'ABA'],
        key: {
            A: { item: 'lostdepths:infused_iron' },
            B: { item: 'minecraft:diamond' },
            C: { item: 'lostdepths:infused_crystal' },
            D: { item: 'lostdepths:power_core' }
        },
        result: { item: 'lostdepths:advanced_power_core' }
    })
})
```

---

## Fusion Table Recipes

Two-input fusion. Recipe type: `lostdepths:fusion_table`.

```js
ServerEvents.recipes(event => {
    event.custom({
        type: 'lostdepths:fusion_table',
        input1: { item: 'lostdepths:raw_firerite' },
        input2: { item: 'lostdepths:raw_melworite' },
        output: { item: 'lostdepths:fused_alloy' }
    })
})
```

---

## Ore Information

### Querying Ore Tiers
```js
const LostdepthsModOres = Java.loadClass('cz.blackdragoncz.lostdepths.init.LostdepthsModOres')

// Get ore definition by block
let oreDef = LostdepthsModOres.findByBlock(block)
if (oreDef != null) {
    let tier = oreDef.minTier()        // PickaxeTier enum (FORGEFIRE, CRYSTALIZED, CELESTIAL, NIGHTMARE)
    let baseDrop = oreDef.baseDrop()   // base drop count
    let dropItem = oreDef.dropItem()   // supplier for drop item
}

// List all registered ores
let allOres = LostdepthsModOres.getAll()
```

### Pickaxe Tiers (lowest to highest)
1. `FORGEFIRE` — Forgefire Pickaxe
2. `CRYSTALIZED` — Crystalized Pickaxe
3. `CELESTIAL` — Celestial Pickaxe
4. `NIGHTMARE` — Nightmare Pickaxe

---

## Item Tags

### Security Pass Items
Tag: `lostdepths:lightpuzzlec` — (deprecated, removed in favor of recipe system)

### Block Tags
All Lost Depths blocks that need a pickaxe are tagged in datagen via `LostdepthsModBlockTags`.

---

## Dimension IDs

- `lostdepths:below_bedrock`
- `lostdepths:between_bedrock_and_overworld`
- `lostdepths:lost_dungeons`

### Checking Player Dimension
```js
PlayerEvents.tick(event => {
    let dimId = event.player.level.dimension.toString()
    if (dimId.includes('lostdepths:lost_dungeons')) {
        // Player is in Lost Dungeons
    }
})
```

---

## Notes

- **Graphics: Fast** — TextEffects automatically fall back to static colors when the player uses Graphics: Fast. No extra handling needed.
- **Tooltip animation** — Minecraft caches tooltip rendering, so animated effects (rainbow/wave with tick) may appear static in standard tooltips. They work best in custom GUIs that re-render every frame.
- **Recipe compatibility** — All Lost Depths recipe types work with KubeJS `event.custom()`. Use `event.remove()` to modify or replace existing recipes.
