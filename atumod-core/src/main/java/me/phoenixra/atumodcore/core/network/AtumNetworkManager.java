package me.phoenixra.atumodcore.core.network;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.network.NetworkManager;
import me.phoenixra.atumodcore.api.network.data.DisplayActionData;
import me.phoenixra.atumodcore.api.network.data.DisplayEventData;
import me.phoenixra.atumodcore.core.network.packets.PacketDisplayEvent;
import me.phoenixra.atumodcore.core.network.packets.PacketHandlerPerformDisplayAction;
import me.phoenixra.atumodcore.core.network.packets.PacketPerformDisplayAction;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

public class AtumNetworkManager extends NetworkManager {
    private AtumMod atumMod;
    public AtumNetworkManager(AtumMod atumMod) {
        super(atumMod);


        registerMessage(PacketHandlerPerformDisplayAction.class,
                PacketPerformDisplayAction.class,
                Side.CLIENT
        );
    }

    @Override
    public void sendDisplayActionForPlayer(@NotNull EntityPlayerMP player, @NotNull DisplayActionData data) {
        sendTo(new PacketPerformDisplayAction(data), player);
    }

    @Override
    public void sendDisplayEvent(@NotNull DisplayEventData data) {
        sendToServer(new PacketDisplayEvent(data));
    }
}
