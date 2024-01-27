package me.phoenixra.atumodcore.api.config;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.category.ConfigCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ConfigManager {

    /**
     * Reload all configs except config categories
     */
    void reloadAllConfigs();

    /**
     * reload the specified config
     *
     * @param name the name of a config without extension
     */
    void reloadConfig(@NotNull String name);

    /**
     * Save all configs. This will not save config categories
     */
    void saveAllConfigs();

    /**
     * Save the specified config
     *
     * @param name the name of a config without extension
     */
    void saveConfig(@NotNull String name);

    /**
     * Get config added to the manager
     * or null if not found
     * @param name The name of a config
     * @return this
     */
    @Nullable
    LoadableConfig getConfig(@NotNull String name);
    /**
     * Add new config to the handler
     *
     * @param config The loadable config
     * @return this
     */
    ConfigManager addConfig(@NotNull LoadableConfig config);

    /**
     * Reload all config categories
     */
    void reloadAllConfigCategories();

    /**
     * Reload the config category
     *
     * @param id the id of a category
     */
    void reloadConfigCategory(@NotNull String id);

    /**
     * Get config category added to the manager
     * or null if not found
     *
     * @param id The id
     * @return The config category
     */
    @Nullable
    ConfigCategory getConfigCategory(@NotNull String id);

    /**
     * Add new config category
     *
     * @param configCategory The config category
     */
    void addConfigCategory(@NotNull ConfigCategory configCategory);

    /**
     * Get the mod.
     *
     * @return The mod instance.
     */
    @NotNull
    AtumMod getAtumMod();
}
