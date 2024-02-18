package me.phoenixra.atumodcore.core.display.elements.canvas;

import me.phoenixra.atumconfig.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Default canvas.
 */
@RegisterDisplayElement(templateId = "canvas")
public class DefaultCanvas extends BaseCanvas{



    //@TODO move both setup and save to base canvas.
    private SetupCanvas setupCanvas;

    private boolean pressedShift = false;

    public DefaultCanvas(@NotNull AtumMod atumMod, @Nullable DisplayCanvas elementOwner) {
        super(atumMod,elementOwner);
    }

    @Override
    protected void onDraw(DisplayResolution resolution, float scaleFactor, int mouseX, int mouseY) {
        if(setupCanvas != null){
            setupCanvas.onDraw(resolution,scaleFactor,mouseX,mouseY);
        }
    }
    private void save(){
        if(setupCanvas == null) return;
        try {
            if(!(setupCanvas.getSettingsConfig() instanceof LoadableConfig)) return;
            LoadableConfig canvasSettingsConfig = (LoadableConfig) setupCanvas.getSettingsConfig();
            setupCanvas.applyChangesToConfig();
            canvasSettingsConfig.save();
            getAtumMod().getDisplayManager().getElementRegistry().registerTemplate(
                    canvasSettingsConfig.getName(),
                    Objects.requireNonNull(getAtumMod().getDisplayManager().getElementRegistry().compileCanvasTemplate(
                            canvasSettingsConfig.getName(),
                            getSettingsConfig()
                    ))
            );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //@TODO change
/*    @SubscribeEvent
    public void onPressed(ElementInputPressEvent event){
        if(!isActive() && !isSetupState()) return;
        if(setupCanvas != null){
            setupCanvas.onInputPress(event);
        }
        if(getSettingsConfig() == null) return;
        if(event.getParentEvent().getType() == InputType.KEYBOARD_LEFT_ALT && pressedShift) {
            save();
            setSetupState(false);
            pressedShift = false;
            return;
        } else if (pressedShift) {
            pressedShift=false;
            return;
        }
        if(event.getParentEvent().getType() != InputType.KEYBOARD_SHIFT) {
            return;
        }
        if(setupCanvas != null) {
            pressedShift = true;
            return;
        }
        setSetupState(true);

    }
    @SubscribeEvent
    public void onReleased(ElementInputReleaseEvent event){
        if(!isActive() && !isSetupState()) return;
        if(event.getParentEvent().getType() != InputType.KEYBOARD_SHIFT && !isSetupState()) {
            pressedShift = false;
            return;
        }
        if(!isSetupState()) return;
        if(setupCanvas != null){
            setupCanvas.onInputRelease(event);
        }
        if(getSettingsConfig() == null) return;
        if(event.getParentEvent().getType() != InputType.KEYBOARD_SHIFT) {
            return;
        }
        if(setupCanvas != null && pressedShift) {
            setSetupState(false);
            pressedShift = false;
            updateVariables(getSettingsConfig(),null);
        }

    }*/

    @Override
    public boolean isSetupState() {
        return setupCanvas != null;
    }

    @Override
    public void setSetupState(boolean setupState) {
        if(setupState) {
            if(setupCanvas!=null){
                setupCanvas.onRemove();
            }
            setSetupCanvas( new SetupCanvas(getAtumMod(), this) );
            setupCanvas.updateVariables(getSettingsConfig(),null);
            setActive(false);
        } else {
            if(setupCanvas != null) {
                setupCanvas.onRemove();
                setSetupCanvas(null);
                setActive(true);
            }
        }
    }


    @Override
    public void onRemove() {
        super.onRemove();
        if(setupCanvas != null) {
            setupCanvas.onRemove();
            System.out.println("REMOVAL");
            setSetupCanvas(null);
        }
    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        ( (DefaultCanvas)clone).setupCanvas = null;
        return clone;
    }


    public void setSetupCanvas(SetupCanvas setupCanvas) {
        this.setupCanvas = setupCanvas;
    }
}
