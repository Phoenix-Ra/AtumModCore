package me.phoenixra.atumodcore.core.display.elements.canvas;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.LoadableConfig;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.events.display.ElementInputPressEvent;
import me.phoenixra.atumodcore.api.events.display.ElementInputReleaseEvent;
import me.phoenixra.atumodcore.api.input.InputType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class DefaultCanvas extends BaseCanvas{



    //@TODO move both setup and save to base canvas.
    private SetupCanvas setupCanvas;

    private boolean pressedShift = false;

    public DefaultCanvas(@NotNull AtumMod atumMod, @Nullable DisplayCanvas elementOwner) {
        super(atumMod,elementOwner);
    }

    @Override
    protected void onDraw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        if(setupCanvas != null){
            setupCanvas.onDraw(scaleFactor,scaleX,scaleY,mouseX,mouseY);
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
                    getAtumMod().getDisplayManager().getElementRegistry().compileCanvasTemplate(
                            canvasSettingsConfig.getName(),
                            getSettingsConfig()
                    )
            );
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onPressed(ElementInputPressEvent event){
        if(!isActive()) return;
        if(event.getParentEvent().getType() == InputType.KEYBOARD_KEY) {
            if(!getAtumMod().isDebugEnabled()) return;
            getAtumMod().getLogger().info("Pressed: " + event.getParentEvent().getKeyboardCharacter());
            if (event.getParentEvent().getKeyboardCharacter() == 'S' && pressedShift) {
                getAtumMod().getLogger().info("Reloading");
                reloadCanvas();
                pressedShift = false;
                return;
            } else if (pressedShift) {
                pressedShift = false;
                return;
            }
        }else if(event.getParentEvent().getType() == InputType.KEYBOARD_SHIFT && !isSetupState()){
            pressedShift = true;
            return;
        }
        if(!isSetupState()) return;
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
        if(!isActive()) return;
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

    }

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
    protected BaseCanvas onClone(BaseCanvas clone) {
        System.out.println("REMOVAL CLONE");
        ( (DefaultCanvas)clone).setupCanvas = null;
        return clone;
    }


    public void setSetupCanvas(SetupCanvas setupCanvas) {
        this.setupCanvas = setupCanvas;
    }
}
