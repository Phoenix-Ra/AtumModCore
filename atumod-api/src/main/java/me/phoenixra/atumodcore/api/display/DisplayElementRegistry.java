package me.phoenixra.atumodcore.api.display;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DisplayElementRegistry{

    /**
     * Get the canvas which is prepared for drawing.
     * This method basically clones the canvas template and
     * changes its id to the usable one.
     *
     * @param id The id of the canvas template.
     * @return The ready-to-use canvas.
     */
    @Nullable
    DisplayCanvas getDrawableCanvas(@NotNull String id);

    /**
     * Get the element template.
     * Caution! It is not prepared for drawing.
     * @param id The id of the element template.
     * @return The element template.
     */
    @Nullable
    DisplayElement getElementTemplate(@NotNull String id);
    /**
     * Get the canvas template.
     * Caution! It is not prepared for drawing.
     * @param id The id of the canvas template.
     * @return The canvas template.
     */
    @Nullable
    DisplayCanvas getCanvasTemplate(@NotNull String id);

    DisplayCanvas compileCanvasTemplate(@NotNull String id,
                                        @NotNull Config config);


    void registerTemplate(@NotNull String id, @NotNull DisplayElement chain);
    void unregisterTemplate(@NotNull String id);
    /**
     * Get the mod.
     *
     * @return The mod instance.
     */
    @NotNull
    AtumMod getAtumMod();
}
