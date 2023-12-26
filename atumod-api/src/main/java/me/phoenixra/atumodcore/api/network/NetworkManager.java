package me.phoenixra.atumodcore.api.network;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.network.data.DisplayActionData;
import me.phoenixra.atumodcore.api.network.data.DisplayEventData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;


import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public abstract class NetworkManager {
    @Getter
    private final AtumMod atumMod;
    private final SimpleNetworkWrapper NETWORK_CHANNEL;
    private final AtomicInteger discriminator = new AtomicInteger(1);


    @SideOnly(Side.SERVER)
    private final List<Consumer<DisplayEventData>> displayEventConsumers = new java.util.concurrent.CopyOnWriteArrayList<>();

    public NetworkManager(@NotNull AtumMod atumMod) {
        this.atumMod = atumMod;
        NETWORK_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(
                atumMod.getName().toLowerCase() + "@atumodcore"
        );

    }


   @SideOnly(Side.SERVER)
   public abstract void sendDisplayActionForPlayer(
            @NotNull EntityPlayerMP player,
            @NotNull DisplayActionData data
   );

    @SideOnly(Side.CLIENT)
    public abstract void sendDisplayEvent(
            @NotNull DisplayEventData data
    );




    @SideOnly(Side.SERVER)
    public final void registerDisplayEventConsumer(@NotNull Consumer<DisplayEventData> consumer) {
        displayEventConsumers.add(consumer);
    }

    @SideOnly(Side.SERVER)
    public final void unregisterDisplayEventConsumer(@NotNull Consumer<DisplayEventData> consumer) {
        displayEventConsumers.remove(consumer);
    }

    @SideOnly(Side.SERVER)
    public final void unregisterAllDisplayEventConsumers() {
        displayEventConsumers.clear();
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


    @SideOnly(Side.SERVER)
    protected final void sendToAll(IMessage message) {
        NETWORK_CHANNEL.sendToAll(message);
    }

    @SideOnly(Side.SERVER)
    protected final void sendTo(IMessage message, EntityPlayerMP player) {
        NETWORK_CHANNEL.sendTo(message, player);
    }


    @SideOnly(Side.SERVER)
    protected final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
        NETWORK_CHANNEL.sendToAllAround(message, point);
    }

    @SideOnly(Side.SERVER)
    protected final void sendToAllTracking(IMessage message, NetworkRegistry.TargetPoint point) {
        NETWORK_CHANNEL.sendToAllTracking(message, point);
    }
    @SideOnly(Side.SERVER)
    protected final void sendToAllTracking(IMessage message, Entity entity) {
        NETWORK_CHANNEL.sendToAllTracking(message, entity);
    }

    @SideOnly(Side.SERVER)
    protected final void sendToDimension(IMessage message, int dimensionId) {
        NETWORK_CHANNEL.sendToDimension(message, dimensionId);
    }
    @SideOnly(Side.CLIENT)
    protected final void sendToServer(IMessage message) {
        NETWORK_CHANNEL.sendToServer(message);
    }


    protected final SimpleNetworkWrapper getNetwork() {
        return NETWORK_CHANNEL;
    }
}
