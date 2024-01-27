package me.phoenixra.atumodcore.api.input;

import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import me.phoenixra.atumodcore.api.input.event.InputReleaseEvent;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

//@TODO has to be modified, the events are weirdly implemented
public interface InputHandler {


    void setCursorType(@NotNull CursorType cursor);


    PairRecord<Integer,Integer> getMousePosition();
    PairRecord<Integer,Integer> getMouseOriginPosition();

    /**
     * Block input from being processed.
     */
    void blockInput();

    /**
     * Unblock input from being processed.
     */
    void unblockInput();

    /**
     * Check if input is blocked.
     * @return true if input is blocked, false otherwise.
     */
    boolean isInputBlocked();

    void addListenerOnPress(String id, Consumer<InputPressEvent> event);
    void addListenerOnRelease(String id, Consumer<InputReleaseEvent> event);
    void removeListenerOnPress(String id);
    void removeListenerOnRelease(String id);

}
