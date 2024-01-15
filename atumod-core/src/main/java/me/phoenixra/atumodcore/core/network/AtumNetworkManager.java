package me.phoenixra.atumodcore.core.network;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.network.NetworkManager;
import me.phoenixra.atumodcore.api.network.data.DisplayActionData;
import me.phoenixra.atumodcore.api.network.data.DisplayEventData;
import me.phoenixra.atumodcore.api.service.AtumModService;
import me.phoenixra.atumodcore.core.network.packets.PacketDisplayEvent;
import me.phoenixra.atumodcore.core.network.packets.PacketHandlerDisplayEvent;
import me.phoenixra.atumodcore.core.network.packets.PacketHandlerPerformDisplayAction;
import me.phoenixra.atumodcore.core.network.packets.PacketPerformDisplayAction;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AtumNetworkManager extends NetworkManager implements AtumModService {
    private AtumMod atumMod;

    public AtumNetworkManager(AtumMod atumMod) {
        super(atumMod);
        atumMod.provideModService(this);
    }
    @Override
    public void handleFmlEvent(@NotNull FMLEvent event) {
        if(event instanceof FMLPreInitializationEvent){
            initNetwork();
            registerMessage(PacketHandlerPerformDisplayAction.class,
                    PacketPerformDisplayAction.class,
                    Side.CLIENT
            );
            registerMessage(PacketHandlerDisplayEvent.class,
                    PacketDisplayEvent.class,
                    Side.SERVER
            );
        }
    }
    @Override
    public void sendDisplayActionForPlayer(@NotNull EntityPlayerMP player, @NotNull DisplayActionData data) {
        sendTo(new PacketPerformDisplayAction(data), player);
    }

    @Override
    public void sendDisplayEvent(@NotNull DisplayEventData data) {
        sendToServer(new PacketDisplayEvent(data));
    }


    @Override
    public void onRemove() {

    }

    @Override
    public @NotNull String getId() {
        return "network";
    }
}
