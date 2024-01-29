package me.phoenixra.atumodcore.api.display.actions;

import me.phoenixra.atumodcore.api.AtumMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DisplayActionRegistry {

    /**
     * Get an action by id.
     *
     * @param id The id of the action.
     * @return The action.
     */
    @Nullable
    DisplayAction getActionById(@NotNull String id);

    /**
     * Register an action.
     *
     * @param id     The id of the action.
     * @param action The action.
     */
    void register(@NotNull String id, @NotNull DisplayAction action);

    /**
     * Unregister an action.
     *
     * @param id The id of the action.
     */
    void unregister(@NotNull String id);

    /**
     * Get the mod.
     *
     * @return The mod instance.
     */
    @NotNull
    AtumMod getAtumMod();
}
