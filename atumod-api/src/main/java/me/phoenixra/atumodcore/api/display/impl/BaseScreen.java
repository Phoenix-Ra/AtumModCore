package me.phoenixra.atumodcore.api.display.impl;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class BaseScreen extends GuiScreen {
    @Getter
    private AtumMod atumMod;
    @Getter
    private DisplayRenderer renderer;

    public BaseScreen(@NotNull AtumMod atumMod, @NotNull DisplayCanvas canvas, @Nullable DisplayRenderer renderer){
        this.atumMod = atumMod;
        this.renderer = renderer != null ? renderer :
                new BaseRenderer(atumMod,canvas,this);
        canvas.setDisplayRenderer(this.renderer);
    }
    public BaseScreen(@NotNull AtumMod atumMod, @NotNull DisplayCanvas canvas) {
        this(atumMod,canvas,null);
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

    /**
     * Override this method to change the default behavior of GuiScreen
     * @return true if the screen should be closed
     */
    @Override
    public boolean doesGuiPauseGame() {
        return super.doesGuiPauseGame();
    }
}
