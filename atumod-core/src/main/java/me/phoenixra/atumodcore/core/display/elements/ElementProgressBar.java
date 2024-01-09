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

    private AtumColor barColorFilled = AtumColor.BLACK;
    private AtumColor barColorEmpty = AtumColor.GREEN;

    private String progressExpression;


    private int timer;
    public ElementProgressBar(@NotNull AtumMod atumMod,
                              @NotNull DisplayCanvas elementOwner) {
        super(atumMod,elementOwner);
    }

    @Override
    protected void onDraw(float scaleFactor, float scaleX, float scaleY,
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
    public void updateVariables(@NotNull Config config,@Nullable String configKey) {
        super.updateVariables(config,configKey);
        String color = config.getStringOrNull("settings.color-filled");
        if(color!=null){
            this.barColorFilled = AtumColor.fromHex(color);
        }
        color = config.getStringOrNull("settings.color-empty");
        if(color!=null){
            this.barColorEmpty = AtumColor.fromHex(color);
        }
        String progressExpression = config.getStringOrNull("settings.progress-expression");
        if(progressExpression!=null){
            this.progressExpression = progressExpression;
        }
    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        ElementProgressBar cloneImage = (ElementProgressBar) clone;
        if(cloneImage.barColorFilled !=null) {
            cloneImage.barColorFilled = new AtumColor(cloneImage.barColorFilled.getRed(), cloneImage.barColorFilled.getGreen(), cloneImage.barColorFilled.getBlue(), cloneImage.barColorFilled.getAlpha());
        }
        if(cloneImage.barColorEmpty !=null) {
            cloneImage.barColorEmpty = new AtumColor(cloneImage.barColorEmpty.getRed(), cloneImage.barColorEmpty.getGreen(), cloneImage.barColorEmpty.getBlue(), cloneImage.barColorEmpty.getAlpha());
        }
        return cloneImage;
    }
}
