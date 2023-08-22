package me.phoenixra.atumodcore.api.config;

import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.wrapper.LoadableConfigWrapper;
import org.jetbrains.annotations.NotNull;

public class BaseConfig extends LoadableConfigWrapper {
    protected BaseConfig(@NotNull AtumMod plugin,
                         @NotNull String configName,
                         @NotNull ConfigType configType,
                         boolean forceLoadResource) {
        super(AtumAPI.getInstance().createLoadableConfig(
                plugin,
                configName,
                "",
                configType,
                forceLoadResource)
        );
    }
}
