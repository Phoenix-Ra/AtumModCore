package me.phoenixra.atumodcore.api;

import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.ConfigManager;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.display.EnabledCanvasRegistry;
import me.phoenixra.atumodcore.api.display.DisplayElementRegistry;
import me.phoenixra.atumodcore.api.display.actions.DisplayActionRegistry;
import me.phoenixra.atumodcore.api.input.InputHandler;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;

public interface AtumAPI {

    /**
     * Create an input handler
     *
     * @param atumMod The mod.
     * @return The handler
     */
    @NotNull
    InputHandler createInputHandler(@NotNull AtumMod atumMod);

    /**
     * Create a config manager
     *
     * @param atumMod The mod.
     * @return The manager
     */
    @NotNull
    ConfigManager createConfigManager(@NotNull AtumMod atumMod);

    /**
     * Create a logger
     *
     * @param atumMod The mod.
     * @return The logger
     */
    @NotNull
    Logger createLogger(@NotNull AtumMod atumMod);


    /**
     * Load configuration from file
     *
     * @param atumMod The mod.
     * @param file the file to load configuration from
     * @return loaded config
     */
    @NotNull
    LoadableConfig loadConfiguration(@NotNull AtumMod atumMod,
                                     @NotNull File file);

    /**
     * loads an existing config from the plugin folder
     * and adds it to a configManager of a plugin
     * <p></p>
     * if specified config doesn't exists
     * creates a new config with content
     * from the plugin resources
     * <p></p>
     *
     * @param atumMod The mod.
     * @param name name of a config (without extension)
     * @param directory The directory of a config. Use empty if root directory
     * @param type The type of a config
     * @param forceLoadResource if true - throws NullPointerException
     *                       when file not found inside the resources folder,
     *                          otherwise creates an empty file
     * @return loaded config
     */
    @NotNull
    LoadableConfig createLoadableConfig(@NotNull AtumMod atumMod,
                                        @NotNull String name,
                                        @NotNull String directory,
                                        @NotNull ConfigType type,
                                        boolean forceLoadResource);


    /**
     * Create config.
     *
     * @param values The values.
     * @param type   The config type.
     * @return The config
     */
    @NotNull
    Config createConfig(@NotNull AtumMod atumMod,
                        @Nullable Map<String, Object> values,
                        @NotNull ConfigType type);



    @NotNull
    DisplayElementRegistry createDisplayElementRegistry(@NotNull AtumMod atumMod);
    @NotNull
    DisplayActionRegistry createDisplayActionRegistry(@NotNull AtumMod atumMod);
    @NotNull
    EnabledCanvasRegistry createEnabledCanvasRegistry(@NotNull AtumMod atumMod);
    /**
     * Evaluate an expression.
     *
     * @param expression The expression.
     * @param context    The context.
     * @return The value of the expression, or zero if invalid.
     */
    double evaluate(@NotNull AtumMod atumMod,
                    @NotNull String expression,
                    @NotNull PlaceholderContext context);


    AtumMod getCoreMod();
    @Nullable AtumMod getLoadedAtumMod(String name);
    void registerAtumMod(@NotNull AtumMod atumMod);

    static AtumAPI getInstance() {
        return Instance.get();
    }

    final class Instance {
        private static AtumAPI api;
        private Instance() {
            throw new UnsupportedOperationException("This is an utility class and cannot be instantiated");
        }

        static void set(final AtumAPI api) {
            if(Instance.api != null) return;

            Instance.api = api;
        }


        static AtumAPI get() {
            return api;
        }


    }
}
