package me.phoenixra.atumodcore.api.display.impl;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumconfig.api.placeholders.types.injectable.StaticPlaceholder;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumconfig.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.display.*;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.events.display.ElementInputPressEvent;
import me.phoenixra.atumodcore.api.events.display.ElementInputReleaseEvent;
import me.phoenixra.atumodcore.api.input.InputType;
import me.phoenixra.atumodcore.api.events.input.InputPressEvent;
import me.phoenixra.atumodcore.api.events.input.InputReleaseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.*;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;

public abstract class BaseCanvas extends BaseElement implements DisplayCanvas, Cloneable {

    @Getter
    private DisplayRenderer displayRenderer;

    @Getter
    private List<DisplayElement> displayElements = new ArrayList<>();
    private boolean pressedShift;

    @Getter @Setter
    private boolean supportScissor = true;
    private boolean initialized = false;

    public BaseCanvas(@NotNull AtumMod atumMod, int drawPriority,
                      int x,
                      int y,
                      int width,
                      int height,
                      @Nullable DisplayCanvas elementOwner){
        super(atumMod, drawPriority, x, y, width, height, elementOwner);


        this.setElementOwner(elementOwner == null ? this : elementOwner);
        if(elementOwner!=null && elementOwner.getDisplayRenderer() != null){
            this.setDisplayRenderer(elementOwner.getDisplayRenderer());
        }
    }
    public BaseCanvas(@NotNull AtumMod atumMod,
                      int drawPriority,
                      int x,
                      int y,
                      int width,
                      int height){
       this(atumMod, drawPriority, x, y, width, height, null);
    }
    public BaseCanvas(@NotNull AtumMod atumMod, @Nullable DisplayCanvas elementOwner){
        this(atumMod, 0, 0, 0, 0, 0, elementOwner);
    }
    public BaseCanvas(@NotNull AtumMod atumMod){
        this(atumMod, 0, 0, 0, 0, 0, null);
    }


    @Override
    public void draw(@NotNull DisplayResolution resolution, float scaleFactor, int mouseX, int mouseY) {
        if(!initialized && getElementOwner() == this){
            setActive(true);
            initialized = true;
        }

        super.draw(resolution, scaleFactor, mouseX, mouseY);

        if(displayElements.isEmpty()){
            return;
        }

        boolean positionAtBeginning = getX() == 0 &&
                getY() == 0;
        boolean useScissor = supportScissor
                && getWidth() != Display.getWidth()
                && getHeight() != Display.getHeight();
        if(useScissor) {
            GL11.glEnable(GL_SCISSOR_TEST);
            GL11.glScissor((int) (getX() * scaleFactor),
                    (int) (Display.getHeight() - (getY() + getHeight()) * scaleFactor), //invert y, bcz scissor works in a bottom-left corner
                    (int) (getWidth() * scaleFactor),
                    (int) (getHeight() * scaleFactor)
            );
        }
         //translate position to the x and y to make elements relative to the canvas
        if(!positionAtBeginning) {
            GL11.glPushMatrix();
            GL11.glTranslatef(getX(), getY(), 0);
            for (DisplayElement element : displayElements) {
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
            for (DisplayElement element : displayElements) {
                if ((getDisplayRenderer() == null && element.isActive())
                        ||
                        (getDisplayRenderer() != null &&
                                getDisplayRenderer().getDisplayData()
                                        .isElementEnabled(element.getId()))) {
                    element.draw(resolution, scaleFactor, mouseX, mouseY);
                }
            }
        }
        if(useScissor) {
            GL11.glDisable(GL_SCISSOR_TEST);
        }
    }

    @Override
    public void updateBaseVariables(@NotNull Config config, @Nullable String configKey) {
        config.clearInjectedPlaceholders(true);
        config.addInjectablePlaceholder(
                Lists.newArrayList(
                        new StaticPlaceholder("mouse_x", ()-> String.valueOf(
                                getAtumMod().getInputHandler().getMousePosition().getFirst()
                        )),
                        new StaticPlaceholder("mouse_y", ()-> String.valueOf(
                                getAtumMod().getInputHandler().getMousePosition().getSecond()
                        ))
                ),
                true
        );
        super.updateBaseVariables(config, configKey);

        if(config.hasPath("supportScissor")) {
            supportScissor = config.getBool("supportScissor");
        }
        updateElements(config.getSubsection("elements"));
    }

    public void updateElements(@NotNull Config config){
        clearElements();
        DisplayElementRegistry registry = getAtumMod().getDisplayManager().getElementRegistry();
        for(String key : config.getKeys(false)){
            Config elementSection = config.getSubsection(key);
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
        for(DisplayElement element : displayElements){
            element.onRemove();
        }
        setActive(false);
        displayElements.clear();
        displayRenderer = null;
    }

    @Override
    public void addElement(@NotNull DisplayElement element) {

        //add to elements
        displayElements.add(element);
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
            for (DisplayElement element : displayElements) {
                if (element.getId().equals(id)) {
                    return element;
                }
            }
            return null;
        }
    }

    @Override
    public void removeElement(@NotNull DisplayElement element) {
        displayElements.remove(element);
        element.onRemove();
        updateDisplayedElements();
    }

    @Override
    public void clearElements() {
        for (DisplayElement element : displayElements) {
            element.onRemove();
        }
        displayElements.clear();
        updateDisplayedElements();
    }

    private void updateDisplayedElements(){
        //reversed sort, to have lowest priority on top
        //of the list, to render highest priority last
        displayElements.sort(Comparator.comparingInt(DisplayElement::getDrawPriority));
        //Collections.reverse(displayedElementsReversed);
    }



    @Override
    public void setDisplayRenderer(@NotNull DisplayRenderer displayRenderer) {
        this.displayRenderer = displayRenderer;
        for(DisplayElement element : displayElements){
            if(element instanceof DisplayCanvas){
                ((DisplayCanvas) element).setDisplayRenderer(displayRenderer);
            }
            if(!element.isActive()){
                getDisplayRenderer().getDisplayData().setElementEnabled(
                        element.getId(),false
                );
            }
        }
    }
    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        //all elements
        for(DisplayElement element : displayElements){
            element.setActive(active);
        }
    }


    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return getElementOwner() == this ? isCoordinateInElement(mouseX, mouseY) :
                getElementOwner().getHoveredElement(mouseX, mouseY) == this;
    }
    @Override
    public DisplayElement getHoveredElement(int mouseX, int mouseY) {


        for (int i = displayElements.size()-1; i>=0; i--) {
            DisplayElement element = displayElements.get(i);
            if (element.isActive() && element.isCoordinateInElement(mouseX, mouseY)) {
                return element;
            }
        }
        return null;
    }
    @Override
    public DisplayElement getElementFromCoordinates(int posX, int posY) {
        for (int i = displayElements.size()-1; i>=0; i--) {
            DisplayElement element = displayElements.get(i);
            if (element.isActive() && element.isCoordinateInElement(posX, posY)) {
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
        for(DisplayElement element : displayElements){
            element.applyResolutionOptimizer(
                    resolution,
                    config.getSubsection("elements."+element.getId())
            );
        }
    }

    @SubscribeEvent
    protected void onPress(InputPressEvent event) {
        if(!isActive()){
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
        if(!isActive()){
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
                ){
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
        clone.displayElements = new ArrayList<>();
        clone.setElementOwner(clone);
        clone.initialized = false;
        for(DisplayElement element : displayElements){
            DisplayElement clonedElement = element.clone();
            clonedElement.setElementOwner(clone);
            clone.addElement(clonedElement);
        }
        return clone;
    }

}
