package me.phoenixra.atumodcore.core.config;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.ConfigType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class AtumConfigSection extends AtumConfig{

    public AtumConfigSection(@NotNull AtumMod atumMod, @NotNull ConfigType type, @Nullable Map<String, Object> values) {
        super(atumMod,type);
        if(values!=null) applyData(values);
    }
}
