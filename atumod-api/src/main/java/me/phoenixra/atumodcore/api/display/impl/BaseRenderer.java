package me.phoenixra.atumodcore.api.display.impl;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.misc.AtumDebugger;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaseRenderer implements DisplayRenderer {
    private final AtumMod atumMod;
    private DisplayCanvas baseCanvas;

    private AtumDebugger renderDebugger;

    private BaseScreen attachedGuiScreen;

    public BaseRenderer(AtumMod atumMod, DisplayCanvas baseCanvas, BaseScreen attachedGuiScreen) {
        this.atumMod = atumMod;
        this.baseCanvas = baseCanvas;
        this.attachedGuiScreen = attachedGuiScreen;
        baseCanvas.setDisplayRenderer(this);
    }
    public BaseRenderer(AtumMod atumMod, DisplayCanvas baseCanvas) {
        this(atumMod,baseCanvas,null);
    }
    @Override
    public void initRenderer() {
        renderDebugger =
        new AtumDebugger(atumMod,"testMenu-"+baseCanvas.getId(),"Drawing the menu");
    }

    @Override
    public void render(int mouseX, int mouseY) {
        renderDebugger.start(()->{
            baseCanvas.draw(RenderUtils.getScaleFactor(), 1, 1,mouseX,mouseY);
        });
    }

    @Override
    public void reloadRenderer() {
        baseCanvas.onRemove();
        baseCanvas = atumMod.getDisplayManager()
                .getElementRegistry().getDrawableCanvas(
                        baseCanvas.getId()
                );
    }

    @Override
    public final void closeRenderer() {
        baseCanvas.onRemove();
        onRendererClosed();

    }

    @Override
    public void onRendererClosed() {
        //override if needed
    }

    @Override
    public final @Nullable BaseScreen getAttachedGuiScreen() {
        return attachedGuiScreen;
    }

    @Override
    public final @NotNull AtumMod getAtumMod() {
        return atumMod;
    }
}
