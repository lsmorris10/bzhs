package com.sevendaystominecraft.capability;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Default implementation of {@link ISevenDaysPlayerStats}.
 *
 * Persisted via NeoForge Data Attachments using {@link INBTSerializable}.
 * All stat floats are clamped to [0, maxValue]. Debuffs are stored as
 * a sub-CompoundTag mapping debuff IDs to remaining tick durations.
 *
 * Spec reference: §1.1 (stats), §1.2 (debuffs)
 *
 * NeoForge 21.4.140 compatibility: Uses INBTSerializable<CompoundTag>
 * which is the serialization interface required by AttachmentType.serializable().
 */
public class SevenDaysPlayerStats implements ISevenDaysPlayerStats, INBTSerializable<CompoundTag> {

    // ── Defaults from spec §1.1 ─────────────────────────────────────────
    private static final float DEFAULT_MAX_FOOD = 100.0f;
    private static final float DEFAULT_MAX_WATER = 100.0f;
    private static final float DEFAULT_MAX_STAMINA = 100.0f;
    private static final float DEFAULT_CORE_TEMP = 70.0f; // °F baseline

    // ── Stat fields ─────────────────────────────────────────────────────
    private float food = DEFAULT_MAX_FOOD;
    private float maxFood = DEFAULT_MAX_FOOD;

    private float water = DEFAULT_MAX_WATER;
    private float maxWater = DEFAULT_MAX_WATER;

    private float stamina = DEFAULT_MAX_STAMINA;
    private float maxStamina = DEFAULT_MAX_STAMINA;

    /** True when stamina hit 0; blocks sprint until recovery above 20%. */
    private boolean staminaExhausted = false;

    private float coreTemperature = DEFAULT_CORE_TEMP;

    /** Active debuffs: debuffId → remaining ticks. */
    private final Map<String, Integer> debuffs = new HashMap<>();

    // =====================================================================
    // ISevenDaysPlayerStats implementation
    // =====================================================================

    @Override public float getFood() { return food; }
    @Override public void setFood(float value) { this.food = clamp(value, 0f, maxFood); }
    @Override public float getMaxFood() { return maxFood; }
    @Override public void setMaxFood(float value) { this.maxFood = Math.max(0f, value); }

    @Override public float getWater() { return water; }
    @Override public void setWater(float value) { this.water = clamp(value, 0f, maxWater); }
    @Override public float getMaxWater() { return maxWater; }
    @Override public void setMaxWater(float value) { this.maxWater = Math.max(0f, value); }

    @Override public float getStamina() { return stamina; }
    @Override public void setStamina(float value) { this.stamina = clamp(value, 0f, maxStamina); }
    @Override public float getMaxStamina() { return maxStamina; }
    @Override public void setMaxStamina(float value) { this.maxStamina = Math.max(0f, value); }

    @Override public boolean isStaminaExhausted() { return staminaExhausted; }
    @Override public void setStaminaExhausted(boolean exhausted) { this.staminaExhausted = exhausted; }

    @Override public float getCoreTemperature() { return coreTemperature; }
    @Override public void setCoreTemperature(float value) { this.coreTemperature = value; }

    @Override
    public Map<String, Integer> getDebuffs() {
        return Collections.unmodifiableMap(debuffs);
    }

    @Override
    public void addDebuff(String debuffId, int durationTicks) {
        // Longest duration wins (no downgrade)
        debuffs.merge(debuffId, durationTicks, Math::max);
    }

    @Override
    public void removeDebuff(String debuffId) {
        debuffs.remove(debuffId);
    }

    @Override
    public boolean hasDebuff(String debuffId) {
        return debuffs.containsKey(debuffId);
    }

    @Override
    public void tickDebuffs() {
        Iterator<Map.Entry<String, Integer>> it = debuffs.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            int remaining = entry.getValue() - 1;
            if (remaining <= 0) {
                it.remove();
            } else {
                entry.setValue(remaining);
            }
        }
    }

    @Override
    public void copyFrom(ISevenDaysPlayerStats other) {
        this.food = other.getFood();
        this.maxFood = other.getMaxFood();
        this.water = other.getWater();
        this.maxWater = other.getMaxWater();
        this.stamina = other.getStamina();
        this.maxStamina = other.getMaxStamina();
        this.staminaExhausted = other.isStaminaExhausted();
        this.coreTemperature = other.getCoreTemperature();
        this.debuffs.clear();
        this.debuffs.putAll(other.getDebuffs());
    }

    // =====================================================================
    // INBTSerializable<CompoundTag> — NeoForge 21.4.140 persistence
    // =====================================================================

    /**
     * Serialize player stats to NBT for world save.
     * Called automatically by the attachment system.
     *
     * @param provider the holder lookup provider (for registry-aware serialization)
     */
    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Food", food);
        tag.putFloat("MaxFood", maxFood);
        tag.putFloat("Water", water);
        tag.putFloat("MaxWater", maxWater);
        tag.putFloat("Stamina", stamina);
        tag.putFloat("MaxStamina", maxStamina);
        tag.putBoolean("StaminaExhausted", staminaExhausted);
        tag.putFloat("CoreTemp", coreTemperature);

        // Serialize debuffs as a sub-compound
        if (!debuffs.isEmpty()) {
            CompoundTag debuffTag = new CompoundTag();
            for (Map.Entry<String, Integer> entry : debuffs.entrySet()) {
                debuffTag.putInt(entry.getKey(), entry.getValue());
            }
            tag.put("Debuffs", debuffTag);
        }

        return tag;
    }

    /**
     * Deserialize player stats from NBT on world load.
     * Called automatically by the attachment system.
     *
     * @param provider the holder lookup provider (for registry-aware deserialization)
     * @param tag the compound tag to read from
     */
    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        food = tag.getFloat("Food");
        maxFood = tag.contains("MaxFood") ? tag.getFloat("MaxFood") : DEFAULT_MAX_FOOD;
        water = tag.getFloat("Water");
        maxWater = tag.contains("MaxWater") ? tag.getFloat("MaxWater") : DEFAULT_MAX_WATER;
        stamina = tag.getFloat("Stamina");
        maxStamina = tag.contains("MaxStamina") ? tag.getFloat("MaxStamina") : DEFAULT_MAX_STAMINA;
        staminaExhausted = tag.contains("StaminaExhausted") && tag.getBoolean("StaminaExhausted");
        coreTemperature = tag.contains("CoreTemp") ? tag.getFloat("CoreTemp") : DEFAULT_CORE_TEMP;

        // Deserialize debuffs from sub-compound
        debuffs.clear();
        if (tag.contains("Debuffs")) {
            CompoundTag debuffTag = tag.getCompound("Debuffs");
            for (String key : debuffTag.getAllKeys()) {
                int ticks = debuffTag.getInt(key);
                if (ticks > 0) {
                    debuffs.put(key, ticks);
                }
            }
        }
    }

    // ── Known debuff IDs from spec §1.2 ─────────────────────────────────
    /** All debuff IDs that the system recognizes. */
    public static final String[] KNOWN_DEBUFF_IDS = {
        "bleeding", "infection_1", "infection_2", "dysentery",
        "sprain", "fracture", "concussion", "burn",
        "hypothermia", "hyperthermia", "radiation",
        "electrocuted", "stunned"
    };

    // ── Debuff ID constants for code clarity ────────────────────────────
    public static final String DEBUFF_BLEEDING = "bleeding";
    public static final String DEBUFF_INFECTION_1 = "infection_1";
    public static final String DEBUFF_INFECTION_2 = "infection_2";
    public static final String DEBUFF_DYSENTERY = "dysentery";
    public static final String DEBUFF_SPRAIN = "sprain";
    public static final String DEBUFF_FRACTURE = "fracture";
    public static final String DEBUFF_CONCUSSION = "concussion";
    public static final String DEBUFF_BURN = "burn";
    public static final String DEBUFF_HYPOTHERMIA = "hypothermia";
    public static final String DEBUFF_HYPERTHERMIA = "hyperthermia";
    public static final String DEBUFF_RADIATION = "radiation";
    public static final String DEBUFF_ELECTROCUTED = "electrocuted";
    public static final String DEBUFF_STUNNED = "stunned";

    // ── Helpers ──────────────────────────────────────────────────────────

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
