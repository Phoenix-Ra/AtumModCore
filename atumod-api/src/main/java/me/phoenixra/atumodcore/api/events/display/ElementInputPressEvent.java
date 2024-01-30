package me.phoenixra.atumodcore.api.events.display;

import lombok.Getter;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.events.input.InputPressEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when a display element is clicked.
 */
@Getter
public class ElementInputPressEvent extends Event {
    private final DisplayElement clickedElement;
    private final InputPressEvent parentEvent;

    public ElementInputPressEvent(DisplayElement element, InputPressEvent pressEvent) {
        this.clickedElement = element;
        this.parentEvent = pressEvent;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}
