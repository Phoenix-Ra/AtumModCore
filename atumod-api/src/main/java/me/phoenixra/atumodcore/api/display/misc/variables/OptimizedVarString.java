package me.phoenixra.atumodcore.api.display.misc.variables;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.display.misc.OptimizedVar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.IllegalFormatException;

public class OptimizedVarString implements OptimizedVar<String> {
    @NotNull
    @Getter
    private String configKey;

    @Getter @Setter
    private String defaultValue;

    private HashMap<DisplayResolution, String> values = new HashMap<>();

    public OptimizedVarString(@NotNull String configKey, String defaultValue) {
        this.configKey = configKey;
        this.defaultValue = defaultValue;
    }

    @Override
    public @NotNull String getValue(@Nullable DisplayResolution resolution) {
        if(resolution == null) return defaultValue;
        return values.getOrDefault(resolution, defaultValue);
    }


    @Override
    public void addOptimizedValue(@NotNull DisplayResolution resolution,
                                  @Nullable String configValue) throws IllegalFormatException {
        if(configValue == null) return;
        values.put(resolution, configValue);
    }

    @Override
    public void removeOptimizedValue(@NotNull DisplayResolution resolution) {
        values.remove(resolution);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        OptimizedVarString clone = (OptimizedVarString) super.clone();
        clone.values = (HashMap<DisplayResolution, String>) values.clone();
        return clone;
    }

}
