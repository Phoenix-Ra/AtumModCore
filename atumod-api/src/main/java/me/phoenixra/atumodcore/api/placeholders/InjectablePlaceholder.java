package me.phoenixra.atumodcore.api.placeholders;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a placeholder that can be injected into a
 * {@link InjectablePlaceholderList} to be used in a {@link PlaceholderContext}.
 *
 * <p>It cannot be registered and exists only in injection</p>
 */
public interface InjectablePlaceholder extends Placeholder{

    /**
     * Get the mod that holds the arguments.
     *
     * @return The mod.
     */
    @Nullable
    @Override
    default AtumMod getAtumMod() {
        return null;
    }
}
