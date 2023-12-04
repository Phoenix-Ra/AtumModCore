package me.phoenixra.atumodcore.api.misc.resources;


import net.minecraft.util.ResourceLocation;

public interface ITextureResourceLocation {
    void loadTexture();

    ResourceLocation getResourceLocation();

    boolean isReady();

    int getHeight();

    int getWidth();

}