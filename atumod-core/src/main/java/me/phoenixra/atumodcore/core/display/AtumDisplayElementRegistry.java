package me.phoenixra.atumodcore.core.display;

import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayElementRegistry;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.core.display.elements.*;
import me.phoenixra.atumodcore.core.display.elements.canvas.DefaultCanvas;
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
                null
        );
        getAtumMod().getLogger().info("Found canvas: " + canvas);
        for(String key : config.getSubsection("elements").getKeys(false)){
            Config elementSection = config.getSubsection("elements." + key);
            String elementType = elementSection.getStringOrDefault("type", "image");
            getAtumMod().getLogger().info("Found element: " + elementType);
            DisplayElement elementElement = this.getElementTemplate(elementType);
            if(elementElement == null){
                this.atumMod.getLogger().error("Could not find element type: " + elementType);
                continue;
            }
            if(!(elementElement instanceof BaseElement)){
                this.atumMod.getLogger().error("Element type: " + elementType + " is not an element!");
                continue;
            }
            BaseElement elementBaseElement = (BaseElement)( elementElement).cloneWithNewVariables(
                    key,
                    elementSection,
                    key
            );
            elementBaseElement.setElementOwner(canvas);
            canvas.addElement(elementBaseElement);
        }
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
