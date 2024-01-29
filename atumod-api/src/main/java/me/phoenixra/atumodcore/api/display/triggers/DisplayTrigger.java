package me.phoenixra.atumodcore.api.display.triggers;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DisplayTrigger extends Cloneable{


    /**
     * Call the trigger.
     * @param triggerData The trigger data.
     */
    void trigger(DisplayTriggerData triggerData);





    /**
     * Clone the trigger with new variables.
     * @param config The config.
     * @param owner The owner.
     * @return The cloned trigger.
     */
    @NotNull DisplayTrigger cloneWithNewVariables(@NotNull Config config,
                                                  @Nullable DisplayRenderer owner);

    /**
     * Clone the trigger.
     * @return The cloned trigger.
     */
    @NotNull DisplayTrigger clone();

    /**
     * Get the display renderer owning this trigger.
     *
     * @return The display renderer.
     */
    @NotNull DisplayRenderer getOwner();


    /**
     * Get the mod instance
     *
     * @return The mod instance.
     */
    @NotNull AtumMod getAtumMod();



}