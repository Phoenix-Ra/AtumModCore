package me.phoenixra.atumodcore.api.display.impl;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.misc.AtumDebugger;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.gui.GuiMainMenu;
import org.jetbrains.annotations.NotNull;


public class BaseScreen extends GuiMainMenu {
    @Getter
    private AtumMod atumMod;
    @Getter
    private DisplayCanvas canvas;

    public BaseScreen(@NotNull AtumMod atumMod, @NotNull DisplayCanvas canvas) {
        this.atumMod = atumMod;
        this.canvas = canvas;
        this.canvas.setAttachedGuiScreen(this);
    }
    @Override
    public void initGui() {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        new AtumDebugger(atumMod,"testMenu","Drawing the menu")
                .start(()->{
                    canvas.draw(RenderUtils.getScaleFactor(), 1, 1,mouseX,mouseY);
                });
    }

    @Override
    public void onGuiClosed() {
        DisplayCanvas canvas1 = canvas;
        canvas = (DisplayCanvas) canvas.clone();
        canvas1.onRemove();
        super.onGuiClosed();
    }
}
