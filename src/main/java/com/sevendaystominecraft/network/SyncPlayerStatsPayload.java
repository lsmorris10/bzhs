package com.sevendaystominecraft.network;

import java.util.HashMap;
import java.util.Map;

import com.sevendaystominecraft.SevenDaysToMinecraft;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Network payload for syncing player stats from server → client.
 *
 * Sent manually via PacketDistributor.sendToPlayer() from PlayerStatsHandler.
 *
 * @param food             current food value
 * @param maxFood          max food value
 * @param water            current water value
 * @param maxWater         max water value
 * @param stamina          current stamina value
 * @param maxStamina       max stamina value
 * @param staminaExhausted whether sprint is blocked (exhaustion mode)
 * @param coreTemp         core body temperature in °F
 * @param debuffs          active debuffs (id → remaining ticks)
 */
public record SyncPlayerStatsPayload(
        float food,
        float maxFood,
        float water,
        float maxWater,
        float stamina,
        float maxStamina,
        boolean staminaExhausted,
        float coreTemp,
        Map<String, Integer> debuffs
) implements CustomPacketPayload {

    public static final Type<SyncPlayerStatsPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(SevenDaysToMinecraft.MOD_ID, "sync_player_stats")
    );

    /**
     * StreamCodec for encoding/decoding this payload on the network.
     */
    public static final StreamCodec<ByteBuf, SyncPlayerStatsPayload> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public SyncPlayerStatsPayload decode(ByteBuf buf) {
                    float food = buf.readFloat();
                    float maxFood = buf.readFloat();
                    float water = buf.readFloat();
                    float maxWater = buf.readFloat();
                    float stamina = buf.readFloat();
                    float maxStamina = buf.readFloat();
                    boolean staminaExhausted = buf.readBoolean();
                    float coreTemp = buf.readFloat();

                    // Read debuff map
                    int count = ByteBufCodecs.VAR_INT.decode(buf);
                    Map<String, Integer> debuffs = new HashMap<>();
                    for (int i = 0; i < count; i++) {
                        String id = ByteBufCodecs.STRING_UTF8.decode(buf);
                        int ticks = ByteBufCodecs.VAR_INT.decode(buf);
                        debuffs.put(id, ticks);
                    }

                    return new SyncPlayerStatsPayload(food, maxFood, water, maxWater,
                            stamina, maxStamina, staminaExhausted, coreTemp, debuffs);
                }

                @Override
                public void encode(ByteBuf buf, SyncPlayerStatsPayload payload) {
                    buf.writeFloat(payload.food);
                    buf.writeFloat(payload.maxFood);
                    buf.writeFloat(payload.water);
                    buf.writeFloat(payload.maxWater);
                    buf.writeFloat(payload.stamina);
                    buf.writeFloat(payload.maxStamina);
                    buf.writeBoolean(payload.staminaExhausted);
                    buf.writeFloat(payload.coreTemp);

                    // Write debuff map
                    ByteBufCodecs.VAR_INT.encode(buf, payload.debuffs.size());
                    for (Map.Entry<String, Integer> entry : payload.debuffs.entrySet()) {
                        ByteBufCodecs.STRING_UTF8.encode(buf, entry.getKey());
                        ByteBufCodecs.VAR_INT.encode(buf, entry.getValue());
                    }
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
