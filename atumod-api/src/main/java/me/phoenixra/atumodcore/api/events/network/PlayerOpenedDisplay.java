package me.phoenixra.atumodcore.api.events.network;

import me.phoenixra.atumodcore.api.network.data.ActiveDisplayData;
import me.phoenixra.atumodcore.api.network.data.DisplayEventData;

public class PlayerOpenedDisplay extends PlayerDisplayEvent {

    public PlayerOpenedDisplay(ActiveDisplayData data) {
        super(data, DisplayEventData.EVENT_OPENED);
    }
}
