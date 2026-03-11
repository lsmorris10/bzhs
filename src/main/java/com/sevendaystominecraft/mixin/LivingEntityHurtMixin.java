package com.sevendaystominecraft.mixin;

import com.sevendaystominecraft.capability.ModAttachments;
import com.sevendaystominecraft.capability.SevenDaysPlayerStats;
import com.sevendaystominecraft.config.SurvivalConfig;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin into {@link LivingEntity#actuallyHurt(ServerLevel, DamageSource, float)}
 * to trigger debuff rolls when a player takes damage.
 *
 * <h3>Spec §1.2 — Debuff triggers on damage:</h3>
 * <ul>
 *   <li><b>Bleeding</b>: 30% chance per zombie melee hit → −1 HP/3sec until cured</li>
 *   <li><b>Infection Stage 1</b>: 10% base per zombie hit (+5% per feral) → stamina regen −25%</li>
 * </ul>
 *
 * <h3>Mixin target note:</h3>
 * We target {@code LivingEntity.actuallyHurt()} rather than {@code Player.hurt()}
 * because actuallyHurt is called after armor/enchantment reduction, giving us
 * the final damage value. We filter to only affect Player instances.
 *
 * <h3>NeoForge 1.21.4 method signature:</h3>
 * {@code protected void actuallyHurt(ServerLevel level, DamageSource source, float amount)}
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityHurtMixin {

    /**
     * After damage is applied, roll for bleeding and infection debuffs.
     * Uses TAIL injection so the damage has already been processed.
     */
    @Inject(method = "actuallyHurt", at = @At("TAIL"))
    private void sevendaystominecraft$rollDebuffsOnHurt(
            ServerLevel level, DamageSource source, float amount, CallbackInfo ci
    ) {
        // Only apply to Player entities
        if (!((Object) this instanceof Player player)) return;

        // Only process on server side (actuallyHurt is server-only but double-check)
        if (player.level().isClientSide()) return;

        // Don't apply debuffs for non-mob damage (falling, fire, starving, etc.)
        // Check if the damage source has a living entity attacker
        if (source.getEntity() == null || !(source.getEntity() instanceof LivingEntity attacker)) return;

        // Check if our stats are attached
        if (!player.hasData(ModAttachments.PLAYER_STATS.get())) return;

        SevenDaysPlayerStats stats = player.getData(ModAttachments.PLAYER_STATS.get());
        SurvivalConfig cfg = SurvivalConfig.INSTANCE;

        // ── Bleeding Roll (§1.2: 30% chance on zombie melee) ────────────
        // For now, treat all living entity melee attacks as potential bleed sources.
        // When custom zombie entities are added, this should check for zombie type.
        float bleedChance = cfg.bleedingChance.get().floatValue();
        if (player.getRandom().nextFloat() < bleedChance) {
            // Bleeding: lasts until cured (set very long duration — 72000 ticks = 1 hour)
            // Max 3 stacks in spec; we track stacks by incrementing duration
            int currentTicks = stats.getDebuffs().getOrDefault(SevenDaysPlayerStats.DEBUFF_BLEEDING, 0);
            if (currentTicks == 0) {
                stats.addDebuff(SevenDaysPlayerStats.DEBUFF_BLEEDING, 72000); // stack 1
            }
            // Bleeding stacks: max 3 stacks = more frequent damage ticks
            // (handled in PlayerStatsHandler.applyDebuffEffects)
        }

        // ── Infection Roll (§1.2: 10% base, +5% per feral) ─────────────
        // TODO: Check if attacker is a feral zombie and add +5% per feral tier
        float infectionChance = cfg.infectionBaseChance.get().floatValue();
        if (player.getRandom().nextFloat() < infectionChance) {
            // Infection Stage 1: 24 hours in-game = 24000 ticks (1 MC day)
            // If they already have stage 1, don't downgrade, but don't upgrade to 2 here
            if (!stats.hasDebuff(SevenDaysPlayerStats.DEBUFF_INFECTION_1)
                    && !stats.hasDebuff(SevenDaysPlayerStats.DEBUFF_INFECTION_2)) {
                stats.addDebuff(SevenDaysPlayerStats.DEBUFF_INFECTION_1, 24000);
            }
        }
    }
}
