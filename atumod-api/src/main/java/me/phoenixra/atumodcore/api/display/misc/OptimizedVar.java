package me.phoenixra.atumodcore.api.display.misc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IllegalFormatException;

public interface OptimizedVar<T> extends Cloneable{

    /**
     * Get the config key.
     * @return The config key.
     */
    @NotNull String getConfigKey();

    /**
     * Get the default value.
     * @return The default value.
     */
    @NotNull
    T getDefaultValue();

    /**
     * Set the default value.
     * @param defaultValue The default value.
     */
    void setDefaultValue(@NotNull T defaultValue);

    /**
     * Get the optimized value.
     * @param resolution The resolution.
     * @return The optimized value.
     */
    @NotNull
    T getValue(@Nullable DisplayResolution resolution);

    /**
     * Add the optimized value.
     * @param resolution The resolution.
     * @param value The value.
     */
    void addOptimizedValue(@NotNull DisplayResolution resolution,
                           @Nullable T value
    );

    /**
     * Add the optimized value.
     * @param resolution The resolution.
     * @param configValue The config value.
     * @throws IllegalFormatException If the config value is invalid.
     */
    void addOptimizedValue(@NotNull DisplayResolution resolution,
                           @Nullable String configValue) throws IllegalFormatException;

    /**
     * Remove the optimized value.
     * @param resolution The resolution.
     */
    void removeOptimizedValue(@NotNull DisplayResolution resolution);


    Object clone() throws CloneNotSupportedException;
}
