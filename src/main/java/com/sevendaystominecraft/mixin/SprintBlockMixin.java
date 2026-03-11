package com.sevendaystominecraft.mixin;

import com.sevendaystominecraft.capability.ModAttachments;
import com.sevendaystominecraft.capability.SevenDaysPlayerStats;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin into {@link Entity#setSprinting(boolean)} to block sprinting
 * when the player is in stamina exhaustion mode.
 *
 * <h3>Why Entity and not Player?</h3>
 * {@code setSprinting(boolean)} is declared in {@link Entity}, not Player.
 * Mixin can only target methods declared directly on the target class.
 *
 * <h3>Why is this needed?</h3>
 * When a player holds the sprint key (Ctrl/Shift), the client continuously
 * calls setSprinting(true) via input handling. Our server-side tick handler
 * can call setSprinting(false), but the client re-enables it before the
 * next tick. By intercepting the actual setSprinting method, we catch
 * ALL attempts to enable sprinting — including client input packets.
 *
 * <h3>Works on both sides:</h3>
 * This fires on both client and server, intercepting the sprint call
 * at its source regardless of what triggered it.
 */
@Mixin(Entity.class)
public abstract class SprintBlockMixin {

    /**
     * Intercept setSprinting to block it when player is stamina-exhausted.
     * Cancels the call if sprinting=true and the player is exhausted.
     */
    @Inject(method = "setSprinting", at = @At("HEAD"), cancellable = true)
    private void sevendaystominecraft$blockSprintWhenExhausted(boolean sprinting, CallbackInfo ci) {
        // Only intercept attempts to START sprinting
        if (!sprinting) return;

        // Only applies to players
        if (!((Object) this instanceof Player player)) return;

        // Check if our data attachment exists and player is exhausted
        if (player.hasData(ModAttachments.PLAYER_STATS.get())) {
            SevenDaysPlayerStats stats = player.getData(ModAttachments.PLAYER_STATS.get());
            if (stats.isStaminaExhausted()) {
                // Block the sprint — don't let setSprinting(true) go through
                ci.cancel();
            }
        }
    }
}
