package me.phoenixra.atumodcore.api.config.variables;

import me.phoenixra.atumodcore.api.config.Config;
import org.jetbrains.annotations.NotNull;

public interface ConfigVariable<T> extends Cloneable {
    /**
     * getEffectBuilder the saved value
     *
     * @return The value
     */
    @NotNull
    T getValue();

    /**
     * set the value
     *
     * @return this
     */
    @NotNull
    ConfigVariable<T> setValue(@NotNull T value);

    /**
     * set the value
     *
     * @return this
     * @throws UnsupportedOperationException if the value cannot be set from a string
     */
    @NotNull
    ConfigVariable<T> setValueFromString(@NotNull String value);

    /**
     * set the value
     *
     * @return this
     * @throws UnsupportedOperationException if the value cannot be set from a config
     */
    @NotNull
    ConfigVariable<T> setValueFromConfig(@NotNull Config config);


    ConfigVariable<T> clone() throws CloneNotSupportedException;
}
