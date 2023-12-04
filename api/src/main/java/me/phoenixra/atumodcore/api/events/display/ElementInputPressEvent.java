package me.phoenixra.atumodcore.api.events.display;

import lombok.Getter;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.input.InputType;
import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ElementInputPressEvent extends Event {
    @Getter
    private final DisplayElement clickedElement;
    @Getter
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
