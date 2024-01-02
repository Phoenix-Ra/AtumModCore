package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementProgressBar extends BaseElement {

    private AtumColor barColorLight = AtumColor.WHITE;
    private AtumColor barColorDark = AtumColor.BLACK;
    private String progressExpression;


    private int timer;
    public ElementProgressBar(@NotNull AtumMod atumMod,
                              @NotNull DisplayCanvas elementOwner) {
        super(atumMod,elementOwner);
    }

    @Override
    protected void onDraw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
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
                        progressExpression, PlaceholderContext.EMPTY
                ),
                barColorLight,
                barColorDark,
                false
        );
    }


    @Override
    public void updateVariables(@NotNull Config config,@Nullable String configKey) {
        super.updateVariables(config,configKey);
        String color = config.getStringOrNull("settings.color-light");
        if(color!=null){
            this.barColorLight = AtumColor.fromHex(color);
        }
        color = config.getStringOrNull("settings.color-dark");
        if(color!=null){
            this.barColorDark = AtumColor.fromHex(color);
        }
        String progressExpression = config.getStringOrNull("settings.progress-expression");
        if(progressExpression!=null){
            this.progressExpression = progressExpression;
        }
    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        ElementProgressBar cloneImage = (ElementProgressBar) clone;
        if(cloneImage.barColorLight!=null) {
            cloneImage.barColorLight = new AtumColor(cloneImage.barColorLight.getRed(), cloneImage.barColorLight.getGreen(), cloneImage.barColorLight.getBlue(), cloneImage.barColorLight.getAlpha());
        }
        if(cloneImage.barColorDark!=null) {
            cloneImage.barColorDark = new AtumColor(cloneImage.barColorDark.getRed(), cloneImage.barColorDark.getGreen(), cloneImage.barColorDark.getBlue(), cloneImage.barColorDark.getAlpha());
        }
        return cloneImage;
    }
}
