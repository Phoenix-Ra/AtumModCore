package me.phoenixra.atumodcore.api.display.impl;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.variables.ConfigVariable;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayLayer;
import me.phoenixra.atumodcore.api.input.InputType;
import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import me.phoenixra.atumodcore.api.input.event.InputReleaseEvent;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class BaseCanvas implements DisplayCanvas, Cloneable {

    @Getter
    private String id = UUID.randomUUID().toString();
    @Getter
    private DisplayLayer layer;
    private int originX;
    private int originY;
    private int originWidth;
    private int originHeight;

    @Getter
    private int x;
    @Getter
    private int y;
    @Getter
    private int width;
    @Getter
    private int height;

    private boolean fixRatio = false;

    @Getter @Setter
    private DisplayCanvas elementOwner;

    private HashMap<DisplayLayer, LinkedHashSet<DisplayElement>> elements = new HashMap<>();

    @Getter
    private HashSet<DisplayElement> displayedElements = new LinkedHashSet<>();
    private List<DisplayElement> displayedElementsReversed = new ArrayList<>();

    public BaseCanvas(@NotNull DisplayLayer layer,
                      int x,
                      int y,
                      int width,
                      int height,
                      @Nullable DisplayCanvas elementOwner){
        this.layer = layer;
        this.x = this.originX = x;
        this.y = this.originY = y;
        this.width = this.originWidth = width;
        this.height = this.originHeight = height;

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
            this.x = this.originX = Integer.parseInt(x.getValue().toString());
        }
        ConfigVariable<?> y = variables.get("posY");
        if(y != null){
            this.y = this.originY = Integer.parseInt(y.getValue().toString());
        }
        ConfigVariable<?> width = variables.get("width");
        if(width != null){
            this.width = this.originWidth = Integer.parseInt(width.getValue().toString());
        }
        ConfigVariable<?> height = variables.get("height");
        if(height != null){
            this.height = this.originHeight = Integer.parseInt(height.getValue().toString());
        }
        ConfigVariable<?> fixRatio = variables.get("fixRatio");
        if(fixRatio != null){
            this.fixRatio = Boolean.parseBoolean(fixRatio.getValue().toString());
        }

    }

    @Override
    public void updateVariables(@NotNull Config config) {

        String layer = config.getStringOrNull("layer");
        if(layer != null){
            this.layer = DisplayLayer.valueOf(layer.toUpperCase());
        }
        this.x = this.originX = config.getInt("posX");
        this.y = this.originY = config.getInt("posY");
        this.width = this.originWidth = config.getInt("width");
        this.height = this.originHeight = config.getInt("height");
        this.fixRatio = config.getBool("fixRatio");

    }

    @Override
    public void addElement(@NotNull DisplayElement element) {

        //add to elements
        if(elements.containsKey(element.getLayer())){
            elements.get(element.getLayer()).add(element);
        }else{
            LinkedHashSet<DisplayElement> list = new LinkedHashSet<>();
            list.add(element);
            elements.put(element.getLayer(), list);
        }
        displayedElements.clear();
        //add to displayedElements
        for(DisplayLayer displayLayer : DisplayLayer.valuesOrderedFromHighest()){
            Set<DisplayElement> list = elements.get(displayLayer);
            System.out.println("layer loading: "+displayLayer + " list: "+list);
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
        System.out.println("Displayed elements: " + displayedElements+"\n Elements: "+elements);
        //add to reversed set
        displayedElementsReversed.clear();
        displayedElementsReversed.addAll(displayedElements);
        Collections.reverse(displayedElementsReversed);

    }

    @Override
    public DisplayElement getHoveredElement(int mouseX, int mouseY) {

        //it works good because it is in linked set and it is ordered from highest to lowest layer
        for (DisplayElement element : displayedElements) {
            if (element.isCoordinateInElement(mouseX, mouseY)) {
                return element;
            }
        }
        return null;
    }

    @Override
    public DisplayElement getElementFromCoordinates(int posX, int posY) {
        for(DisplayElement element : displayedElements){
            if(element.isCoordinateInElement(posX,posY)){
                return element;
            }
        }
        return null;
    }

    @Override
    public void draw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        int[] coords = RenderUtils.fixCoordinates(originX,originY,originWidth,originHeight,fixRatio);
        x = coords[0];
        y = coords[1];
        width = coords[2];
        height = coords[3];
        for(DisplayElement element : displayedElementsReversed){
            element.draw(scaleFactor, scaleX, scaleY, mouseX, mouseY);
        }
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return getElementOwner() == this ? isCoordinateInElement(mouseX, mouseY) :
                getElementOwner().getHoveredElement(mouseX, mouseY) == this;
    }

    @Override
    public void onPress(InputPressEvent event) {
        if(event.getType() == InputType.MOUSE_LEFT || event.getType() == InputType.MOUSE_RIGHT){
            System.out.println("PRESSSSSSED mouseX: "+event.getMouseX()+" mouseY: "+event.getMouseY());
            DisplayElement element = getElementFromCoordinates(event.getMouseX(),event.getMouseY());
            if(element != null){
                element.onPress(event);
            }
        }
    }

    @Override
    public void onRelease(InputReleaseEvent event) {
        if(event.getType() == InputType.MOUSE_LEFT || event.getType() == InputType.MOUSE_RIGHT){
            DisplayElement element = getElementFromCoordinates(event.getMouseX(),event.getMouseY());
            if(element != null){
                element.onRelease(event);
            }
        }
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
            return onClone(clone);
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    protected abstract BaseCanvas onClone(BaseCanvas clone);



}
