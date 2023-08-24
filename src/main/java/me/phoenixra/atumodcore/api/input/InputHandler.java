package me.phoenixra.atumodcore.api.input;

import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import me.phoenixra.atumodcore.api.input.event.InputReleaseEvent;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface InputHandler {


    void setCursorType(@NotNull CursorType cursor);

    PairRecord<Integer,Integer> getMousePosition();

    void blockInput();
    void unblockInput();
    boolean isInputBlocked();
    void addListenerOnPress(String id, Consumer<InputPressEvent> event);
    void addListenerOnRelease(String id, Consumer<InputReleaseEvent> event);
    void removeListenerOnPress(String id);
    void removeListenerOnRelease(String id);

}
