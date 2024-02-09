package com.vexsoftware.votifier.fabric.forwarding;

import com.vexsoftware.votifier.fabric.NuVotifier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

class PluginMessagePayload implements CustomPacketPayload {

    public static final StreamCodec<RegistryFriendlyByteBuf, PluginMessagePayload> CODEC = StreamCodec.ofMember(PluginMessagePayload::write, PluginMessagePayload::new).cast();

    private final byte[] data;

    private PluginMessagePayload(FriendlyByteBuf buf) {
        this.data = new byte[buf.readableBytes()];
        buf.readBytes(this.data);
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeBytes(this.data);
    }

    public byte[] data() {
        return data;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return NuVotifier.getInstance().getForwardingMethod().type();
    }
}
