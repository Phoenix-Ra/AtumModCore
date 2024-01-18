package me.phoenixra.atumodcore.api.display.misc;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.IllegalFormatException;

public interface OptimizedVariable<T> extends Cloneable{

    @NotNull
    String getConfigKey();
    @NotNull
    T getDefaultValue();
    void setDefaultValue(@NotNull T defaultValue);
    @NotNull
    T getValue(@Nullable DisplayResolution resolution);
    void addOptimizedValue(@NotNull DisplayResolution resolution,
                           @Nullable T value
    );
    void addOptimizedValue(@NotNull DisplayResolution resolution,
                           @Nullable String configValue
    ) throws IllegalFormatException;
    void removeOptimizedValue(@NotNull DisplayResolution resolution);


    Object clone() throws CloneNotSupportedException;
}
