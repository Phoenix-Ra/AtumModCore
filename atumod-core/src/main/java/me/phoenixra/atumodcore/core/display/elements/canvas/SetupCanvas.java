package me.phoenixra.atumodcore.core.display.elements.canvas;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayLayer;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.events.display.ElementInputPressEvent;
import me.phoenixra.atumodcore.api.events.display.ElementInputReleaseEvent;
import me.phoenixra.atumodcore.api.input.CursorType;
import me.phoenixra.atumodcore.api.input.InputType;
import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import me.phoenixra.atumodcore.api.input.event.InputReleaseEvent;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import me.phoenixra.atumodcore.api.utils.GeometryUtils;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;


public class SetupCanvas extends BaseCanvas {

    private LoadableConfig canvasConfig;
    private DefaultCanvas setupOwner;

    private DisplayElement selectedElement;
    private PairRecord<Integer, Integer> mousePos = new PairRecord<>(0, 0);

    private ElementSelectionState selectionState = ElementSelectionState.NONE;
    private ElementSelectionState mouseState = ElementSelectionState.NONE;

    private Set<DisplayElement> modifiedElements = new HashSet<>();

    public SetupCanvas(@NotNull AtumMod atumMod, @NotNull DefaultCanvas setupOwner) {
        super(atumMod, DisplayLayer.FOREGROUND, 0, 0, 1920, 1080);
        this.setupOwner = setupOwner;
    }


    @Override
    public void onRemove() {
        super.onRemove();
        clearSelection();

    }

    @Override
    public void draw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        super.draw(scaleFactor, scaleX, scaleY, mouseX, mouseY);

    }

    @Override
    protected void onDraw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        if (!isActive()) return;
        if (selectedElement == null) return;
        PairRecord<Integer, Integer> newMousePos = getAtumMod().getInputHandler().getMouseOriginPosition();
        double ratioWidth = RenderUtils.getWindowRatioWidth();
        double ratioHeight = RenderUtils.getWindowRatioHeight();
        int x = (int)(newMousePos.getFirst() / ratioWidth) - (int)(mousePos.getFirst() / ratioWidth) ;
        int y = (int)(newMousePos.getSecond() / ratioHeight) - (int)(mousePos.getSecond() / ratioHeight);

        switch (selectionState) {
            case SELECTING:
                switch (ElementSelectionState.getByPosition(
                        mouseX,
                        mouseY,
                        selectedElement.getX(),
                        selectedElement.getY(),
                        selectedElement.getWidth(),
                        selectedElement.getHeight())) {
                    case RESIZING_TOP:
                        getAtumMod().getInputHandler().setCursorType(CursorType.RESIZE_VERTICAL);
                        mouseState = ElementSelectionState.RESIZING_TOP;
                        break;
                    case RESIZING_BOTTOM:
                        getAtumMod().getInputHandler().setCursorType(CursorType.RESIZE_VERTICAL);
                        mouseState = ElementSelectionState.RESIZING_BOTTOM;
                        break;
                    case RESIZING_LEFT:
                        getAtumMod().getInputHandler().setCursorType(CursorType.RESIZE_HORIZONTAL);
                        mouseState = ElementSelectionState.RESIZING_LEFT;
                        break;
                    case RESIZING_RIGHT:
                        getAtumMod().getInputHandler().setCursorType(CursorType.RESIZE_HORIZONTAL);
                        mouseState = ElementSelectionState.RESIZING_RIGHT;
                        break;
                    case RESIZING_TOP_LEFT:
                        getAtumMod().getInputHandler().setCursorType(CursorType.RESIZE_TOP_LEFT);
                        mouseState = ElementSelectionState.RESIZING_TOP_LEFT;
                        break;
                    case RESIZING_TOP_RIGHT:
                        getAtumMod().getInputHandler().setCursorType(CursorType.RESIZE_TOP_RIGHT);
                        mouseState = ElementSelectionState.RESIZING_TOP_RIGHT;
                        break;
                    case RESIZING_BOTTOM_LEFT:
                        getAtumMod().getInputHandler().setCursorType(CursorType.RESIZE_TOP_RIGHT);
                        mouseState = ElementSelectionState.RESIZING_BOTTOM_LEFT;
                        break;
                    case RESIZING_BOTTOM_RIGHT:
                        getAtumMod().getInputHandler().setCursorType(CursorType.RESIZE_TOP_LEFT);
                        mouseState = ElementSelectionState.RESIZING_BOTTOM_RIGHT;
                        break;
                    case MOVING:
                        getAtumMod().getInputHandler().setCursorType(CursorType.POSITIONING);
                        mouseState = ElementSelectionState.MOVING;
                        break;
                    default:
                        getAtumMod().getInputHandler().setCursorType(CursorType.DEFAULT);
                        mouseState = ElementSelectionState.SELECTING;
                        break;
                }

                //if(selectedElement.getOriginX() - newMousePos.getFirst())
                break;
            case MOVING:
                selectedElement.setOriginX(selectedElement.getOriginX() + x);
                selectedElement.setOriginY(selectedElement.getOriginY() + y);
                break;
            case RESIZING_TOP:
                selectedElement.setOriginY(selectedElement.getOriginY() + y);
                selectedElement.setOriginHeight(selectedElement.getOriginHeight() - y);
                break;
            case RESIZING_BOTTOM:
                selectedElement.setOriginHeight(selectedElement.getOriginHeight() + y);
                break;
            case RESIZING_LEFT:
                selectedElement.setOriginX(selectedElement.getOriginX() + x);
                selectedElement.setOriginWidth(selectedElement.getOriginWidth() - x);
                break;
            case RESIZING_RIGHT:
                selectedElement.setOriginWidth(selectedElement.getOriginWidth() + x);
                break;
            case RESIZING_TOP_LEFT:
                selectedElement.setOriginX(selectedElement.getOriginX() + x);
                selectedElement.setOriginY(selectedElement.getOriginY() + y);
                selectedElement.setOriginWidth(selectedElement.getOriginWidth() - x);
                selectedElement.setOriginHeight(selectedElement.getOriginHeight() - y);
                break;
            case RESIZING_TOP_RIGHT:
                selectedElement.setOriginY(selectedElement.getOriginY() + y);
                selectedElement.setOriginWidth(selectedElement.getOriginWidth() + x);
                selectedElement.setOriginHeight(selectedElement.getOriginHeight() - y);
                break;
            case RESIZING_BOTTOM_LEFT:
                selectedElement.setOriginX(selectedElement.getOriginX() + x);
                selectedElement.setOriginWidth(selectedElement.getOriginWidth() - x);
                selectedElement.setOriginHeight(selectedElement.getOriginHeight() + y);
                break;
            case RESIZING_BOTTOM_RIGHT:
                selectedElement.setOriginWidth(selectedElement.getOriginWidth() + x);
                selectedElement.setOriginHeight(selectedElement.getOriginHeight() + y);
                break;
        }
        mousePos = newMousePos;
    }

    @Override
    protected void onPress(InputPressEvent event) {

    }

    @Override
    protected void onRelease(InputReleaseEvent event) {

    }

    @Override
    public void updateVariables(@NotNull Config config, @Nullable String configKey) {
        super.updateVariables(config, configKey);
        if (!(config instanceof LoadableConfig)) return;
        canvasConfig = (LoadableConfig) config;
    }

    private void clearSelection() {
        if (selectedElement != null) {
            selectedElement.setOutline(false);
            selectedElement = null;
            selectionState = ElementSelectionState.NONE;
        }
        getAtumMod().getInputHandler().setCursorType(CursorType.DEFAULT);
    }

    protected void applyChangesToConfig(){
        for(DisplayElement element : modifiedElements){
            if(element.getConfigKey()==null) continue;
            getSettingsConfig().set("elements." + element.getConfigKey() + ".posX", element.getOriginX());
            getSettingsConfig().set("elements." + element.getConfigKey() + ".posY", element.getOriginY());
            getSettingsConfig().set("elements." + element.getConfigKey() + ".width", element.getOriginWidth());
            getSettingsConfig().set("elements." + element.getConfigKey() + ".height", element.getOriginHeight());
        }
    }

    public void onInputPress(ElementInputPressEvent event) {
        if (!isActive()) return;
        if (event.getParentEvent().getType() != InputType.MOUSE_LEFT) return;
        if (selectedElement == null) {
            selectedElement = event.getClickedElement();
            selectedElement.setOutline(true);
            mousePos = getAtumMod().getInputHandler().getMouseOriginPosition();
            mousePos.setFirst((int) (mousePos.getFirst() / RenderUtils.getWindowRatioWidth()));
            mousePos.setSecond((int) (mousePos.getSecond() / RenderUtils.getWindowRatioWidth()));
        } else if (selectedElement != event.getClickedElement()) {
            clearSelection();
        } else {
            selectionState = mouseState!=ElementSelectionState.NONE?mouseState:ElementSelectionState.SELECTING;
        }

    }

    public void onInputRelease(ElementInputReleaseEvent event) {
        if (!isActive()) return;
        if (event.getParentEvent().getType() != InputType.MOUSE_LEFT) return;
        if (selectedElement != null) {
            selectionState = ElementSelectionState.SELECTING;
            if(selectedElement.getConfigKey()==null) return;
            modifiedElements.add(selectedElement);
        }
    }

    @Override
    protected BaseCanvas onClone(BaseCanvas clone) {
        return clone;
    }

    @Override
    public boolean isSetupState() {
        return false;
    }

    @Override
    public void setSetupState(boolean setupState) {
    }


    private static enum ElementSelectionState {
        NONE,
        SELECTING,
        MOVING,
        RESIZING_TOP,
        RESIZING_BOTTOM,
        RESIZING_LEFT,
        RESIZING_RIGHT,
        RESIZING_TOP_LEFT,
        RESIZING_TOP_RIGHT,
        RESIZING_BOTTOM_LEFT,
        RESIZING_BOTTOM_RIGHT;


        public static ElementSelectionState getByPosition(int mouseX,
                                                          int mouseY,
                                                          int rectX,
                                                          int rectY,
                                                          int rectWidth,
                                                          int rectHeight) {


            if (GeometryUtils.isPointInsideRectangle(
                    mouseX,
                    mouseY,
                    rectX,
                    rectY,
                    rectWidth,
                    rectHeight
            )) {
                if (GeometryUtils.isPointInsideRectangle(
                        mouseX,
                        mouseY,
                        rectX,
                        rectY+rectHeight/4,
                        rectWidth/4,
                        rectHeight/2
                )) {
                    return RESIZING_LEFT;
                }else if (
                        GeometryUtils.isPointInsideRectangle(
                                mouseX,
                                mouseY,
                                rectX+rectWidth-rectWidth/4,
                                rectY+rectHeight/4,
                                rectWidth/4,
                                rectHeight/2
                        )
                ) {
                    return RESIZING_RIGHT;
                }else if (
                        GeometryUtils.isPointInsideRectangle(
                                mouseX,
                                mouseY,
                                rectX+rectWidth/4,
                                rectY,
                                rectWidth/2,
                                rectHeight/4
                        )
                ) {
                    return RESIZING_TOP;
                }else if (
                        GeometryUtils.isPointInsideRectangle(
                                mouseX,
                                mouseY,
                                rectX+rectWidth/4,
                                rectY+rectHeight-rectHeight/4,
                                rectWidth/2,
                                rectHeight/4
                        )
                ) {
                    return RESIZING_BOTTOM;
                }else if (
                        GeometryUtils.isPointInsideRectangle(
                                mouseX,
                                mouseY,
                                rectX,
                                rectY,
                                rectWidth/4,
                                rectHeight/4
                        )
                ) {
                    return RESIZING_TOP_LEFT;
                }else if (
                        GeometryUtils.isPointInsideRectangle(
                                mouseX,
                                mouseY,
                                rectX+rectWidth-rectWidth/4,
                                rectY,
                                rectWidth/4,
                                rectHeight/4
                        )
                ) {
                    return RESIZING_TOP_RIGHT;
                }else if (
                        GeometryUtils.isPointInsideRectangle(
                                mouseX,
                                mouseY,
                                rectX,
                                rectY+rectHeight-rectHeight/4,
                                rectWidth/4,
                                rectHeight/4
                        )
                ) {
                    return RESIZING_BOTTOM_LEFT;
                }else if (
                        GeometryUtils.isPointInsideRectangle(
                                mouseX,
                                mouseY,
                                rectX+rectWidth-rectWidth/4,
                                rectY+rectHeight-rectHeight/4,
                                rectWidth/4,
                                rectHeight/4
                        )
                ) {
                    return RESIZING_BOTTOM_RIGHT;
                } else {
                    return MOVING;
                }
            }
            return NONE;

        }
    }


    //add actions to modify the element.
    //Add method which will convert the element to a config.
    //
}
