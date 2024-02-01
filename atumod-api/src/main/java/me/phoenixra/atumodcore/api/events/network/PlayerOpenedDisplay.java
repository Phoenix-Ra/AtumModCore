package me.phoenixra.atumodcore.api.events.network;

import me.phoenixra.atumodcore.api.network.data.ActiveDisplayData;
import me.phoenixra.atumodcore.api.network.data.DisplayEventData;
import net.minecraft.entity.player.EntityPlayerMP;

public class PlayerOpenedDisplay extends PlayerDisplayEvent {

    public PlayerOpenedDisplay(EntityPlayerMP playerMP,ActiveDisplayData data) {
        super(playerMP, data, DisplayEventData.EVENT_OPENED);
    }
}
