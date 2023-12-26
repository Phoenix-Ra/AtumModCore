package me.phoenixra.atumodcore.core.network.packets;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerDisplayEvent implements IMessageHandler<PacketDisplayEvent, IMessage> {
    @Override
    public IMessage onMessage(PacketDisplayEvent message, MessageContext ctx) {
        return null;
    }
}
