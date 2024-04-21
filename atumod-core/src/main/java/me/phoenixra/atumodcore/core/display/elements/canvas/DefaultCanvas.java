package me.phoenixra.atumodcore.core.display.elements.canvas;


import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Default canvas.
 */
@RegisterDisplayElement(templateId = "canvas")
public class DefaultCanvas extends BaseCanvas{

    public DefaultCanvas(@NotNull AtumMod atumMod, @Nullable DisplayCanvas elementOwner) {
        super(atumMod,elementOwner);
    }

    @Override
    protected void onDraw(DisplayResolution resolution, float scaleFactor, int mouseX, int mouseY) {

    }


    @Override
    public void onRemove() {
        super.onRemove();
    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        return clone;
    }


}
