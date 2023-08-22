package me.phoenixra.atumodcore.api.display;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DisplayElementRegistry{


    @Nullable
    DisplayElement getElementById(@NotNull String id);

    DisplayCanvas compile(@NotNull Config config);


    void register(@NotNull String id, @NotNull DisplayElement chain);
    /**
     * Get the mod.
     *
     * @return The mod instance.
     */
    @NotNull
    AtumMod getAtumMod();
}
