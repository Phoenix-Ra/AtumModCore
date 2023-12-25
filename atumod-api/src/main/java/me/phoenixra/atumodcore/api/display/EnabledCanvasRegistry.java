package me.phoenixra.atumodcore.api.display;

import me.phoenixra.atumodcore.api.AtumMod;
import org.jetbrains.annotations.NotNull;

public interface EnabledCanvasRegistry {

    DisplayCanvas getCanvasById(@NotNull String id);

    void registerCanvas(@NotNull String id, @NotNull DisplayCanvas canvas);
    void unregisterCanvas(@NotNull String id);
    /**
     * Get the mod.
     *
     * @return The mod instance.
     */
    @NotNull
    AtumMod getAtumMod();
}
