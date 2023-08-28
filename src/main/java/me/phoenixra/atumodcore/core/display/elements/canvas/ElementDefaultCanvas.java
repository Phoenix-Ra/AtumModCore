package me.phoenixra.atumodcore.core.display.elements.canvas;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.events.display.ElementInputPressEvent;
import me.phoenixra.atumodcore.api.input.InputType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ElementDefaultCanvas extends BaseCanvas {


    private ElementSetupCanvas setupCanvas;

    public ElementDefaultCanvas(@NotNull AtumMod atumMod, @Nullable DisplayCanvas elementOwner) {
        super(atumMod,elementOwner);
    }

    @Override
    public void draw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        if(setupCanvas != null){
            setupCanvas.draw(scaleFactor,scaleX,scaleY,mouseX,mouseY);
            return;
        }
        super.draw(scaleFactor,scaleX,scaleY,mouseX,mouseY);

    }

    @Override
    protected void onDraw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        //empty
    }

    @Override
    protected BaseCanvas onClone(BaseCanvas clone) {
        return clone;
    }

    @SubscribeEvent
    public void onPressedShift(ElementInputPressEvent event){
        if(getSettingsConfig() == null) return;
        if(event.getParentEvent().getType() != InputType.KEYBOARD_SHIFT) {
            return;
        }
        if(setupCanvas != null) {
            setupCanvas.onRemove();
            setupCanvas = null;
            setActive(true);
            return;
        }
        setupCanvas = new ElementSetupCanvas(getAtumMod());
        setupCanvas.updateVariables(getSettingsConfig());
        setActive(false);

    }
}
