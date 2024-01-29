package me.phoenixra.atumodcore.api.display;

import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.display.actions.DisplayActionRegistry;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.triggers.DisplayTriggerRegistry;
import me.phoenixra.atumodcore.api.service.AtumModService;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     * Get the enabled canvas.
     *
     * @param id The id of the canvas.
     * @return The enabled canvas or null
     */
    @Nullable DisplayCanvas getEnabledCanvas(@NotNull String id);

    /**
     * Register an enabled canvas.
     *
     * @param id The id of the canvas.
     * @param canvas The canvas.
     */
    void registerEnabledCanvas(@NotNull String id,
                               @NotNull DisplayCanvas canvas);

    /**
     * Unregister an enabled canvas.
     *
     * @param id The id of the canvas.
     */
    void unregisterEnabledCanvas(@NotNull String id);

    /**
     * Get the mod.
     *
     * @return The mod instance.
     */
    @NotNull
    AtumMod getAtumMod();
}
