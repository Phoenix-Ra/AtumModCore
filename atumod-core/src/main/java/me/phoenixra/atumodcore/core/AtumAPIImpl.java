package me.phoenixra.atumodcore.core;

import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.ConfigManager;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.display.DisplayElementRegistry;
import me.phoenixra.atumodcore.api.display.EnabledCanvasRegistry;
import me.phoenixra.atumodcore.api.display.actions.DisplayActionRegistry;
import me.phoenixra.atumodcore.api.input.InputHandler;
import me.phoenixra.atumodcore.api.network.NetworkManager;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.core.config.AtumConfigManager;
import me.phoenixra.atumodcore.core.config.AtumConfigSection;
import me.phoenixra.atumodcore.core.config.AtumLoadableConfig;
import me.phoenixra.atumodcore.core.display.AtumDisplayActionRegistry;
import me.phoenixra.atumodcore.core.display.AtumDisplayElementRegistry;
import me.phoenixra.atumodcore.core.display.AtumEnabledCanvasRegistry;
import me.phoenixra.atumodcore.core.input.AtumInputHandler;
import me.phoenixra.atumodcore.core.misc.ExpressionEvaluator;
import me.phoenixra.atumodcore.core.network.AtumNetworkManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AtumAPIImpl implements AtumAPI {

    private ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();

    private AtumMod atumMod;
    private HashMap<String,AtumMod> mods = new HashMap<>();

    public AtumAPIImpl(@NotNull AtumMod atumMod) {
        this.atumMod = atumMod;
    }

    @Override
    public @NotNull InputHandler createInputHandler(@NotNull AtumMod atumMod) {
        return new AtumInputHandler(atumMod);
    }

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
    public @NotNull DisplayActionRegistry createDisplayActionRegistry(@NotNull AtumMod atumMod) {
        return new AtumDisplayActionRegistry(atumMod);
    }

    @Override
    public @NotNull EnabledCanvasRegistry createEnabledCanvasRegistry(@NotNull AtumMod atumMod) {
        return new AtumEnabledCanvasRegistry(atumMod);
    }

    @Override
    public @NotNull NetworkManager createNetworkManager(@NotNull AtumMod atumMod) {
        return new AtumNetworkManager(atumMod);
    }


    @Override
    public double evaluate(@NotNull AtumMod atumMod, @NotNull String expression, @NotNull PlaceholderContext context) {
        return expressionEvaluator.evaluate(atumMod, expression, context);
    }

    @Override
    public AtumMod getCoreMod() {
        return atumMod;
    }

    @Override
    public AtumMod getLoadedAtumMod(String name) {
        return mods.get(name);
    }

    @Override
    public void registerAtumMod(@NotNull AtumMod atumMod) {
        mods.put(atumMod.getName(), atumMod);
    }
}
