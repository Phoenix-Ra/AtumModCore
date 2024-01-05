package me.phoenixra.atumodcore.api.events.data;

import lombok.Getter;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import net.minecraftforge.fml.common.eventhandler.Event;
public class DisplayDataChangedEvent extends Event{

    @Getter
    private final DisplayRenderer displayRenderer;
    @Getter
    private final String dataId;
    @Getter
    private final String value;

    public DisplayDataChangedEvent(DisplayRenderer displayRenderer, String dataId, String value) {
        this.displayRenderer = displayRenderer;
        this.dataId = dataId;
        this.value = value;
    }
}
