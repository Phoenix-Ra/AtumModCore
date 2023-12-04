package me.phoenixra.atumodcore.core.display.screens;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

public class SetupScreen extends BaseScreen {
    public SetupScreen(@NotNull AtumMod atumMod, @NotNull DisplayCanvas canvas) {
        super(atumMod, canvas);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glClear(1);

        
    }
}
