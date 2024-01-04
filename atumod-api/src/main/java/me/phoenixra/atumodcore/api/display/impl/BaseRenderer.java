package me.phoenixra.atumodcore.api.display.impl;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.data.DisplayData;
import me.phoenixra.atumodcore.api.misc.AtumDebugger;
import me.phoenixra.atumodcore.api.placeholders.InjectablePlaceholder;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseRenderer implements DisplayRenderer {
    private final AtumMod atumMod;
    @Getter
    private DisplayCanvas baseCanvas;

    private AtumDebugger renderDebugger;

    private BaseScreen attachedGuiScreen;

    @Getter
    private DisplayData displayData;

    private List<InjectablePlaceholder> injections = Collections.synchronizedList(new ArrayList<>());

    private boolean init = false;

    public BaseRenderer(AtumMod atumMod, DisplayCanvas baseCanvas, BaseScreen attachedGuiScreen) {
        this.atumMod = atumMod;
        this.baseCanvas = baseCanvas;
        this.attachedGuiScreen = attachedGuiScreen;
        displayData = new BaseDisplayData(this);
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
        if(!init){
            initRenderer();
            init = true;
        }
        baseCanvas.draw(RenderUtils.getScaleFactor(), 1, 1,mouseX,mouseY);
    }

    @Override
    public void reloadRenderer() {
        if(baseCanvas.getTemplateId() == null) return;
        atumMod.getLogger().info("Reloading renderer for canvas: "+baseCanvas.getId());
        baseCanvas.onRemove();
        baseCanvas = atumMod.getDisplayManager()
                .getElementRegistry().getDrawableCanvas(
                        baseCanvas.getTemplateId()
                );
        if(baseCanvas == null){
            atumMod.getLogger().error("Failed to reload renderer for canvas: "+baseCanvas.getId());
            return;
        }
        baseCanvas.setDisplayRenderer(this);
        displayData.clearData();
        onReload();
    }

    @Override
    public final void closeRenderer() {
        baseCanvas.onRemove();
        onRendererClosed();

    }

    @Override
    public void setBaseCanvas(@NotNull DisplayCanvas baseCanvas) {
        this.baseCanvas = baseCanvas;
        baseCanvas.setDisplayRenderer(this);
    }


    public void onRendererClosed() {
        //override if needed
    }
    public void onReload(){
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

    @Override
    public void addInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
        for (InjectablePlaceholder placeholder : placeholders) {
            if (placeholder == null) {
                continue;
            }
            if (injections.contains(placeholder)) {
                continue;
            }
            injections.add(placeholder);
        }
    }

    @Override
    public void removeInjectablePlaceholder(@NotNull Iterable<InjectablePlaceholder> placeholders) {
        for (InjectablePlaceholder placeholder : placeholders) {
            if (placeholder == null) {
                continue;
            }
            injections.remove(placeholder);
        }
    }

    @Override
    public void clearInjectedPlaceholders() {
        injections.clear();
    }
    @Override
    public @NotNull List<InjectablePlaceholder> getPlaceholderInjections() {
        return injections;
    }

}
