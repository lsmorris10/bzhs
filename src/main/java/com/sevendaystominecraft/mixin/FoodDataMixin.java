package com.sevendaystominecraft.mixin;

import com.sevendaystominecraft.capability.ModAttachments;
import com.sevendaystominecraft.capability.SevenDaysPlayerStats;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin into {@link FoodData#tick(ServerPlayer)} to disable vanilla hunger entirely.
 *
 * Spec §1.1: "Override FoodData entirely" — all food/saturation/exhaustion
 * logic is replaced by our custom system in {@link com.sevendaystominecraft.capability.PlayerStatsHandler}.
 *
 * <h3>What this does:</h3>
 * <ul>
 *   <li>Cancels the vanilla FoodData.tick() method at HEAD</li>
 *   <li>Prevents vanilla hunger bar depletion, saturation calculations,
 *       and natural regen tied to foodLevel</li>
 *   <li>Our custom drain/regen runs in PlayerTickEvent instead</li>
 * </ul>
 *
 * <h3>MC 1.21.4 note:</h3>
 * In MC 1.21.4, FoodData.tick() takes a {@link ServerPlayer} parameter
 * (not {@link net.minecraft.world.entity.player.Player}). The mixin
 * callback must match this exact signature.
 */
@Mixin(FoodData.class)
public abstract class FoodDataMixin {

    /**
     * Inject at the HEAD of tick() and cancel — all vanilla food logic
     * is replaced by our custom system.
     *
     * We also sync vanilla foodLevel to our custom food percentage
     * so the vanilla hunger bar displays approximately correct.
     *
     * @param player the ServerPlayer (MC 1.21.4 signature)
     * @param ci callback info — cancellable
     */
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void sevendaystominecraft$cancelVanillaTick(ServerPlayer player, CallbackInfo ci) {
        // Bridge: write our food % to vanilla foodLevel so the vanilla HUD bar
        // stays approximately correct until we build custom HUD (§14.1)
        if (player.hasData(ModAttachments.PLAYER_STATS.get())) {
            SevenDaysPlayerStats stats = player.getData(ModAttachments.PLAYER_STATS.get());
            float foodPct = (stats.getMaxFood() > 0)
                    ? stats.getFood() / stats.getMaxFood()
                    : 0f;
            // Vanilla foodLevel is 0–20; map our 0–100% to that range
            int vanillaLevel = Math.round(foodPct * 20f);
            ((FoodData) (Object) this).setFoodLevel(Math.max(0, Math.min(20, vanillaLevel)));
        }

        // Cancel all vanilla hunger logic (saturation, exhaustion, regen)
        ci.cancel();
    }
}
