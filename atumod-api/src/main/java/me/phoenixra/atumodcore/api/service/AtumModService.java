package me.phoenixra.atumodcore.api.service;

import net.minecraftforge.fml.common.event.FMLEvent;
import org.jetbrains.annotations.NotNull;

/**
 * A mod service is the feature allowing you to
 * use the FMLEvents outside the AtumMod class.
 * and control the lifecycle of the service.
 */
public interface AtumModService {

    /**
     * Called on FMLEvent called.
     * Make sure that you used the notifyModServices
     * method in your AtumMod class.
     * Otherwise, this method will not be called.
     *
     * @param event The event.
     */
    void handleFmlEvent(@NotNull FMLEvent event);

    /**
     * Called when the service is removed.
     *
     */
    void onRemove();

    /**
     * Get the id of the service.
     *
     * @return The id
     */
    @NotNull String getId();
}
