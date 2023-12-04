package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseElement;
import me.phoenixra.atumodcore.api.misc.AtumColor;
import me.phoenixra.atumodcore.api.placeholders.context.PlaceholderContext;
import me.phoenixra.atumodcore.api.utils.PlayerUtils;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementImage extends BaseElement {
    private Runnable imageBinder;
    private AtumColor color = AtumColor.WHITE;
    private int textureX;
    private int textureY;
    private int textureWidth;
    private int textureHeight;

    public ElementImage(@NotNull AtumMod atumMod,@NotNull DisplayCanvas elementOwner) {
        super(atumMod,elementOwner);

    }

    @Override
    protected void onDraw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
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
    public void updateVariables(@NotNull Config config, @Nullable String configKey) {
        super.updateVariables(config,configKey);
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        String image = config.getStringOrNull("settings.image");
        if(image!=null){
            if(image.equalsIgnoreCase("playerSkin")){
                this.imageBinder = PlayerUtils::bindPlayerSkinTexture;
            }else {
                ResourceLocation imageLocation = new ResourceLocation(image);
                this.imageBinder = () -> textureManager.bindTexture(imageLocation);
            }
        }
        String color = config.getStringOrNull("settings.color");
        if(color!=null){
            this.color = AtumColor.fromHex(color);
        }
        String textureX = config.getStringOrNull("settings.textureX");
        if(textureX!=null){
            this.textureX = (int) config.getAtumMod().getApi().evaluate(
                    config.getAtumMod(),
                    textureX,
                    PlaceholderContext.of(config)
            );
        }
        String textureY = config.getStringOrNull("settings.textureY");
        if(textureY!=null){
            this.textureY = (int) config.getAtumMod().getApi().evaluate(
                    config.getAtumMod(),
                    textureY,
                    PlaceholderContext.of(config)
            );
        }
        String textureWidth = config.getStringOrNull("settings.textureWidth");
        if(textureWidth!=null){
            this.textureWidth = (int) config.getAtumMod().getApi().evaluate(
                    config.getAtumMod(),
                    textureWidth,
                    PlaceholderContext.of(config)
            );
        }
        String textureHeight = config.getStringOrNull("settings.textureHeight");
        if(textureHeight!=null){
            this.textureHeight = (int) config.getAtumMod().getApi().evaluate(
                    config.getAtumMod(),
                    textureHeight,
                    PlaceholderContext.of(config)
            );
        }
    }

    @Override
    protected BaseElement onClone(BaseElement clone) {
        ElementImage cloneImage = (ElementImage) clone;
        if(cloneImage.color!=null) {
            cloneImage.color = new AtumColor(cloneImage.color.getRed(), cloneImage.color.getGreen(), cloneImage.color.getBlue(), cloneImage.color.getAlpha());
        }
        return cloneImage;
    }

}
