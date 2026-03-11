package com.sevendaystominecraft.capability;

import java.util.Map;

/**
 * Public API for 7 Days to Minecraft custom player stats.
 *
 * Replaces vanilla hunger/saturation with a multi-resource survival system
 * per spec §1.1. All values are floats clamped to [0, maxValue].
 *
 * Attached to Player entities via NeoForge Data Attachments
 * ({@link ModAttachments#PLAYER_STATS}).
 */
public interface ISevenDaysPlayerStats {

    // ── Food (Fullness) ─────────────────────────────────────────────────
    /** Current food level (0–maxFood). Passive drain −0.2/min + activity mult. */
    float getFood();
    void setFood(float value);
    float getMaxFood();
    void setMaxFood(float value);

    // ── Water (Hydration) ───────────────────────────────────────────────
    /** Current water level (0–maxWater). Passive drain −0.3/min + heat biome mult. */
    float getWater();
    void setWater(float value);
    float getMaxWater();
    void setMaxWater(float value);

    // ── Stamina ─────────────────────────────────────────────────────────
    /** Current stamina (0–maxStamina). Sprint −10/s, melee −8–25, mining −5, jump −8. */
    float getStamina();
    void setStamina(float value);
    float getMaxStamina();
    void setMaxStamina(float value);

    /**
     * Whether the player is in stamina exhaustion mode.
     * Set true when stamina hits 0; cleared when stamina recovers above 20%.
     * While exhausted, sprinting is blocked even if the sprint key is held.
     */
    boolean isStaminaExhausted();
    void setStaminaExhausted(boolean exhausted);

    // ── Core Temperature ────────────────────────────────────────────────
    /** Core body temperature in °F. Baseline 70°F. Adjusts ±2°F/sec toward ambient. */
    float getCoreTemperature();
    void setCoreTemperature(float value);

    // ── Debuffs ─────────────────────────────────────────────────────────
    /**
     * Active debuffs mapped by name to remaining duration in ticks.
     * Keys match debuff IDs from spec §1.2:
     * "bleeding", "infection_1", "infection_2", "dysentery",
     * "sprain", "fracture", "concussion", "burn",
     * "hypothermia", "hyperthermia", "radiation", "electrocuted", "stunned"
     *
     * @return unmodifiable view of the debuff map
     */
    Map<String, Integer> getDebuffs();

    /**
     * Add or refresh a debuff. If the debuff already exists, the longer duration wins.
     *
     * @param debuffId id matching spec §1.2
     * @param durationTicks remaining ticks for the debuff
     */
    void addDebuff(String debuffId, int durationTicks);

    /** Remove a debuff (e.g. cured by item). */
    void removeDebuff(String debuffId);

    /** Check if a specific debuff is active. */
    boolean hasDebuff(String debuffId);

    /** Tick all debuffs: decrement durations, remove those that expire. */
    void tickDebuffs();

    // ── Utility ─────────────────────────────────────────────────────────
    /** Copy all values from another stats instance (used on death/respawn). */
    void copyFrom(ISevenDaysPlayerStats other);
}
