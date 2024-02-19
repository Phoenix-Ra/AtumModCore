package me.phoenixra.atumodcore.api.config;

import me.phoenixra.atumconfig.api.ConfigOwner;
import me.phoenixra.atumconfig.core.config.AtumConfigManager;
import me.phoenixra.atumodcore.api.service.AtumModService;
import net.minecraftforge.fml.common.event.FMLEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import org.jetbrains.annotations.NotNull;

public class AtumConfigManagerMod extends AtumConfigManager implements AtumModService {
    public AtumConfigManagerMod(ConfigOwner configOwner) {
        super(configOwner);
    }

    @Override
    public void handleFmlEvent(@NotNull FMLEvent event) {
        if(event instanceof FMLPostInitializationEvent){
            reloadAll();
        }
    }

    @Override
    public void onRemove() {

    }

    @Override
    public @NotNull String getId() {
        return "config_manager";
    }
}
