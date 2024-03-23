package me.phoenixra.atumodcore.api.network;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.events.network.PlayerClosedDisplay;
import me.phoenixra.atumodcore.api.events.network.PlayerDisplayEvent;
import me.phoenixra.atumodcore.api.events.network.PlayerOpenedDisplay;
import me.phoenixra.atumodcore.api.network.data.ActiveDisplayData;
import me.phoenixra.atumodcore.api.network.data.DisplayActionData;
import me.phoenixra.atumodcore.api.network.data.DisplayEventData;
import me.phoenixra.atumodcore.api.utils.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages the network of the mod.
 * <p>It is recommended to use this class to work with packets</p>
 *
 * STILL IN DEVELOPMENT THE DISPLAY NETWORK SYSTEM
 */
public abstract class NetworkManager {
    @Getter
    private final AtumMod atumMod;
    private SimpleNetworkWrapper NETWORK_CHANNEL;
    private final AtomicInteger discriminator = new AtomicInteger(1);


    @SideOnly(Side.SERVER)
    private Map<EntityPlayerMP, List<ActiveDisplayData>> openedDisplays;

    public NetworkManager(@NotNull AtumMod atumMod) {
        this.atumMod = atumMod;


    }
    protected void initNetwork(){
        NETWORK_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(
                atumMod.getName() + "@atumodcore"
        );
        if(FMLCommonHandler.instance().getSide() == Side.SERVER) {
            openedDisplays = new ConcurrentHashMap<>();
        }
    }

    /**
     *  Send a display action to player.
     *  <br>
     *  If you want to perform action that do not require the
     *  element, for example play_sound, you can pass null
     *  or empty string as element id.
     *  <br>
     * @param player the player
     * @param data   the display action data
     */
   @SideOnly(Side.SERVER)
   public abstract void sendDisplayActionForPlayer(
            @NotNull EntityPlayerMP player,
            @NotNull DisplayActionData data
   );


   /**
        * Send a display event to player.
        * <br>
        * You can also send display events via action "send_display_event@{event_id}"
        * <br>
        * @param data the display event data
        */
    @SideOnly(Side.CLIENT)
    public abstract void sendDisplayEvent(
            @NotNull DisplayEventData data
    );


    @SideOnly(Side.SERVER)
    public List<ActiveDisplayData> getOpenedRenderersForPlayer(
            @NotNull EntityPlayerMP player
    ){
        return openedDisplays.getOrDefault(player, new ArrayList<>());
    }

    @SideOnly(Side.SERVER)
    public void clearOpenedRenderersForPlayer(
            @NotNull EntityPlayerMP player
    ){
        openedDisplays.remove(player);
    }
    @SideOnly(Side.SERVER)
    public void handleDisplayEvent(
            @NotNull EntityPlayerMP player,
            @NotNull DisplayEventData data
    ){
        if(data.getEventId() == DisplayEventData.EVENT_OPENED) {
            List<ActiveDisplayData> list = openedDisplays.getOrDefault(player, new ArrayList<>());
            ActiveDisplayData pair = new ActiveDisplayData(
                    data.getRendererId(),
                    data.getElementId()
            );
            list.add(pair);
            openedDisplays.put(player, list);
            MinecraftForge.EVENT_BUS.post(new PlayerOpenedDisplay(player, pair));
        } else if(data.getEventId() == DisplayEventData.EVENT_CLOSED) {
            List<ActiveDisplayData> list = openedDisplays.get(player);
            ActiveDisplayData pair = new ActiveDisplayData(
                    data.getRendererId(),
                    data.getElementId()
            );
            if(list == null ||
                    !list.contains(pair)) {
                //ignore unrecognized canvas events
                return;
            }
            list.remove(pair);
            openedDisplays.put(player, list);
            MinecraftForge.EVENT_BUS.post(new PlayerClosedDisplay(player, pair));
        }else {
            List<ActiveDisplayData> list = openedDisplays.get(player);
            for(ActiveDisplayData pair : list){
                if(pair.getRendererId() == data.getRendererId()){
                    MinecraftForge.EVENT_BUS.post(
                            new PlayerDisplayEvent(
                                    player,
                                    pair,
                                    data.getElementId(),
                                    data.getEventId()
                            )
                    );
                    break;
                }
            }
        }
    }


    /**
     * Register a packet message.
     *
     * @param messageHandler     The message handler.
     * @param requestMessageType The request message type.
     * @param side               The side.
     * @param <REQUEST>          The request message type.
     * @param <REPLY>            The reply message type.
     */
    public final  <REQUEST extends IMessage, REPLY extends IMessage> void registerMessage(
            Class<? extends IMessageHandler<REQUEST, REPLY>> messageHandler,
            Class<REQUEST> requestMessageType,
            Side side
    ) {
        NETWORK_CHANNEL.registerMessage(messageHandler, requestMessageType, discriminator.incrementAndGet(), side);
    }

    /**
     * Construct a minecraft packet from the supplied message.
     * Can be used where minecraft packets are required
     *
     * @param message The message to translate into packet form
     * @return A minecraft {@link Packet} suitable for use in minecraft APIs
     */
    public final Packet<?> getPacketFrom(IMessage message) {
        return NETWORK_CHANNEL.getPacketFrom(message);
    }

    /**
     * Send this message to the specified player.
     * The {@link IMessageHandler} for this message type
     * should be on the CLIENT side.
     *
     * @param message The message to send
     * @param player The player to send it to
     */
    @SideOnly(Side.SERVER)
    public final void sendTo(IMessage message, EntityPlayerMP player) {
        NETWORK_CHANNEL.sendTo(message, player);
    }

    /**
     * Send this message to everyone.
     * The {@link IMessageHandler} for this message type
     * should be on the CLIENT side.
     *
     * @param message The message to send
     */
    @SideOnly(Side.SERVER)
    public final void sendToAll(IMessage message) {
        NETWORK_CHANNEL.sendToAll(message);
    }

    /**
     * Send this message to everyone within a certain range of a point.
     * The {@link IMessageHandler} for this message type
     * should be on the CLIENT side.
     *
     * @param message The message to send
     * @param point The {@link NetworkRegistry.TargetPoint} around which to send
     */
    @SideOnly(Side.SERVER)
    public final void sendToAllAround(IMessage message,
                                         NetworkRegistry.TargetPoint point) {
        NETWORK_CHANNEL.sendToAllAround(message, point);
    }

    /**
     * Sends this message to everyone tracking a point.
     * The {@link IMessageHandler} for this message type should be on the CLIENT side.
     * The {@code range} field of the {@link NetworkRegistry.TargetPoint} is ignored.
     *
     * @param message The message to send
     * @param point The tracked {@link NetworkRegistry.TargetPoint} around which to send
     */
    @SideOnly(Side.SERVER)
    public final void sendToAllTracking(IMessage message,
                                           NetworkRegistry.TargetPoint point) {
        NETWORK_CHANNEL.sendToAllTracking(message, point);
    }

    /**
     * Sends this message to everyone tracking an entity.
     * The {@link IMessageHandler} for this message type should be on the CLIENT side.
     * This is not equivalent to {@link #sendToAllTracking(IMessage, NetworkRegistry.TargetPoint)}
     * because entities have different tracking distances based on their type.
     *
     * @param message The message to send
     * @param entity The tracked entity around which to send
     */
    @SideOnly(Side.SERVER)
    public final void sendToAllTracking(IMessage message,
                                           Entity entity) {
        NETWORK_CHANNEL.sendToAllTracking(message, entity);
    }

    /**
     * Send this message to everyone within the supplied dimension.
     * The {@link IMessageHandler} for this message type should be on the CLIENT side.
     *
     * @param message The message to send
     * @param dimensionId The dimension id to target
     */
    @SideOnly(Side.SERVER)
    public final void sendToDimension(IMessage message,
                                         int dimensionId) {
        NETWORK_CHANNEL.sendToDimension(message, dimensionId);
    }

    /**
     * Send this message to the server.
     * The {@link IMessageHandler} for this message type should
     * be on the SERVER side.
     *
     * @param message The message to send
     */
    @SideOnly(Side.CLIENT)
    public final void sendToServer(IMessage message) {
        if(!PlayerUtils.isInMultiplayer()) return;
        System.out.println("Sending to server: " + message.getClass().getSimpleName());
        NETWORK_CHANNEL.sendToServer(message);
    }

    /**
     * Get the network channel.
     *
     * @return The network channel.
     */
    public final SimpleNetworkWrapper getNetwork() {
        return NETWORK_CHANNEL;
    }
}
