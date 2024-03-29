package me.phoenixra.atumodcore.api.display;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.actions.DisplayActionRegistry;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTriggerRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DisplayManager{



    /**
     * Get the element registry.
     *
     * @return The element registry.
     */
    @NotNull DisplayElementRegistry getElementRegistry();


    /**
     * Get the action registry.
     *
     * @return The action registry.
     */
    @NotNull DisplayActionRegistry getActionRegistry();

    /**
     * Get the trigger registry.
     *
     * @return The trigger registry.
     */
    @NotNull DisplayTriggerRegistry getTriggerRegistry();

    /**
     * Generate a new renderer id.
     * @return The id.
     */
    int generateRendererId();

    /**
     * Set HUD canvas.
     * <p>Use it to make your own HUD</p>
     *
     * @param canvas The canvas.
     */
    void setHUDCanvas(@NotNull DisplayCanvas canvas);

    /**
     * Get the HUD canvas currently used.
     *
     * @return The HUD canvas.
     */
    @NotNull DisplayCanvas getHUDCanvas();

    /**
     * Get the renderer
     *
     * @param id The id of the canvas.
     * @return The enabled canvas or null
     */
    @Nullable DisplayRenderer getRenderer(int id);

    /**
     * Register the renderer
     *
     * @param id The id.
     * @param renderer The renderer.
     */
    void registerRenderer(int id,
                          @NotNull DisplayRenderer renderer);

    /**
     * Unregister the renderer.
     *
     * @param id The id.
     */
    void unregisterRenderer(int id);

    /**
     * Get the mod.
     *
     * @return The mod instance.
     */
    @NotNull
    AtumMod getAtumMod();
}
