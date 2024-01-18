package me.phoenixra.atumodcore.api.display.misc.variables;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.misc.OptimizedVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.IllegalFormatException;

public class OptimizedVariableBoolean implements OptimizedVariable<Boolean> {
    @NotNull
    @Getter
    private String configKey;


    private boolean defaultValue;

    private HashMap<DisplayResolution, Boolean> values = new HashMap<>();

    public OptimizedVariableBoolean(@NotNull String configKey, boolean defaultValue) {
        this.configKey = configKey;
        this.defaultValue = defaultValue;
    }

    @Override
    public @NotNull Boolean getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setDefaultValue(@NotNull Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public @NotNull Boolean getValue(@Nullable DisplayResolution resolution) {
        if(resolution == null) return defaultValue;
        return values.getOrDefault(resolution, defaultValue);
    }

    @Override
    public void addOptimizedValue(@NotNull DisplayResolution resolution,
                                  @Nullable Boolean value) {
        if(value == null) return;
        values.put(resolution, value);
    }

    @Override
    public void addOptimizedValue(@NotNull DisplayResolution resolution,
                                  @Nullable String configValue) throws IllegalFormatException {
        if (configValue == null) return;
        values.put(resolution, Boolean.parseBoolean(configValue));
    }

    @Override
    public void removeOptimizedValue(@NotNull DisplayResolution resolution) {
        values.remove(resolution);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        OptimizedVariableBoolean clone = (OptimizedVariableBoolean) super.clone();
        clone.values = (HashMap<DisplayResolution, Boolean>) values.clone();
        return clone;
    }
}
