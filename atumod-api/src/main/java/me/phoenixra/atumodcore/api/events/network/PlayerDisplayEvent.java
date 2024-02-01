package me.phoenixra.atumodcore.api.events.network;

import lombok.Getter;
import me.phoenixra.atumodcore.api.network.data.ActiveDisplayData;
import net.minecraftforge.fml.common.eventhandler.Event;
@Getter
public class PlayerDisplayEvent extends Event {
    private final ActiveDisplayData data;
    private final String elementId;
    private final int eventId;

    public PlayerDisplayEvent(ActiveDisplayData data,
                              String elementId,
                              int eventId) {
        this.data = data;
        this.eventId = eventId;
        this.elementId = elementId;
    }
    public PlayerDisplayEvent(ActiveDisplayData data,
                              int eventId) {
        this(data, data.getBaseCanvasId(), eventId);
    }
}
