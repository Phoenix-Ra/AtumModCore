package me.phoenixra.atumodcore.api.config;

import org.apache.logging.log4j.core.config.yaml.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface LoadableConfig extends Config {
    /**
     * Create the file.
     *
     * @param forceResourceLoad if true - throws NullPointerException
     *                          when file not found inside the resources folder
     */
    void createFile(boolean forceResourceLoad);

    /**
     * Reload the config.
     *
     * @throws IOException If error has occurred while reloading
     */
    void reload() throws IOException;

    /**
     * Save the config.
     *
     * @throws IOException If error has occurred while saving
     */
    void save() throws IOException;

    /**
     * Get the config file.
     *
     * @return The file.
     */
    File getFile();

    /**
     * Get the full file name
     *
     * @return The file name.
     */
    default String getFileName(){
        return getFile().getName();
    }

    /**
     * Get the name of a config
     *
     * @return The name.
     */
    default String getName(){
        return getFileName().replace("."+getType().getFileExtension(),"");
    }

    /**
     * Get the path to a file inside the .jar
     *
     * @return The file.
     */
    String getResourcePath();
}
