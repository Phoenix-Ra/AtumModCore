package me.phoenixra.atumodcore.core;

import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.ConfigManager;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.display.DisplayElementRegistry;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.core.config.AtumConfigManager;
import me.phoenixra.atumodcore.core.config.AtumConfigSection;
import me.phoenixra.atumodcore.core.config.AtumLoadableConfig;
import me.phoenixra.atumodcore.core.display.AtumDisplayElementRegistry;
import me.phoenixra.atumodcore.core.misc.ExpressionEvaluator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;

public class AtumAPIImpl implements AtumAPI {

    private ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
    @Override
    public @NotNull ConfigManager createConfigManager(@NotNull AtumMod atumMod) {
        return new AtumConfigManager(atumMod);
    }

    @Override
    public @NotNull Logger createLogger(@NotNull AtumMod atumMod) {
        return LogManager.getLogger(atumMod.getName());
    }

    @Override
    public @NotNull LoadableConfig loadConfiguration(@NotNull AtumMod atumMod, @NotNull File file) {
        return new AtumLoadableConfig(atumMod, file);
    }

    @Override
    public @NotNull LoadableConfig createLoadableConfig(@NotNull AtumMod atumMod, @NotNull String name, @NotNull String directory, @NotNull ConfigType type, boolean forceLoadResource) {
        return new AtumLoadableConfig(atumMod, type, directory, name, forceLoadResource);
    }

    @Override
    public @NotNull Config createConfig(@NotNull AtumMod atumMod,
                                        @Nullable Map<String, Object> values,
                                        @NotNull ConfigType type) {
        return new AtumConfigSection(atumMod, type, values);
    }

    @Override
    public @NotNull DisplayElementRegistry createDisplayElementRegistry(@NotNull AtumMod atumMod) {
        return new AtumDisplayElementRegistry(atumMod);
    }

    @Override
    public double evaluate(@NotNull AtumMod atumMod, @NotNull String expression, @NotNull PlaceholderContext context) {
        return expressionEvaluator.evaluate(atumMod, expression, context);
    }
}
