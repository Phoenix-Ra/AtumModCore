package me.phoenixra.atumodcore.api.display.triggers;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DisplayTriggerRegistry {

    List<DisplayTrigger> compile(@NotNull Config config);

    @Nullable
    DisplayTrigger getTriggerById(@NotNull String id);

    void register(@NotNull String id, @NotNull DisplayTrigger action);
    /**
     * Get the mod.
     *
     * @return The mod instance.
     */
    @NotNull
    AtumMod getAtumMod();
}
