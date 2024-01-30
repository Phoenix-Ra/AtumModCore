package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.display.misc.DisplayResolution;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.utils.PlayerUtils;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Image element.
 * <br> <br>
 * Settings:
 * <ul>
 *     <li>color - color (RGB)</li>
 *     <li>textureX - texture X</li>
 *     <li>textureY - texture Y</li>
 *     <li>textureWidth - texture width</li>
 *     <li>textureHeight - texture height</li>
 * </ul>
 *
 */
@RegisterDisplayElement(templateId = "image_player_skin")
public class ElementPlayerSkin extends BaseElement {
    private Runnable imageBinder;
    private AtumColor color = AtumColor.WHITE;
    private int textureX;
    private int textureY;
    private int textureWidth;
    private int textureHeight;
    public ElementPlayerSkin(@NotNull AtumMod atumMod,
                             @NotNull DisplayCanvas elementOwner) {
        super(atumMod,elementOwner);

    }

    @Override
    protected void onDraw(DisplayResolution resolution,
                          float scaleFactor, int mouseX, int mouseY) {
        imageBinder.run();
        color.useColor();
        if(textureHeight==0||textureWidth==0){
            RenderUtils.drawCompleteImage(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight()
            );
        }else {
            RenderUtils.drawPartialImage(
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight(),
                    textureX,
                    textureY,
                    textureWidth,
                    textureHeight
            );
        }
    }

    @Override
    public void updateElementVariables(@NotNull Config config,
                                       @Nullable String configKey) {
        PlaceholderContext context = PlaceholderContext.of(getElementOwner().getDisplayRenderer());

        this.imageBinder = PlayerUtils::bindPlayerSkinTexture;

        String color = config.getStringOrNull("color");
        if(color!=null){
            this.color = AtumColor.fromHex(color);
        }
        String textureX = config.getStringOrNull("textureX");
        if(textureX!=null){
            this.textureX = (int)  config.getEvaluated(
                    "textureX",
                    context
            );
        }
        String textureY = config.getStringOrNull("textureY");
        if(textureY!=null){
            this.textureY = (int)  config.getEvaluated(
                    "textureY",
                    context
            );
        }
        String textureWidth = config.getStringOrNull("textureWidth");
        if(textureWidth!=null){
            this.textureWidth = (int)  config.getEvaluated(
                    "textureWidth",
                    context
            );
        }
        String textureHeight = config.getStringOrNull("textureHeight");
        if(textureHeight!=null){
            this.textureHeight = (int)  config.getEvaluated(
                    "textureHeight",
                    context
            );
        }

    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        ElementPlayerSkin cloneImage = (ElementPlayerSkin) clone;
        try {
            if (cloneImage.color != null) {
                cloneImage.color = color.clone();
            }
        }catch (CloneNotSupportedException ignored){}
        return cloneImage;
    }
}
