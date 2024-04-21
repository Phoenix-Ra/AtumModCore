package me.phoenixra.atumodcore.api.display.animators;

import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import org.jetbrains.annotations.NotNull;

public interface DisplayAnimator extends Cloneable{

    void run(@NotNull DisplayElement element,
             @NotNull Config animationSettings);


    /**
     * Get the mod instance
     *
     * @return The mod instance.
     */
    @NotNull AtumMod getAtumMod();
}
