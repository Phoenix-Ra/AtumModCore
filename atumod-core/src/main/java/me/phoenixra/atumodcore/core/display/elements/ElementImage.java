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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementImage extends BaseElement {
    private Runnable imageBinder;
    private ResourceLocation speciaImageDefault;


    private AtumColor color = AtumColor.WHITE;
    private int textureX;
    private int textureY;
    private int textureWidth;
    private int textureHeight;


    private AtumColor colorDefault = AtumColor.WHITE;
    private int textureXDefault;
    private int textureYDefault;
    private int textureWidthDefault;
    private int textureHeightDefault;

    private AtumColor outlineColorDefault = AtumColor.WHITE;
    private int outlineSizeDefault = 1;
    private boolean hasOutlineDefault = false;

    private boolean savedOutlineState = false;


    public ElementImage(@NotNull AtumMod atumMod,@NotNull DisplayCanvas elementOwner) {
        super(atumMod,elementOwner);

    }

    @Override
    protected void onDraw(float scaleFactor, float scaleX, float scaleY, int mouseX, int mouseY) {
        if(speciaImageDefault!=null){
            drawHeldItemOrDefault();
            return;
        }
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
        PlaceholderContext context = PlaceholderContext.of(getElementOwner().getDisplayRenderer());
        String image = config.getStringOrNull("settings.image");
        if(image!=null){
            speciaImageDefault = null;
            if(image.equalsIgnoreCase("playerSkin")){
                this.imageBinder = PlayerUtils::bindPlayerSkinTexture;
            }else if(image.equalsIgnoreCase("heldItem")){
               speciaImageDefault = new ResourceLocation(
                        config.getStringOrDefault("settings.defaultImage.location",
                                "atumodcore:textures/button.png")
                );
               this.colorDefault = AtumColor.fromHex(
                       config.getStringOrDefault("settings.defaultImage.color",
                               "#FFFFFFFF")
               );
                this.textureXDefault = config.getIntOrDefault("settings.defaultImage.textureX",0);
                this.textureYDefault = config.getIntOrDefault("settings.defaultImage.textureY",0);
                this.textureWidthDefault = config.getIntOrDefault("settings.defaultImage.textureWidth",0);
                this.textureHeightDefault = config.getIntOrDefault("settings.defaultImage.textureHeight",0);
               if(config.hasPath("settings.defaultImage.outline")) {
                   this.outlineColorDefault = AtumColor.fromHex(
                           config.getStringOrDefault("settings.defaultImage.outline.color",
                                   "#FFFFFFFF")
                   );
                   this.outlineSizeDefault = config.getIntOrDefault("settings.defaultImage.outline.size", 1);
                   this.hasOutlineDefault = true;
               }else{
                   hasOutlineDefault = false;
               }

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
            this.textureX = (int)  config.getEvaluated(
                    "settings.textureX",
                    context
            );
        }
        String textureY = config.getStringOrNull("settings.textureY");
        if(textureY!=null){
            this.textureY = (int)  config.getEvaluated(
                    "settings.textureY",
                    context
            );
        }
        String textureWidth = config.getStringOrNull("settings.textureWidth");
        if(textureWidth!=null){
            this.textureWidth = (int)  config.getEvaluated(
                    "settings.textureWidth",
                    context
            );
        }
        String textureHeight = config.getStringOrNull("settings.textureHeight");
        if(textureHeight!=null){
            this.textureHeight = (int)  config.getEvaluated(
                    "settings.textureHeight",
                    context
            );
        }
        savedOutlineState = hasOutline;

    }







    private void drawHeldItemOrDefault(){

        if(Minecraft.getMinecraft().player == null ||
                Minecraft.getMinecraft().player.getHeldItemMainhand().isEmpty()) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(
                    speciaImageDefault
            );
            colorDefault.useColor();
            if(textureHeightDefault==0||textureWidthDefault==0){
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
                        textureXDefault,
                        textureYDefault,
                        textureWidthDefault,
                        textureHeightDefault
                );
            }

            if(hasOutlineDefault){
                RenderUtils.drawOutline(
                        getX(),
                        getY(),
                        getWidth(),
                        getHeight(),
                        outlineSizeDefault,
                        outlineColorDefault
                );
                savedOutlineState = hasOutline;
            }
            hasOutline = false;
            return;
        }
        color.useColor();
        RenderUtils.renderItemIntoGUI(
                Minecraft.getMinecraft().player.getHeldItemMainhand(),
                getX()+getWidth()/2,
                getY()+getHeight()/2,
                getWidth(),
                getHeight()
        );
        hasOutline = savedOutlineState;
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
