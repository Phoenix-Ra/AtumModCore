package me.phoenixra.atumodcore.api.display.triggers;

import me.phoenixra.atumodcore.api.AtumMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DisplayTriggerRegistry {


    /**
     * Register the trigger template.
     *
     * @param id The id.
     * @param trigger The trigger.
     */
    void registerTemplate(@NotNull String id, @NotNull DisplayTrigger trigger);

    /**
     * Unregister the trigger template.
     *
     * @param id The id.
     */
    void unregisterTemplate(@NotNull String id);

    /**
     * Get the trigger template.
     *
     * @param id The id.
     * @return The template.
     */
    @Nullable DisplayTrigger getTemplate(@NotNull String id);


    /**
     * Get the mod.
     *
     * @return The mod instance.
     */
    @NotNull
    AtumMod getAtumMod();
}
