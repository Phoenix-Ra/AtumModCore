package me.phoenixra.atumodcore.api.events.display;

import lombok.Getter;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.events.input.InputReleaseEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when mouse is released on a display element.
 */
@Getter
public class ElementInputReleaseEvent extends Event {
    private final DisplayElement clickedElement;
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
