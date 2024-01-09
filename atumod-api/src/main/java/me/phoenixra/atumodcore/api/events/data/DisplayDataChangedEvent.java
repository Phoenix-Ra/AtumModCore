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
    @Getter
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
