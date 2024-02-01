package me.phoenixra.atumodcore.api.display.impl;

import com.google.common.collect.Lists;
import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.display.*;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.events.display.ElementInputPressEvent;
import me.phoenixra.atumodcore.api.events.display.ElementInputReleaseEvent;
import me.phoenixra.atumodcore.api.input.InputType;
import me.phoenixra.atumodcore.api.events.input.InputPressEvent;
import me.phoenixra.atumodcore.api.events.input.InputReleaseEvent;
import me.phoenixra.atumodcore.api.placeholders.types.injectable.StaticPlaceholder;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.*;

public abstract class BaseCanvas extends BaseElement implements DisplayCanvas, Cloneable {

    @Getter
    private DisplayRenderer displayRenderer;

    private HashMap<DisplayLayer, LinkedHashSet<DisplayElement>> elements = new HashMap<>();
    @Getter
    private HashSet<DisplayElement> displayedElements = new LinkedHashSet<>();
    private List<DisplayElement> displayedElementsReversed = new ArrayList<>();
    private boolean pressedShift;

    private boolean initialized = false;

    public BaseCanvas(@NotNull AtumMod atumMod,@NotNull DisplayLayer layer,
                      int x,
                      int y,
                      int width,
                      int height,
                      @Nullable DisplayCanvas elementOwner){
        super(atumMod,layer, x, y, width, height, elementOwner);

        this.setElementOwner(elementOwner == null ? this : elementOwner);
        if(elementOwner!=null && elementOwner.getDisplayRenderer() != null){
            this.setDisplayRenderer(elementOwner.getDisplayRenderer());
        }
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
    public void draw(@NotNull DisplayResolution resolution, float scaleFactor, int mouseX, int mouseY) {
        if(!initialized && getElementOwner() == this){
            setActive(true);
            initialized = true;
        }
        super.draw(resolution, scaleFactor, mouseX, mouseY);

        if(displayedElementsReversed.isEmpty()){
            return;
        }

        boolean positionAtBeginning = getOriginX().getDefaultValue() == 0 &&
                getOriginY().getDefaultValue() == 0;

         //translate position to the x and y to make elements relative to the canvas
        if(!positionAtBeginning) {
            GL11.glPushMatrix();
            GL11.glTranslatef(getX(), getY(), 0);
            for (DisplayElement element : displayedElementsReversed) {
                if ((getDisplayRenderer() == null && element.isActive())
                        ||
                        (getDisplayRenderer() != null &&
                                getDisplayRenderer().getDisplayData()
                                .isElementEnabled(element.getId()))) {
                    element.draw(resolution, scaleFactor, mouseX, mouseY);
                }
            }
            GL11.glPopMatrix();
        }else{
            for (DisplayElement element : displayedElementsReversed) {
                if ((getDisplayRenderer() == null && element.isActive())
                        ||
                        (getDisplayRenderer() != null &&
                                getDisplayRenderer().getDisplayData()
                                        .isElementEnabled(element.getId()))) {
                    element.draw(resolution, scaleFactor, mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public void updateBaseVariables(@NotNull Config config, @Nullable String configKey) {
        config.clearInjectedPlaceholders();
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
        super.updateBaseVariables(config, configKey);


        DisplayElementRegistry registry = getAtumMod().getDisplayManager().getElementRegistry();
        for(String key : config.getSubsection("elements").getKeys(false)){
            Config elementSection = config.getSubsection("elements." + key);
            String elementType = elementSection.getStringOrDefault("template", "image");
            getAtumMod().getLogger().info("Found element: " + elementType);
            DisplayElement elementElement = registry.getElementTemplate(elementType);
            if(elementElement == null){
                getAtumMod().getLogger().error("Could not find element template: " + elementType);
                continue;
            }
            if(!(elementElement instanceof BaseElement)){
                getAtumMod().getLogger().error("Element template: " + elementType + " is not a BaseElement!");
                continue;
            }
            BaseElement elementBaseElement = (BaseElement)( elementElement).cloneWithNewVariables(
                    elementSection,
                    key,
                    this
            );
            elementBaseElement.setElementOwner(this);
            this.addElement(elementBaseElement);
        }


    }

    @Override
    public void updateElementVariables(@NotNull Config config) {
        //do nothing
    }


    @Override
    public void reloadCanvas() {
        try {
            if(!(getSettingsConfig() instanceof LoadableConfig) ||
            getDisplayRenderer() == null){
                return;
            }
            LoadableConfig config = (LoadableConfig) getSettingsConfig();
            config.reload();
            getAtumMod().getDisplayManager().getElementRegistry().registerTemplate(
                    config.getName(),
                    Objects.requireNonNull(getAtumMod().getDisplayManager().getElementRegistry().compileCanvasTemplate(
                            config.getName(),
                            getSettingsConfig()
                    ))
            );
            getDisplayRenderer().reloadRenderer();
        }catch (Exception e){
            e.printStackTrace();
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
        setActive(false);
        elements.clear();
        displayedElements.clear();
        displayedElementsReversed.clear();
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
        element.setElementOwner(this);
        if((element instanceof DisplayCanvas) && getDisplayRenderer() != null){
            ((DisplayCanvas) element).setDisplayRenderer(getDisplayRenderer());
        }
        updateDisplayedElements();
    }

    @Override
    public @Nullable DisplayElement getElement(@NotNull String id) {
        if(id.contains("#")){
            String[] split = id.split("#");
            if(split.length != 2){
                return null;
            }
            DisplayElement element = getElement(split[0]);
            if(element == null){
                return null;
            }
            if(!(element instanceof DisplayCanvas)){
                return null;
            }
            return ((DisplayCanvas) element).getElement(split[1]);
        }else {
            if(getId().equals(id)){
                return this;
            }
            for (LinkedHashSet<DisplayElement> list : elements.values()) {
                for (DisplayElement element : list) {
                    if (element.getId().equals(id)) {
                        return element;
                    }
                }
            }
            return null;
        }
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
            displayedElements.addAll(list);
        }
        //add to reversed set
        displayedElementsReversed.clear();
        displayedElementsReversed.addAll(displayedElements);
        Collections.reverse(displayedElementsReversed);
    }



    @Override
    public void setDisplayRenderer(@NotNull DisplayRenderer displayRenderer) {
        this.displayRenderer = displayRenderer;
        for(LinkedHashSet<DisplayElement> list : elements.values()){
            for(DisplayElement element : list){
                if(element instanceof DisplayCanvas){
                    ((DisplayCanvas) element).setDisplayRenderer(displayRenderer);
                }
            }
        }
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


    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return getElementOwner() == this ? isCoordinateInElement(mouseX, mouseY) :
                getElementOwner().getHoveredElement(mouseX, mouseY) == this;
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
    public void applyResolutionOptimizerGlobally(@NotNull Config config) {
        for(String key : config.getSubsection("resolution_optimization").getKeys(false)){
            try {
                DisplayResolution resolution = DisplayResolution.valueOf(key);
                applyResolutionOptimizer(
                        resolution,
                        config.getSubsection("resolution_optimization."+key)
                );
            }catch (IllegalArgumentException e){
                getAtumMod().getLogger().warn("Could not find resolution: " + key);
            }
        }
    }

    @Override
    public void applyResolutionOptimizer(@NotNull DisplayResolution resolution, @NotNull Config config) {
        super.applyResolutionOptimizer(resolution, config);
        for(DisplayElement element : displayedElements){
            element.applyResolutionOptimizer(
                    resolution,
                    config.getSubsection("elements."+element.getId())
            );
        }
    }

    @SubscribeEvent
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

    @SubscribeEvent
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
    @SubscribeEvent
    public void onPressed1(ElementInputPressEvent event){
        if(!isActive() || (getDisplayRenderer() == null
                || getDisplayRenderer().getBaseCanvas() != this)) return;

        if(event.getParentEvent().getType() == InputType.KEYBOARD_KEY) {
            if(!getAtumMod().isDebugEnabled()) return;
            if (event.getParentEvent().getKeyboardCharacter() == 'S' && pressedShift) {
                getAtumMod().getLogger().info("Reloading");
                reloadCanvas();
                pressedShift = false;
            } else if (pressedShift) {
                pressedShift = false;
            }
        }else if(event.getParentEvent().getType() == InputType.KEYBOARD_SHIFT
                && !isSetupState()){
            pressedShift = true;
        }

    }
    @SubscribeEvent
    public void onReleased1(ElementInputReleaseEvent event){
        if(event.getParentEvent().getType() == InputType.KEYBOARD_SHIFT){
            pressedShift = false;
        }

    }

    @Override
    public final @NotNull DisplayElement clone() {
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

}
