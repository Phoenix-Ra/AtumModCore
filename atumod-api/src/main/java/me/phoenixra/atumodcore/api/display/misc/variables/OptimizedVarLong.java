package me.phoenixra.atumodcore.api.display.misc.variables;

import lombok.Getter;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.misc.OptimizedVar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.IllegalFormatException;

public class OptimizedVarLong implements OptimizedVar<Long> {

    @NotNull
    @Getter
    private String configKey;

    private long defaultValue;

    private HashMap<DisplayResolution, Long> values = new HashMap<>();

    public OptimizedVarLong(@NotNull String configKey, long defaultValue) {
        this.configKey = configKey;
        this.defaultValue = defaultValue;
    }

    @Override
    public @NotNull Long getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setDefaultValue(@NotNull Long defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public @NotNull Long getValue(@Nullable DisplayResolution resolution) {
        if(resolution == null) return defaultValue;
        return values.getOrDefault(resolution, defaultValue);
    }

    @Override
    public void addOptimizedValue(@NotNull DisplayResolution resolution,
                                  @Nullable Long value){
        if(value == null) return;
        values.put(resolution, value);
    }

    @Override
    public void addOptimizedValue(@NotNull DisplayResolution resolution,
                                  @Nullable String configValue)  throws IllegalFormatException {
        if(configValue == null) return;
        values.put(resolution, Long.parseLong(configValue.split("\\.")[0]));
    }

    @Override
    public void removeOptimizedValue(@NotNull DisplayResolution resolution) {
        values.remove(resolution);
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        OptimizedVarLong clone = (OptimizedVarLong) super.clone();
        clone.values = (HashMap<DisplayResolution, Long>) values.clone();
        return clone;
    }
}
