package me.phoenixra.atumodcore.api.service;

import net.minecraftforge.fml.common.event.FMLEvent;
import org.jetbrains.annotations.NotNull;

public interface AtumModService {

    void handleFmlEvent(@NotNull FMLEvent event);
    void onRemove();
    @NotNull String getId();
}
