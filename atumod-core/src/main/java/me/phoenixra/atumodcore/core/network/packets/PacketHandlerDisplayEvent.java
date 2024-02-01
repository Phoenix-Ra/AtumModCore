package me.phoenixra.atumodcore.core.network.packets;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.core.network.packets.PacketDisplayEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerDisplayEvent implements IMessageHandler<PacketDisplayEvent, IMessage> {
    @Override
    public IMessage onMessage(PacketDisplayEvent message, MessageContext ctx) {
        AtumMod atumMod = AtumAPI.getInstance().getLoadedAtumMod(message.atumModId);
        if(atumMod==null) {
            return null;
        }

        atumMod.getNetworkManager().handleDisplayEvent(
                ctx.getServerHandler().player,
                message.asDisplayEventData()
        );
        return null;
    }
}
