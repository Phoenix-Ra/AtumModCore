package me.phoenixra.atumodcore.api.display.misc.resources;

import net.minecraft.util.ResourceLocation;

/**
 * Experimental
 */
public interface TextureResource {
    void loadTexture();

    boolean isLoaded();

    void clear();

    ResourceLocation getResourceLocation();
    int getHeight();

    int getWidth();
}
