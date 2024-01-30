package me.phoenixra.atumodcore.api.input;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import java.io.InputStream;


public interface InputHandler {


    /**
     * Set the cursor type.
     * @param cursor the cursor type
     */
    void setCursorType(@NotNull CursorType cursor);

    /**
     * Load the custom cursor object.
     * @param resource the resource location of the cursor
     * @param width the width of the cursor  (def: 32)
     * @param height the height of the cursor  (def: 32)
     * @param xHotspot the x hotspot  (def: 16)
     * @param yHotspot the y hotspot  (def: 16)
     * @return the cursor object
     */
    @Nullable Cursor loadCursor(InputStream resource,
                                int width, int height,
                                int xHotspot, int yHotspot);

    /**
     * Load and apply the custom cursor object.
     *
     * @param resource the resource location of the cursor
     * @param width the width of the cursor  (def: 32)
     * @param height the height of the cursor  (def: 32)
     * @param xHotspot the x hotspot  (def: 16)
     * @param yHotspot the y hotspot  (def: 16)
     */
     default void loadAndApplyCursor(InputStream resource,
                                   int width, int height,
                                   int xHotspot, int yHotspot) throws LWJGLException {
         Cursor cursor = loadCursor(resource, width, height, xHotspot, yHotspot);
         if(cursor != null) Mouse.setNativeCursor(cursor);
     }


    /**
     * Get the mouse position, affected by the scale factor.
     * @return the mouse position
     */
    @NotNull
    PairRecord<Integer,Integer> getMousePosition();

    /**
     * Get the mouse position not affected by the scale factor.
     * @return the mouse position
     */
    @NotNull
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

    /**
     * Get the mod that owns this input handler.
     * @return the mod instance
     */
    @NotNull AtumMod getAtumMod();

}
