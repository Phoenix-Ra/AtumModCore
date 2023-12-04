package me.phoenixra.atumodcore.api.display.actions;

import me.phoenixra.atumodcore.api.AtumMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DisplayActionRegistry {
    @Nullable
    DisplayAction getActionById(@NotNull String id);

    void register(@NotNull String id, @NotNull DisplayAction action);
    void unregister(@NotNull String id);
    /**
     * Get the mod.
     *
     * @return The mod instance.
     */
    @NotNull
    AtumMod getAtumMod();
}
