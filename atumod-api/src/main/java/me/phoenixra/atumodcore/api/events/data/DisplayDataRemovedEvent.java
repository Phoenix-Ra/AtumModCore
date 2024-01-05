package me.phoenixra.atumodcore.api.events.data;

import lombok.Getter;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class DisplayDataRemovedEvent extends Event {
    @Getter
    private final DisplayRenderer displayRenderer;
    @Getter
    private final String dataId;

    public DisplayDataRemovedEvent(DisplayRenderer displayRenderer, String dataId) {
        this.displayRenderer = displayRenderer;
        this.dataId = dataId;
    }

}
