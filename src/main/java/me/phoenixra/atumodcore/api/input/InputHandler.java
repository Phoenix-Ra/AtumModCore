package me.phoenixra.atumodcore.api.input;

import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import me.phoenixra.atumodcore.api.input.event.InputReleaseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface InputHandler {


    void setCursorType(@NotNull CursorType cursor);


    void blockInput();
    void unblockInput();
    boolean isInputBlocked();
    void addListenerOnPress(Consumer<InputPressEvent> event);
    void addListenerOnRelease(Consumer<InputReleaseEvent> event);


}
