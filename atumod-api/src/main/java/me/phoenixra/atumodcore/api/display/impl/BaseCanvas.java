package me.phoenixra.atumodcore.api.display.impl;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayLayer;
import me.phoenixra.atumodcore.api.events.display.ElementInputPressEvent;
import me.phoenixra.atumodcore.api.events.display.ElementInputReleaseEvent;
import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import me.phoenixra.atumodcore.api.input.event.InputReleaseEvent;
import me.phoenixra.atumodcore.api.placeholders.types.injectable.StaticPlaceholder;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class BaseCanvas extends BaseElement implements DisplayCanvas, Cloneable {

    @Getter @Setter
    private BaseScreen attachedGuiScreen;

    private HashMap<DisplayLayer, LinkedHashSet<DisplayElement>> elements = new HashMap<>();

    @Getter
    private HashSet<DisplayElement> displayedElements = new LinkedHashSet<>();
    private List<DisplayElement> displayedElementsReversed = new ArrayList<>();

    @Getter
    private LoadableConfig settingsConfig = null;


    private boolean initialized = false;
    public BaseCanvas(@NotNull AtumMod atumMod,@NotNull DisplayLayer layer,
                      int x,
                      int y,
                      int width,
                      int height,
                      @Nullable DisplayCanvas elementOwner){
        super(atumMod,layer, x, y, width, height, elementOwner);

        this.setElementOwner(elementOwner == null ? this : elementOwner);

    }
    public BaseCanvas(@NotNull AtumMod atumMod,
                      @NotNull DisplayLayer layer,
                      int x,
                      int y,
                      int width,
                      int height){
       this(atumMod,layer, x, y, width, height, null);
    }
    public BaseCanvas(@NotNull AtumMod atumMod, @Nullable DisplayCanvas elementOwner){
        this(atumMod,DisplayLayer.MIDDLE, 0, 0, 0, 0, elementOwner);
    }
    public BaseCanvas(@NotNull AtumMod atumMod){
        this(atumMod,DisplayLayer.MIDDLE, 0, 0, 0, 0, null);
    }


    @Override
    public void draw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        if(!initialized && getElementOwner() == this){
            AtumAPI.getInstance().getCoreMod().getInputHandler().addListenerOnPress(getId()+"-press",
                    this::onPress
            );
            AtumAPI.getInstance().getCoreMod().getInputHandler().addListenerOnRelease(getId()+"-release",
                    this::onRelease
            );
            getAtumMod().getEnabledCanvasRegistry().registerCanvas(
                    getConfigKey(),
                    this
            );
            initialized = true;
        }
        super.draw(scaleFactor, scaleX, scaleY, mouseX, mouseY);
        for(DisplayElement element : displayedElementsReversed){
            element.draw(scaleFactor, scaleX, scaleY, mouseX, mouseY);
        }
    }

    @Override
    public void updateVariables(@NotNull Config config, @Nullable String configKey) {
        if(config instanceof LoadableConfig) settingsConfig = (LoadableConfig) config;
        config.addInjectablePlaceholder(
                Lists.newArrayList(
                        new StaticPlaceholder("mouse_x", ()-> String.valueOf(
                                config.getAtumMod().getInputHandler().getMousePosition().getFirst()
                        )),
                        new StaticPlaceholder("mouse_y", ()-> String.valueOf(
                                config.getAtumMod().getInputHandler().getMousePosition().getSecond()
                        ))
                )
        );
        super.updateVariables(config, configKey);

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
        updateDisplayedElements();
    }

    @Override
    public void removeElement(@NotNull DisplayElement element) {
        Set<DisplayElement> list = elements.get(element.getLayer());
        if(list != null){
            list.remove(element);
        }
        element.onRemove();
        updateDisplayedElements();
    }

    @Override
    public void clearElements() {
        for (LinkedHashSet<DisplayElement> list : elements.values()) {
            for (DisplayElement element : list) {
                element.onRemove();
            }
        }
        elements.clear();
        updateDisplayedElements();
    }

    private void updateDisplayedElements(){
        displayedElements.clear();
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
    public boolean isHovered(int mouseX, int mouseY) {
        return getElementOwner() == this ? isCoordinateInElement(mouseX, mouseY) :
                getElementOwner().getHoveredElement(mouseX, mouseY) == this;
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        //all elements
        for(LinkedHashSet<DisplayElement> list : elements.values()){
            for(DisplayElement element : list){
                element.setActive(active);
            }
        }
    }

    protected void onPress(InputPressEvent event) {
        if(!isActive() && !isSetupState()){
            return;
        }

        DisplayElement element = getElementFromCoordinates(event.getMouseX(),event.getMouseY());
        if(element != null){
            if(element instanceof BaseCanvas){
                ((BaseCanvas) element).onPress(event);
                return;
            }
            MinecraftForge.EVENT_BUS.post(new ElementInputPressEvent(element,event));
        }else{
            MinecraftForge.EVENT_BUS.post(new ElementInputPressEvent(this,event));
        }
    }

    protected void onRelease(InputReleaseEvent event) {
        if(!isActive() && !isSetupState()){
            return;
        }

        DisplayElement element = getElementFromCoordinates(event.getMouseX(),event.getMouseY());
        if(element != null){
            if(element instanceof BaseCanvas){
                ((BaseCanvas) element).onRelease(event);
                return;
            }
            MinecraftForge.EVENT_BUS.post(new ElementInputReleaseEvent(element,event));
        }else{
            MinecraftForge.EVENT_BUS.post(new ElementInputReleaseEvent(this,event));
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        for(LinkedHashSet<DisplayElement> list : elements.values()){
            for(DisplayElement element : list){
                element.onRemove();
            }
        }
        AtumAPI.getInstance().getCoreMod().getInputHandler().removeListenerOnPress(getId()+"-press");
        AtumAPI.getInstance().getCoreMod().getInputHandler().removeListenerOnRelease(getId()+"-release");
        setActive(false);
        elements.clear();
        displayedElements.clear();
        displayedElementsReversed.clear();
        getAtumMod().getEnabledCanvasRegistry().unregisterCanvas(getConfigKey());
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
    public final DisplayElement clone() {
        BaseCanvas clone = (BaseCanvas) super.clone();
        HashMap<DisplayLayer, LinkedHashSet<DisplayElement>> map = clone.elements;
        clone.elements = new HashMap<>();
        clone.displayedElementsReversed = new ArrayList<>();
        clone.displayedElements = new LinkedHashSet<>();
        clone.setElementOwner(clone);
        clone.initialized = false;
        for(Map.Entry<DisplayLayer, LinkedHashSet<DisplayElement>> entry : map.entrySet()){
            for(DisplayElement element : entry.getValue()){
                DisplayElement clonedElement = element.clone();
                clonedElement.setElementOwner(clone);
                clone.addElement(clonedElement);
            }
        }
        return clone;
    }

    @Override
    protected final BaseElement onClone(BaseElement clone) {
        return clone;
    }

    protected abstract BaseCanvas onClone(BaseCanvas clone);
}
