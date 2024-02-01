package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayLayer;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Fill element.
 * <br> <br>
 * Settings:
 * <ul>
 *     <li>color - color (RGB)</li>
 *     <li>opacity - opacity</li>
 * </ul>
 */
@RegisterDisplayElement(templateId = "fill")
public class ElementFill extends BaseElement {
    private AtumColor color = AtumColor.WHITE;
    private float opacity;

    public ElementFill(@NotNull AtumMod atumMod,
                       @Nullable DisplayCanvas elementOwner) {
        super(atumMod, elementOwner);
    }

    @Override
    protected void onDraw(DisplayResolution resolution,
                          float scaleFactor,
                          int mouseX, int mouseY) {
        RenderUtils.fill(
                getX(),
                getY(),
                getX()+getWidth(),
                getY()+getHeight(),
                color.toInt(),
                opacity
        );
    }

    @Override
    public void updateElementVariables(@NotNull Config config) {
        String color = config.getStringOrNull("color");
        if(color!=null){
            this.color = AtumColor.fromHex(color);
        }
        this.opacity = (float) config.getDoubleOrDefault("opacity",1);
    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        ElementFill cloneFill = (ElementFill) clone;
        try {
            if (cloneFill.color != null) {
                cloneFill.color = color.clone();
            }
        }catch (CloneNotSupportedException ignored){}
        return cloneFill;
    }
}
