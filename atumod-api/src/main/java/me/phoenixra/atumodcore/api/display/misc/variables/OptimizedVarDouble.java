package me.phoenixra.atumodcore.api.display.misc.variables;

import lombok.Getter;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.misc.OptimizedVar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.IllegalFormatException;

public class OptimizedVarDouble implements OptimizedVar<Double> {

    @NotNull
    @Getter
    private String configKey;

    private double defaultValue;

    private HashMap<DisplayResolution, Double> values = new HashMap<>();

    public OptimizedVarDouble(@NotNull String configKey, double defaultValue) {
        this.configKey = configKey;
        this.defaultValue = defaultValue;
    }

    @Override
    public @NotNull Double getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setDefaultValue(@NotNull Double defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public @NotNull Double getValue(@Nullable DisplayResolution resolution) {
        if(resolution == null) return defaultValue;
        return values.getOrDefault(resolution, defaultValue);
    }

    @Override
    public void addOptimizedValue(@NotNull DisplayResolution resolution,
                                  @Nullable Double value) {
        if(value == null) return;
        values.put(resolution, value);
    }

    @Override
    public void addOptimizedValue(@NotNull DisplayResolution resolution,
                                  @Nullable String configValue) throws IllegalFormatException {
        if (configValue == null) return;
        values.put(resolution, Double.parseDouble(configValue));
    }

    @Override
    public void removeOptimizedValue(@NotNull DisplayResolution resolution) {
        values.remove(resolution);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        OptimizedVarDouble clone = (OptimizedVarDouble) super.clone();
        clone.values = (HashMap<DisplayResolution, Double>) values.clone();
        return clone;
    }

}
