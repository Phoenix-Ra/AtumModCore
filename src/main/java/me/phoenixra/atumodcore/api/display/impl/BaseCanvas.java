package me.phoenixra.atumodcore.api.display.impl;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.variables.ConfigVariable;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayLayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class BaseCanvas implements DisplayCanvas, Cloneable {

    @Getter
    private String id = UUID.randomUUID().toString();
    @Getter
    private DisplayLayer layer;
    @Getter
    private int x;
    @Getter
    private int y;
    @Getter
    private int width;
    @Getter
    private int height;
    @Getter @Setter
    private DisplayCanvas elementOwner;

    private HashMap<DisplayLayer, LinkedHashSet<DisplayElement>> elements = new HashMap<>();

    @Getter
    private HashSet<DisplayElement> displayedElements = new LinkedHashSet<>();


    private DisplayElement lastHoveredElement = null;

    public BaseCanvas(@NotNull DisplayLayer layer,
                      int x,
                      int y,
                      int width,
                      int height,
                      @Nullable DisplayCanvas elementOwner){
        this.layer = layer;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.elementOwner = elementOwner == null ? this : elementOwner;
    }
    public BaseCanvas(@NotNull DisplayLayer layer,
                      int x,
                      int y,
                      int width,
                      int height){
       this(layer, x, y, width, height, null);
    }
    public BaseCanvas(@Nullable DisplayCanvas elementOwner){
        this(DisplayLayer.MIDDLE, 0, 0, 0, 0, elementOwner);
    }
    public BaseCanvas(){
        this(DisplayLayer.MIDDLE, 0, 0, 0, 0, null);
    }

    @Override
    public void updateVariables(@NotNull HashMap<String, ConfigVariable<?>> variables) {
        ConfigVariable<?> id = variables.get("id");
        if(id != null){
            this.id = id.getValue().toString();
        }
        ConfigVariable<?> layer = variables.get("layer");
        if(layer != null){
            this.layer = DisplayLayer.valueOf(layer.getValue().toString().toUpperCase());
        }
        ConfigVariable<?> x = variables.get("posX");
        if(x != null){
            this.x = Integer.parseInt(x.getValue().toString());
        }
        ConfigVariable<?> y = variables.get("posY");
        if(y != null){
            this.y = Integer.parseInt(y.getValue().toString());
        }
        ConfigVariable<?> width = variables.get("width");
        if(width != null){
            this.width = Integer.parseInt(width.getValue().toString());
        }
        ConfigVariable<?> height = variables.get("height");
        if(height != null){
            this.height = Integer.parseInt(height.getValue().toString());
        }
    }

    @Override
    public void updateVariables(@NotNull Config config) {

        String layer = config.getStringOrNull("layer");
        if(layer != null){
            this.layer = DisplayLayer.valueOf(layer.toUpperCase());
        }
        this.x = config.getInt("posX");
        this.y = config.getInt("posY");
        this.width = config.getInt("width");
        this.height = config.getInt("height");
    }

    @Override
    public void addElement(@NotNull DisplayElement element) {

        //add to elements
        if(elements.containsKey(layer)){
            elements.get(layer).add(element);
        }else{
            LinkedHashSet<DisplayElement> list = new LinkedHashSet<>();
            list.add(element);
            elements.put(layer, list);
        }

        //add to displayedElements
        for(DisplayLayer displayLayer : DisplayLayer.valuesOrderedFromHighest()){
            Set<DisplayElement> list = elements.get(displayLayer);
            if(list == null){
                continue;
            }
            //check if different element is already displayed on the same position
            for(DisplayElement layerElement : list){
                if(displayedElements.stream().noneMatch(layerElement::isElementInsideThis)){
                    displayedElements.add(layerElement);
                }
            }
        }

    }

    @Override
    public DisplayElement getHoveredElement(int mouseX, int mouseY) {
        if(lastHoveredElement != null && lastHoveredElement.isCoordinateInElement(mouseX,mouseY)){
            return lastHoveredElement;
        }
        //it works good because it is in linked set and it is ordered from highest to lowest layer
        for(DisplayElement element : displayedElements){
            if(element.isCoordinateInElement(mouseX,mouseY)){
                lastHoveredElement = element;
                return element;
            }
        }
        return null;
    }

    @Override
    public DisplayElement getElementFromCoordinates(int posX, int posY) {
        for(DisplayElement element : displayedElements){
            if(element.isCoordinateInElement(posX,posY)){
                lastHoveredElement = element;
                return element;
            }
        }
        return null;
    }

    @Override
    public void draw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        for(DisplayElement element : displayedElements){
            element.draw(scaleFactor, scaleX, scaleY, mouseX, mouseY);
        }
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return getElementOwner() == this ? isCoordinateInElement(mouseX, mouseY) :
                getElementOwner().getHoveredElement(mouseX, mouseY) == this;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BaseCanvas){
            BaseCanvas canvas = (BaseCanvas) obj;
            return canvas.getId().equals(getId());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }


    @Override
    public BaseCanvas clone() {
        try {
            BaseCanvas clone = (BaseCanvas) super.clone();
            clone.id = UUID.randomUUID().toString();
            clone.elements = new HashMap<>();
            clone.displayedElements = new LinkedHashSet<>();
            clone.lastHoveredElement = null;
            return onClone(clone);
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    protected abstract BaseCanvas onClone(BaseCanvas clone);
}
