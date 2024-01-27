package me.phoenixra.atumodcore.api.events.display;

import lombok.Getter;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.input.InputType;
import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import me.phoenixra.atumodcore.api.input.event.InputReleaseEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when mouse is released on a display element.
 */
public class ElementInputReleaseEvent extends Event {
    @Getter
    private final DisplayElement clickedElement;
    @Getter
    private final InputReleaseEvent parentEvent;

    public ElementInputReleaseEvent(DisplayElement element, InputReleaseEvent releaseEvent) {
        this.clickedElement = element;
        this.parentEvent = releaseEvent;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}
