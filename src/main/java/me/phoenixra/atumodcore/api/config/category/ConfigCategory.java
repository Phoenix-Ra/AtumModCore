package me.phoenixra.atumodcore.api.config.category;

import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.ConfigType;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import me.phoenixra.atumodcore.api.utils.FileUtils;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public abstract class ConfigCategory {
    private final AtumMod plugin;
    private final ConfigType configType;
    private final String id;
    private final String directory;
    private final boolean supportSubFolders;

    /**
     * Config Category class
     *
     * @param atumMod            the plugin
     * @param id                the category id
     * @param directory         the directory path
     * @param supportSubFolders if accept configs from subFolders
     */
    public ConfigCategory(@NotNull AtumMod atumMod,
                          @NotNull ConfigType configType,
                          @NotNull String id,
                          @NotNull String directory,
                          boolean supportSubFolders) {
        this.plugin = atumMod;
        this.configType = configType;
        this.id = id;
        this.directory = directory;
        this.supportSubFolders = supportSubFolders;
    }

    /**
     * Reload the config category
     */
    public final void reload() {
        beforeReload();
        clear();
        File dir = new File(plugin.getDataFolder(), directory);
        if (!dir.exists()) {
            loadDefaults();
        }
        for (PairRecord<String, File> entry : FileUtils.loadFiles(dir, supportSubFolders)) {
            Config conf = AtumAPI.getInstance().createLoadableConfig(
                    getPlugin(),
                    entry.getSecond().getName().split("\\.")[0],
                    directory,
                    configType,
                    false
            );
            acceptConfig(entry.getFirst(), conf);
        }
        afterReload();
    }

    private void loadDefaults() {

        for (String path : FileUtils.getAllPathsInResourceFolder(plugin, directory)) {
            try {
                File file = new File(plugin.getDataFolder(), path);
                if (!file.getName().contains(".")) {
                    file.mkdir();
                    plugin.getLogger().info("Dir: " + path + " | " + file.getName());
                    continue;
                }
                plugin.getLogger().info("File: " + path + " | " + file.getName());
                InputStream stream = plugin.getClass().getResourceAsStream(path);
                if (stream == null) continue;
                Files.copy(stream, Paths.get(file.toURI()), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Clear the saved data
     */
    protected abstract void clear();

    /**
     * Accept the config
     */
    protected abstract void acceptConfig(@NotNull String id, @NotNull Config config);

    /**
     * Called before category reload
     * <p></p>
     * Override to add implementation
     */
    public void beforeReload() {
    }

    /**
     * Called after category reload
     * <p></p>
     * Override to add implementation
     */
    public void afterReload() {
    }


    /**
     * Get the ID of a category
     *
     * @return The id
     */
    public final @NotNull String getId() {
        return id;
    }

    /**
     * Get the directory path
     *
     * @return The directory
     */
    public final @NotNull String getDirectory() {
        return directory;
    }


    /**
     * Get the plugin.
     *
     * @return The plugin instance.
     */
    public final @NotNull AtumMod getPlugin() {
        return plugin;
    }
}
