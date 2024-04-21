package me.phoenixra.atumodcore.api.display.misc.variables;

import lombok.Getter;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.misc.OptimizedVar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.IllegalFormatException;

public class OptimizedVarInt implements OptimizedVar<Integer> {
    @NotNull @Getter
    private String configKey;


    private Integer defaultValue;

    private HashMap<DisplayResolution, Integer> values = new HashMap<>();

    public OptimizedVarInt(@NotNull String configKey, int defaultValue) {
        this.configKey = configKey;
        this.defaultValue = defaultValue;
    }

    @Override
    public @NotNull Integer getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setDefaultValue(@NotNull Integer defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public @NotNull Integer getValue(@Nullable DisplayResolution resolution) {
        if(resolution == null) return defaultValue;
        return values.getOrDefault(resolution, defaultValue);
    }

    @Override
    public void addOptimizedValue(@NotNull DisplayResolution resolution,
                                  @Nullable Integer value) {
        if(value == null) return;
        values.put(resolution, value);
    }

    @Override
    public void addOptimizedValue(@NotNull DisplayResolution resolution,
                                  @Nullable String configValue) throws IllegalFormatException {
        if (configValue == null) return;
        values.put(resolution, Integer.parseInt(configValue.split("\\.")[0]));
    }

    @Override
    public void removeOptimizedValue(@NotNull DisplayResolution resolution) {
        values.remove(resolution);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        OptimizedVarInt clone = (OptimizedVarInt) super.clone();
        clone.values = (HashMap<DisplayResolution, Integer>) values.clone();
        return clone;
    }


}
