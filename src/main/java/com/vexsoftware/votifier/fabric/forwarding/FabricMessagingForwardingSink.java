package com.vexsoftware.votifier.fabric.forwarding;

import com.vexsoftware.votifier.fabric.NuVotifier;
import com.vexsoftware.votifier.support.forwarding.AbstractPluginMessagingForwardingSink;
import com.vexsoftware.votifier.support.forwarding.ForwardedVoteListener;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class FabricMessagingForwardingSink extends AbstractPluginMessagingForwardingSink implements ServerPlayNetworking.PlayPayloadHandler<PluginMessagePayload> {

    private final String channel;

    private final CustomPacketPayload.Type<PluginMessagePayload> type;

    public FabricMessagingForwardingSink(String channel, ForwardedVoteListener listener) {
        super(listener);
        this.channel = channel;
        this.type = CustomPacketPayload.createType(channel);
        PayloadTypeRegistry.playC2S().register(type, PluginMessagePayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(type, this);
    }

    @Override
    public void halt() {
        ServerPlayNetworking.unregisterGlobalReceiver(new ResourceLocation(channel));
    }

    public void receive(PluginMessagePayload payload, ServerPlayNetworking.Context context) {
        try {
            this.handlePluginMessage(payload.data());
        } catch (Exception e) {
            NuVotifier.LOGGER.error("There was an unknown error when processing a forwarded vote.", e);
        }
    }

    public CustomPacketPayload.Type<PluginMessagePayload> type() {
        return type;
    }
}
