package com.github.thedeathlycow.thermoo.impl;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class WetComponent implements EnvironmentComponent, AutoSyncedComponent {

    private static final String WETNESS_KEY = "wetness";

    private int wetness = 0;

    private final LivingEntity provider;

    public WetComponent(LivingEntity provider) {
        this.provider = provider;
    }

    @Override
    public int getValue() {
        return this.wetness;
    }

    @Override
    public void setValue(int value) {
        if (this.wetness != value) {
            this.wetness = value;
            ThermooComponents.WETNESS.sync(this.provider);
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        if (tag.contains(WETNESS_KEY, NbtElement.INT_TYPE)) {
            this.wetness = tag.getInt(WETNESS_KEY);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        if (this.wetness > 0) {
            tag.putInt(WETNESS_KEY, this.wetness);
        }
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeVarInt(this.wetness);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.wetness = buf.readVarInt();
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        final BlockPos providerPos = this.provider.getBlockPos();
        return player == this.provider
                || providerPos.isWithinDistance(player.getSyncedPos(), EnvironmentComponent.SYNC_DISTANCE);
    }
}
