package me.phoenixra.atumodcore.api.placeholders;

import me.phoenixra.atumodcore.api.AtumMod;
import org.jetbrains.annotations.NotNull;

public interface RegistrablePlaceholder extends Placeholder{

    /**
     * Register the arguments.
     *
     * @return The arguments.
     */
    @NotNull
    default RegistrablePlaceholder register() {
        PlaceholderManager.registerPlaceholder(this);
        return this;
    }

    /**
     * Get the plugin that holds the arguments.
     *
     * @return The plugin.
     */
    @NotNull
    @Override
    AtumMod getAtumMod();
}
