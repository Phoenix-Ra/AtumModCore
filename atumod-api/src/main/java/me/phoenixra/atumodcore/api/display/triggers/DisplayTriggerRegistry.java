package me.phoenixra.atumodcore.api.display.triggers;

import me.phoenixra.atumodcore.api.AtumMod;
import org.jetbrains.annotations.NotNull;

public interface DisplayTriggerRegistry {


    void registerTemplate(@NotNull String id, @NotNull DisplayTrigger trigger);
    void unregisterTemplate(@NotNull String id);

    DisplayTrigger getTemplate(@NotNull String id);


    /**
     * Get the mod.
     *
     * @return The mod instance.
     */
    @NotNull
    AtumMod getAtumMod();
}
