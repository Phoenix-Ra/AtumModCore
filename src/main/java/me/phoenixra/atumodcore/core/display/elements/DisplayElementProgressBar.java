package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import org.jetbrains.annotations.NotNull;

public class DisplayElementProgressBar extends BaseElement {

    private AtumColor barColorLight = AtumColor.WHITE;
    private AtumColor barColorDark = AtumColor.BLACK;
    private String progressExpression;

    private boolean outlined;

    private int timer;
    public DisplayElementProgressBar(@NotNull DisplayCanvas elementOwner) {
        super(elementOwner);
    }

    @Override
    public void draw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        super.draw(scaleFactor, scaleX, scaleY, mouseX, mouseY);

        timer++;
        if(timer>100){
            timer = 0;
        }
        RenderUtils.drawCustomBar(
                getX(),
                getY(),
                getWidth(),
                getHeight(),
                timer,
                barColorLight,
                barColorDark,
                outlined
        );
        /*AtumAPI.getInstance().evaluate(
                        AtumModCore.getInstance(),
                        progressExpression, PlaceholderContext.EMPTY
                )*/
    }


    @Override
    public void updateVariables(@NotNull Config config) {
        super.updateVariables(config);
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
        this.outlined = config.getBool("settings.outlined");
    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        DisplayElementProgressBar cloneImage = (DisplayElementProgressBar) clone;
        if(cloneImage.barColorLight!=null) {
            cloneImage.barColorLight = new AtumColor(cloneImage.barColorLight.getRed(), cloneImage.barColorLight.getGreen(), cloneImage.barColorLight.getBlue(), cloneImage.barColorLight.getAlpha());
        }
        if(cloneImage.barColorDark!=null) {
            cloneImage.barColorDark = new AtumColor(cloneImage.barColorDark.getRed(), cloneImage.barColorDark.getGreen(), cloneImage.barColorDark.getBlue(), cloneImage.barColorDark.getAlpha());
        }
        return cloneImage;
    }
}
