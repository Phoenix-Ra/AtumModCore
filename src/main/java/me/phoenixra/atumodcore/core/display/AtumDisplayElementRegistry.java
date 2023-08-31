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
import me.phoenixra.atumodcore.core.display.elements.canvas.ElementDefaultCanvas;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class AtumDisplayElementRegistry implements DisplayElementRegistry {
    @Getter
    private final AtumMod atumMod;

    private HashMap<String, DisplayElement> registry = new HashMap<>();
    public AtumDisplayElementRegistry(AtumMod atumMod) {
        this.atumMod = atumMod;
        register("canvas", new ElementDefaultCanvas(atumMod,null));
        register("image", new ElementImage(atumMod,null));
        register("text", new ElementText(atumMod,null));
        register("button", new ElementButton(atumMod,null));
        register("progress_bar", new ElementProgressBar(atumMod,null));
    }
    @Override
    public @Nullable DisplayElement getElementById(@NotNull String id) {
        return registry.get(id.toLowerCase());
    }

    @Override
    public @Nullable DisplayCanvas getCanvasById(@NotNull String id) {
        DisplayElement element = this.getElementById(id);
        if(!(element instanceof DisplayCanvas)){
            return null;
        }
        return (DisplayCanvas) element;
    }

    @Override
    public DisplayCanvas compile(@NotNull Config config) {

        String canvasType = config.getStringOrDefault("type","canvas");
        DisplayElement element = this.getElementById(canvasType);
        if(element == null){
            this.atumMod.getLogger().error("Could not find canvas type: " + canvasType);
            return null;
        }
        if(!(element instanceof BaseCanvas)){
            this.atumMod.getLogger().error("Canvas type: " + canvasType + " is not a canvas!");
            return null;
        }
        BaseCanvas canvas = (BaseCanvas) (element).clone();
        canvas.updateVariables(config,null);
        getAtumMod().getLogger().info("Found canvas: " + canvas);
        for(String key : config.getSubsection("elements").getKeys(false)){
            Config elementSection = config.getSubsection("elements." + key);
            String elementType = elementSection.getStringOrDefault("type", "image");
            getAtumMod().getLogger().info("Found element: " + elementType);
            DisplayElement elementElement = this.getElementById(elementType);
            if(elementElement == null){
                this.atumMod.getLogger().error("Could not find element type: " + elementType);
                continue;
            }
            if(!(elementElement instanceof BaseElement)){
                this.atumMod.getLogger().error("Element type: " + elementType + " is not an element!");
                continue;
            }
            BaseElement elementBaseElement = (BaseElement)( elementElement).clone();
            elementBaseElement.setElementOwner(canvas);
            elementBaseElement.updateVariables(elementSection, key);
            canvas.addElement(elementBaseElement);
        }
        return canvas;
    }

    @Override
    public void register(@NotNull String id, @NotNull DisplayElement element) {
        registry.put(id, element);
    }

    @Override
    public void unregister(@NotNull String id) {
        registry.remove(id);
    }

}
