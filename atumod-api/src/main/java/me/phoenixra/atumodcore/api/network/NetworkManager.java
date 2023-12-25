package me.phoenixra.atumodcore.api.network;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.network.packets.PacketPerformDisplayAction;
import me.phoenixra.atumodcore.api.network.packets.PacketHandlerPerformDisplayAction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;


import java.util.concurrent.atomic.AtomicInteger;

public abstract class NetworkManager {
    @Getter
    private final AtumMod atumMod;
    private final SimpleNetworkWrapper NETWORK_CHANNEL;
    private final AtomicInteger discriminator = new AtomicInteger(1);


    public NetworkManager(@NotNull AtumMod atumMod) {
        this.atumMod = atumMod;
        NETWORK_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(
                atumMod.getName().toLowerCase() + "@atumodcore"
        );

        registerMessage(PacketHandlerPerformDisplayAction.class,
                PacketPerformDisplayAction.class,
                Side.CLIENT
        );

    }




    protected final  <REQUEST extends IMessage, REPLY extends IMessage> void registerMessage(
            Class<? extends IMessageHandler<REQUEST, REPLY>> messageHandler,
            Class<REQUEST> requestMessageType,
            Side side
    ) {
        NETWORK_CHANNEL.registerMessage(messageHandler, requestMessageType, discriminator.incrementAndGet(), side);
    }


    protected final Packet<?> getPacketFrom(IMessage message) {
        return NETWORK_CHANNEL.getPacketFrom(message);
    }


    protected final void sendToAll(IMessage message) {
        NETWORK_CHANNEL.sendToAll(message);
    }


    protected final void sendTo(IMessage message, EntityPlayerMP player) {
        NETWORK_CHANNEL.sendTo(message, player);
    }


    protected final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
        NETWORK_CHANNEL.sendToAllAround(message, point);
    }

    protected final void sendToAllTracking(IMessage message, NetworkRegistry.TargetPoint point) {
        NETWORK_CHANNEL.sendToAllTracking(message, point);
    }

    protected final void sendToAllTracking(IMessage message, Entity entity) {
        NETWORK_CHANNEL.sendToAllTracking(message, entity);
    }


    protected final void sendToDimension(IMessage message, int dimensionId) {
        NETWORK_CHANNEL.sendToDimension(message, dimensionId);
    }

    protected final void sendToServer(IMessage message) {
        NETWORK_CHANNEL.sendToServer(message);
    }


    protected final SimpleNetworkWrapper getNetwork() {
        return NETWORK_CHANNEL;
    }
}
