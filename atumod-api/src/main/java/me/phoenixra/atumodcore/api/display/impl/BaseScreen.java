package me.phoenixra.atumodcore.api.display.impl;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.misc.AtumDebugger;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.gui.GuiMainMenu;
import org.jetbrains.annotations.NotNull;


public class BaseScreen extends GuiMainMenu {
    @Getter
    private AtumMod atumMod;
    @Getter
    private BaseRenderer renderer;

    public BaseScreen(@NotNull AtumMod atumMod, @NotNull DisplayCanvas canvas) {
        this.atumMod = atumMod;
        this.renderer = new BaseRenderer(atumMod,canvas,this);
    }
    @Override
    public void initGui() {
        renderer.initRenderer();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        renderer.render(mouseX,mouseY);
    }

    @Override
    public void onGuiClosed() {
        renderer.closeRenderer();
    }
}
