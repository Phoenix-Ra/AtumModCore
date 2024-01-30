package me.phoenixra.atumodcore.api.events.data;

import lombok.Getter;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when a display data is changed.
 */
@Getter
public class DisplayDataChangedEvent extends Event{

    private final DisplayRenderer displayRenderer;
    private final String dataId;
    private final String value;
    private final ChangeType changeType;

    public DisplayDataChangedEvent(DisplayRenderer displayRenderer,
                                   String dataId, String value,
                                   ChangeType changeType) {
        this.displayRenderer = displayRenderer;
        this.dataId = dataId;
        this.value = value;
        this.changeType = changeType;
    }

    public enum ChangeType {
        SET,
        SET_TEMPORARY,
        SET_DEFAULT_BACK;
    }
}
