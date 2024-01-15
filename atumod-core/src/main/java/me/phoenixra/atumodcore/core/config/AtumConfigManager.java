package me.phoenixra.atumodcore.core.config;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.ConfigManager;
import me.phoenixra.atumodcore.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.config.category.ConfigCategory;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AtumConfigManager implements ConfigManager, AtumModService {
    @Getter
    private AtumMod atumMod;
    private Map<String, LoadableConfig> configs = new ConcurrentHashMap<>();
    private Map<String, ConfigCategory> configCategoryRegistry = new ConcurrentHashMap<>();
    public AtumConfigManager(AtumMod atumMod) {
        this.atumMod = atumMod;
        atumMod.provideModService(this);
    }
    @Override
    public void handleFmlEvent(@NotNull FMLEvent event) {
        if(event instanceof FMLPostInitializationEvent){
            reloadAllConfigCategories();
        }
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
                atumMod.getLogger().warn(
                        "Caught an Exception while trying to reload the config with name:"+ entry.getKey()
                );
                exception.printStackTrace();
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
            atumMod.getLogger().warn(
                    "Caught an Exception while trying to reload the config with name:"+ config.getName()
            );
            exception.printStackTrace();
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
                atumMod.getLogger().warn(
                        "Caught an Exception while trying to save the config with name:"+ entry.getKey()
                );
                exception.printStackTrace();
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
            atumMod.getLogger().warn(
                    "Caught an Exception while trying to save the config with name:"+ config.getName()
            );
            exception.printStackTrace();
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
                atumMod.getLogger().warn(
                        "Caught an Exception while trying to reload the config category with id:"+ entry.getKey()
                );
                exception.printStackTrace();
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
            atumMod.getLogger().warn(
                    "Caught an Exception while trying to reload the config category with id:"+ config.getId()
            );
            exception.printStackTrace();
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


    @Override
    public void onRemove() {

    }

    @Override
    public @NotNull String getId() {
        return "configManager";
    }
}
