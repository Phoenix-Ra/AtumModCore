package me.phoenixra.atumodcore.api.display.misc.variables;

import lombok.Getter;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.misc.OptimizedVar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.IllegalFormatException;

public class OptimizedVarFloat implements OptimizedVar<Float> {

    @NotNull
    @Getter
    private String configKey;

    private float defaultValue;

    private HashMap<DisplayResolution, Float> values = new HashMap<>();

    public OptimizedVarFloat(@NotNull String configKey, float defaultValue) {
        this.configKey = configKey;
        this.defaultValue = defaultValue;
    }

    @Override
    public @NotNull Float getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setDefaultValue(@NotNull Float defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public @NotNull Float getValue(@Nullable DisplayResolution resolution) {
        if(resolution == null) return defaultValue;
        return values.getOrDefault(resolution, defaultValue);
    }

    @Override
    public void addOptimizedValue(@NotNull DisplayResolution resolution,
                                  @Nullable Float value) {
        if(value == null) return;
        values.put(resolution, value);
    }

    @Override
    public void addOptimizedValue(@NotNull DisplayResolution resolution,
                                  @Nullable String configValue) throws IllegalFormatException {
        if (configValue == null) return;
        values.put(resolution, Float.parseFloat(configValue));
    }

    @Override
    public void removeOptimizedValue(@NotNull DisplayResolution resolution) {
        values.remove(resolution);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        OptimizedVarFloat clone = (OptimizedVarFloat) super.clone();
        clone.values = (HashMap<DisplayResolution, Float>) values.clone();
        return clone;
    }

}
