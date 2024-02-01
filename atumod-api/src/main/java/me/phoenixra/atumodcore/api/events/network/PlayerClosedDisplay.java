package me.phoenixra.atumodcore.api.events.network;

import me.phoenixra.atumodcore.api.network.data.ActiveDisplayData;
import me.phoenixra.atumodcore.api.network.data.DisplayEventData;

public class PlayerClosedDisplay extends PlayerDisplayEvent {

    public PlayerClosedDisplay(ActiveDisplayData data) {
        super(data, DisplayEventData.EVENT_CLOSED);
    }

}
