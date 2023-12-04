package me.phoenixra.atumodcore.api.config.serialization;

import me.phoenixra.atumodcore.api.config.Config;
import org.jetbrains.annotations.NotNull;

/**
 * Save objects to configs.
 *
 * @param <T> The type of object to save
 */
@FunctionalInterface
public interface ConfigSerializer<T> {

    /**
     * Save an object to a config.
     * <p>Use AtumAPI#getInstance#createConfig</p>
     *
     * @param obj The object.
     * @return The config.
     */
    @NotNull
    Config serializeToConfig(@NotNull T obj);
}
