package me.phoenixra.atumodcore.api.placeholders;

import me.phoenixra.atumodcore.api.AtumMod;
import org.jetbrains.annotations.Nullable;

public interface InjectablePlaceholder extends Placeholder{
    /**
     * Get the plugin that holds the arguments.
     *
     * @return The plugin.
     */
    @Nullable
    @Override
    default AtumMod getAtumMod() {
        return null;
    }
}
