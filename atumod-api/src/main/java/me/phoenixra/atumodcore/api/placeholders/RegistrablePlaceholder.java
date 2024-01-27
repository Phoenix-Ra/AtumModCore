package me.phoenixra.atumodcore.api.placeholders;

import me.phoenixra.atumodcore.api.AtumMod;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a placeholder that
 * can be registered in {@link PlaceholderManager}
 * <p>It is so-called global placeholder.</p>
 */
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
     * Get the mod that holds the arguments.
     *
     * @return The mod.
     */
    @NotNull
    @Override
    AtumMod getAtumMod();
}
