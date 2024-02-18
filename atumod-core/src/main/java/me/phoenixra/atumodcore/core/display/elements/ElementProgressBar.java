package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumconfig.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumconfig.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Progress bar element.
 * <br> <br>
 * Settings:
 * <ul>
 *     <li>color-filled - filled color (RGB)</li>
 *     <li>color-empty - empty color (RGB)</li>
 *     <li>progress-expression - progress expression</li>
 * </ul>
 * You can use placeholders in progress expression.
 */
@RegisterDisplayElement(templateId = "progress_bar")
public class ElementProgressBar extends BaseElement {

    private AtumColor barColorFilled = AtumColor.BLACK;
    private AtumColor barColorEmpty = AtumColor.GREEN;

    private String progressExpression;


    private int timer;
    public ElementProgressBar(@NotNull AtumMod atumMod,
                              @NotNull DisplayCanvas elementOwner) {
        super(atumMod,elementOwner);
    }

    @Override
    protected void onDraw(DisplayResolution resolution, float scaleFactor,
                          int mouseX, int mouseY) {
        timer++;
        if(timer>100){
            timer = 0;
        }
        RenderUtils.drawCustomBar(
                getX(),
                getY(),
                getWidth(),
                getHeight(),
                AtumAPI.getInstance().evaluate(
                        getAtumMod(),
                        progressExpression,
                        PlaceholderContext.of(
                                getElementOwner().getDisplayRenderer()
                        )
                ),
                barColorEmpty,
                barColorFilled
        );
    }


    @Override
    public void updateElementVariables(@NotNull Config config) {
        String color = config.getStringOrNull("color-filled");
        if(color!=null){
            this.barColorFilled = AtumColor.fromHex(color);
        }
        color = config.getStringOrNull("color-empty");
        if(color!=null){
            this.barColorEmpty = AtumColor.fromHex(color);
        }
        String progressExpression = config.getStringOrNull("progress-expression");
        if(progressExpression!=null){
            this.progressExpression = progressExpression;
        }
    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        ElementProgressBar cloneImage = (ElementProgressBar) clone;
        try {
            if (cloneImage.barColorFilled != null) {
                cloneImage.barColorFilled = barColorFilled.clone();
            }
            if(cloneImage.barColorEmpty !=null) {
                cloneImage.barColorEmpty = barColorEmpty.clone();
            }
        }catch (CloneNotSupportedException ignored){}
        return cloneImage;
    }
}
