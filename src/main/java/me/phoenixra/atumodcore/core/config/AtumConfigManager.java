package me.phoenixra.atumodcore.core.config;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.ConfigManager;
import me.phoenixra.atumodcore.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.config.category.ConfigCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AtumConfigManager implements ConfigManager {
    @Getter
    private AtumMod atumMod;
    private Map<String, LoadableConfig> configs = new ConcurrentHashMap<>();
    private Map<String, ConfigCategory> configCategoryRegistry = new ConcurrentHashMap<>();
    public AtumConfigManager(AtumMod atumMod) {
        this.atumMod = atumMod;
    }

    @Override
    public void reloadAllConfigs() {
        List<String> removal = new ArrayList<>();
        for (Map.Entry<String, LoadableConfig> entry : configs.entrySet()) {
            if (!entry.getValue().getFile().exists() || !entry.getValue().getFile().isFile()) {
                removal.add(entry.getKey());
                continue;
            }
            try {
                entry.getValue().reload();
            }catch (Exception exception){
                atumMod.getLogger().info(
                        "Caught a &eException while trying to reload the config with name:"+ entry.getKey()
                );
            }
        }
        removal.forEach(configs::remove);
    }

    @Override
    public void reloadConfig(@NotNull String name) {
        LoadableConfig  config = configs.get(name);
        if (config == null) {
            return;
        }
        try {
            config.reload();
        }catch (Exception exception){
            atumMod.getLogger().info(
                    "Caught a &eException while trying to reload the config with name:"+ config.getName()
            );
        }
    }

    @Override
    public void saveAllConfigs() {
        List<String> removal = new ArrayList<>();
        for (Map.Entry<String, LoadableConfig> entry : configs.entrySet()) {
            if (!entry.getValue().getFile().exists() || !entry.getValue().getFile().isFile()) {
                removal.add(entry.getKey());
                continue;
            }
            try {
                entry.getValue().save();
            }catch (Exception exception){
                atumMod.getLogger().info(
                        "Caught a &eException while trying to save the config with name:"+ entry.getKey()
                );
            }
        }
        removal.forEach(configs::remove);
    }

    @Override
    public void saveConfig(@NotNull String name) {
        LoadableConfig  config = configs.get(name);
        if (config == null) {
            return;
        }
        try {
            config.save();
        }catch (Exception exception){
            atumMod.getLogger().info(
                    "Caught a &eException while trying to save the config with name:"+ config.getName()
            );
        }
    }

    @Override
    public @Nullable LoadableConfig getConfig(@NotNull String name) {
        return configs.get(name);
    }

    @Override
    public ConfigManager addConfig(@NotNull LoadableConfig config) {
        configs.put(config.getName(), config);
        return this;
    }

    @Override
    public void reloadAllConfigCategories() {
        List<String> removal = new ArrayList<>();
        for (Map.Entry<String, ConfigCategory> entry : configCategoryRegistry.entrySet()) {
            try {
                entry.getValue().reload();
            }catch (Exception exception){
                atumMod.getLogger().info(
                        "Caught a &eException while trying to reload the config category with id:"+ entry.getKey()
                );
            }
        }
    }

    @Override
    public void reloadConfigCategory(@NotNull String id) {
        ConfigCategory config = configCategoryRegistry.get(id);
        if (config == null) {
            return;
        }
        try {
            config.reload();
        }catch (Exception exception){
            atumMod.getLogger().info(
                    "Caught a &eException while trying to reload the config category with id:"+ config.getId()
            );
        }
    }

    @Override
    public @Nullable ConfigCategory getConfigCategory(@NotNull String id) {
        return configCategoryRegistry.get(id);
    }
    @Override
    public void addConfigCategory(@NotNull ConfigCategory configCategory) {
        configCategoryRegistry.put(configCategory.getId(), configCategory);
    }
}
