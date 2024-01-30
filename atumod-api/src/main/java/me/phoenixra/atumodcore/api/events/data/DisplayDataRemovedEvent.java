package me.phoenixra.atumodcore.api.events.data;

import lombok.Getter;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import net.minecraftforge.fml.common.eventhandler.Event;
/**
 * Called when a display data is removed
 */
@Getter
public class DisplayDataRemovedEvent extends Event {
    private final DisplayRenderer displayRenderer;
    private final String dataId;

    public DisplayDataRemovedEvent(DisplayRenderer displayRenderer, String dataId) {
        this.displayRenderer = displayRenderer;
        this.dataId = dataId;
    }

}
