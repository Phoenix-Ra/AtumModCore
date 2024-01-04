package me.phoenixra.atumodcore.core.network.packets;

import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerDisplayEvent implements IMessageHandler<PacketDisplayEvent, IMessage> {
    @Override
    public IMessage onMessage(PacketDisplayEvent message, MessageContext ctx) {
        String atumModName =
                ctx.getClientHandler().getNetworkManager().channel()
                        .id().asShortText().split("@")[0];
        System.out.println("ATUM MOD NAME FROM PACKET: " + atumModName);
        AtumMod atumMod = AtumAPI.getInstance().getLoadedAtumMod(atumModName);
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
