package me.phoenixra.atumodcore.core.display;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayElementRegistry;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.core.display.elements.*;
import me.phoenixra.atumodcore.core.display.elements.canvas.DefaultCanvas;
import me.phoenixra.atumodcore.core.display.elements.choose.ElementChooseBool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AtumDisplayElementRegistry implements DisplayElementRegistry {
    @Getter
    private final AtumMod atumMod;

    private Map<String, DisplayElement> registry = new ConcurrentHashMap<>();
    public AtumDisplayElementRegistry(AtumMod atumMod) {
        this.atumMod = atumMod;
        registerTemplate("canvas", new DefaultCanvas(atumMod,null));
        registerTemplate("image", new ElementImage(atumMod,null));
        registerTemplate("text", new ElementText(atumMod,null));
        registerTemplate("button", new ElementButton(atumMod,null));
        registerTemplate("progress_bar", new ElementProgressBar(atumMod,null));


        registerTemplate("choose_bool", new ElementChooseBool(atumMod,null));

    }

    @Override
    public @Nullable DisplayCanvas getDrawableCanvas(@NotNull String id) {
        DisplayElement element = this.getElementTemplate(id);
        if(!(element instanceof DisplayCanvas)){
            return null;
        }
        return (DisplayCanvas) element.cloneWithRandomId();
    }

    @Override
    public @Nullable DisplayElement getElementTemplate(@NotNull String id) {
        return registry.get(id);
    }

    @Override
    public @Nullable DisplayCanvas getCanvasTemplate(@NotNull String id) {
        DisplayElement element = this.getElementTemplate(id);
        if(!(element instanceof DisplayCanvas)){
            return null;
        }
        return (DisplayCanvas) element;
    }

    @Override
    public DisplayCanvas compileCanvasTemplate(@NotNull String id, @NotNull Config config) {

        String canvasType = config.getStringOrDefault("type","canvas");
        DisplayElement element = this.getElementTemplate(canvasType);
        if(element == null){
            this.atumMod.getLogger().error("Could not find canvas type: " + canvasType);
            return null;
        }
        if(!(element instanceof BaseCanvas)){
            this.atumMod.getLogger().error("Canvas type: " + canvasType + " is not a canvas!");
            return null;
        }
        BaseCanvas canvas = (BaseCanvas) (element).cloneWithNewVariables(
                id,
                config,
                null,
                null
        );
        getAtumMod().getLogger().info("Found canvas: " + canvas.getTemplateId());
        canvas.applyResolutionOptimizerGlobally(config);
        return canvas;
    }

    @Override
    public void registerTemplate(@NotNull String id, @NotNull DisplayElement element) {
        registry.put(id, element);
    }

    @Override
    public void unregisterTemplate(@NotNull String id) {
        registry.remove(id);
    }

}
