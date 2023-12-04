package me.phoenixra.atumodcore.api.config.wrapper;

import me.phoenixra.atumodcore.api.config.LoadableConfig;
import org.apache.logging.log4j.core.config.yaml.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LoadableConfigWrapper extends ConfigWrapper<LoadableConfig> implements LoadableConfig {
    protected LoadableConfigWrapper(@NotNull final LoadableConfig handle) {
        super(handle);
    }
    @Override
    public void createFile(boolean forceResourceLoad) {
        getHandle().createFile(forceResourceLoad);
    }

    @Override
    public void reload() throws IOException {
        getHandle().reload();
    }

    @Override
    public void save() throws IOException {
        getHandle().save();
    }

    @Override
    public File getFile() {
        return getHandle().getFile();
    }

    @Override
    public String getResourcePath() {
        return getHandle().getResourcePath();
    }

}
