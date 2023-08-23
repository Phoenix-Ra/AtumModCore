package me.phoenixra.atumodcore.api.gui.elements;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.gui.IGuiElement;
import me.phoenixra.atumodcore.api.gui.handlers.ButtonHandler;
import me.phoenixra.atumodcore.core.input.MouseInput;
import me.phoenixra.atumodcore.api.misc.animation.IAnimationRenderer;
import me.phoenixra.atumodcore.api.misc.resources.ExternalTextureResourceLocation;
import me.phoenixra.atumodcore.mod.sound.SoundHandler;
import me.phoenixra.atumodcore.api.utils.MathUtils;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class GuiElementButton extends GuiButton implements IGuiElement {
    protected boolean handleClick = false;
    protected static boolean leftDown = false;
    protected boolean leftDownThis = false;
    protected boolean leftDownNotHovered = false;
    public boolean ignoreBlockedInput = false;
    public boolean ignoreLeftMouseDownClickBlock = false;
    public boolean enableRightClick = false;
    public float labelScale = 1.0F;

    @Getter @Setter
    protected boolean usable = true;
    protected boolean labelShadow = true;
    public float alpha = 1.0F;
    public boolean renderLabel = true;
    protected Color idleColor;
    protected Color hoveredColor;
    protected Color idleBorderColor;
    protected Color hoveredBorderColor;
    protected float borderWidth = 2.0F;
    protected ResourceLocation backgroundHover;
    protected ResourceLocation backgroundNormal;
    protected IAnimationRenderer backgroundAnimationNormal;
    protected IAnimationRenderer backgroundAnimationHover;
    public boolean loopBackgroundAnimations = true;
    public boolean restartBackgroundAnimationsOnHover = true;
    protected boolean lastHoverState = false;
    String clicksound = null;
    String[] description = null;

    protected IPressable press;

    public GuiElementButton(int x, int y, int widthIn, int heightIn, String buttonText, IPressable onPress) {
        super(MathUtils.randInt(100, 999), x, y, widthIn, heightIn, buttonText);
        this.press = onPress;
    }

    public GuiElementButton(int x, int y, int widthIn, int heightIn, String buttonText, boolean handleClick, IPressable onPress) {
        super(MathUtils.randInt(100, 999), x, y, widthIn, heightIn, buttonText);
        this.handleClick = handleClick;
        this.press = onPress;
        
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {

            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

            if (this.isMouseOver()
                    && this.lastHoverState != this.isMouseOver()
                    && restartBackgroundAnimationsOnHover) {

                if (this.backgroundAnimationNormal != null) {
                    this.backgroundAnimationNormal.resetAnimation();
                }
                if (this.backgroundAnimationHover != null) {
                    this.backgroundAnimationHover.resetAnimation();
                }
            }
            this.lastHoverState = this.isMouseOver();

            GlStateManager.enableBlend();
            if (this.hasColorBackground()) {
                Color border;
                if (!hovered) {
                    drawRect(this.x, this.y, this.x + this.width, this.y + this.height, this.idleColor.getRGB() | MathHelper.ceil(this.alpha * 255.0F) << 24);
                    border = this.idleBorderColor;
                } else {
                    if (this.enabled) {
                        drawRect(this.x, this.y, this.x + this.width, this.y + this.height, this.hoveredColor.getRGB() | MathHelper.ceil(this.alpha * 255.0F) << 24);
                        border = this.hoveredBorderColor;
                    } else {
                        drawRect(this.x, this.y, this.x + this.width, this.y + this.height, this.idleColor.getRGB() | MathHelper.ceil(this.alpha * 255.0F) << 24);
                        border = this.idleBorderColor;
                    }
                }
                if (this.hasBorder()) {
                    //top
                    RenderUtils.fill(this.x, this.y, this.x + this.width, this.y + this.borderWidth, border.getRGB(), this.alpha);
                    //bottom
                    RenderUtils.fill(this.x, this.y + this.height - this.borderWidth, this.x + this.width, this.y + this.height, border.getRGB(), this.alpha);
                    //left
                    RenderUtils.fill(this.x, this.y + this.borderWidth, this.x + this.borderWidth, this.y + this.height - this.borderWidth, border.getRGB(), this.alpha);
                    //right
                    RenderUtils.fill(this.x + this.width - this.borderWidth, this.y + this.borderWidth, this.x + this.width, this.y + this.height - this.borderWidth, border.getRGB(), this.alpha);
                }
            } else {
                this.renderBackgroundNormal();
                this.renderBackgroundHover();
            }

            this.mouseDragged(mc, mouseX, mouseY);

            this.renderLabel();

            if (this.isMouseOver()) {
                ButtonHandler.setActiveDescriptionButton(this);
            }

        }
        if (!MouseInput.isLeftMouseDown()) {
            this.leftDownNotHovered = false;
        } else if (!this.isMouseOver()) {
            this.leftDownNotHovered = true;
        }

        if (!handleClick || !usable) return;
        if (this.isMouseOver()
                && (MouseInput.isLeftMouseDown() || (this.enableRightClick && MouseInput.isRightMouseDown()))
                && (!leftDown || this.ignoreLeftMouseDownClickBlock)
                && !leftDownNotHovered
                && !this.isInputBlocked()
                && this.enabled
                && this.visible) {
            if (!this.leftDownThis) {
                this.press.onPress(this);
                this.playPressSound(Minecraft.getMinecraft().getSoundHandler());
                leftDown = true;
                this.leftDownThis = true;
            }
        }
        if (!MouseInput.isLeftMouseDown() && !(MouseInput.isRightMouseDown() && this.enableRightClick)) {
            leftDown = false;
            this.leftDownThis = false;
        }
    }

    protected void renderBackgroundHover() {
        try {
            if (!this.isMouseOver()) {
                return;
            }
            if (!enabled) {
                this.renderDefaultBackground();
                return;
            }
            if (!hasCustomBackgroundHover()) {
                renderDefaultBackground();
                return;
            }
            if (backgroundHover == null) {
                int aniX = this.backgroundAnimationHover.getPosX();
                int aniY = this.backgroundAnimationHover.getPosY();
                int aniWidth = this.backgroundAnimationHover.getWidth();
                int aniHeight = this.backgroundAnimationHover.getHeight();
                boolean aniLoop = this.backgroundAnimationHover.isGettingLooped();

                this.backgroundAnimationHover.setPosX(this.x);
                this.backgroundAnimationHover.setPosY(this.y);
                this.backgroundAnimationHover.setWidth(this.width);
                this.backgroundAnimationHover.setHeight(this.height);
                this.backgroundAnimationHover.setLooped(this.loopBackgroundAnimations);
                this.backgroundAnimationHover.setOpacity(this.alpha);

                this.backgroundAnimationHover.render();

                this.backgroundAnimationHover.setPosX(aniX);
                this.backgroundAnimationHover.setPosY(aniY);
                this.backgroundAnimationHover.setWidth(aniWidth);
                this.backgroundAnimationHover.setHeight(aniHeight);
                this.backgroundAnimationHover.setLooped(aniLoop);
                this.backgroundAnimationHover.setOpacity(1.0F);
                return;
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.backgroundNormal);
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, this.alpha);
            drawModalRectWithCustomSizedTexture(this.x, this.y, 0.0F, 0.0F, this.width, this.height, this.width, this.height);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void renderBackgroundNormal() {
        try {
            if (isMouseOver()) return;
            if(!hasCustomBackgroundNormal()){
                renderDefaultBackground();
                return;
            }
            if(backgroundNormal == null){
                int aniX = this.backgroundAnimationNormal.getPosX();
                int aniY = this.backgroundAnimationNormal.getPosY();
                int aniWidth = this.backgroundAnimationNormal.getWidth();
                int aniHeight = this.backgroundAnimationNormal.getHeight();
                boolean aniLoop = this.backgroundAnimationNormal.isGettingLooped();

                this.backgroundAnimationNormal.setPosX(this.x);
                this.backgroundAnimationNormal.setPosY(this.y);
                this.backgroundAnimationNormal.setWidth(this.width);
                this.backgroundAnimationNormal.setHeight(this.height);
                this.backgroundAnimationNormal.setLooped(this.loopBackgroundAnimations);
                this.backgroundAnimationNormal.setOpacity(this.alpha);

                this.backgroundAnimationNormal.render();

                this.backgroundAnimationNormal.setPosX(aniX);
                this.backgroundAnimationNormal.setPosY(aniY);
                this.backgroundAnimationNormal.setWidth(aniWidth);
                this.backgroundAnimationNormal.setHeight(aniHeight);
                this.backgroundAnimationNormal.setLooped(aniLoop);
                this.backgroundAnimationNormal.setOpacity(1.0F);
                return;
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.backgroundNormal);
            GlStateManager.enableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, this.alpha);
            drawModalRectWithCustomSizedTexture(this.x, this.y, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void renderDefaultBackground() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(BUTTON_TEXTURES);
        GlStateManager.color(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getHoverState(this.hovered);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
        this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
    }

    protected void renderLabel() {
        if (!renderLabel) {
            return;
        }

        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        int stringWidth = font.getStringWidth(this.displayString);
        int stringHeight = 8;
        int pX = (int) (((this.x + (this.width / 2)) - ((stringWidth * this.labelScale) / 2)) / this.labelScale);
        int pY = (int) (((this.y + (this.height / 2)) - ((stringHeight * this.labelScale) / 2)) / this.labelScale);

        GlStateManager.pushMatrix();
        GlStateManager.scale(this.labelScale, this.labelScale, this.labelScale);

        if (this.labelShadow) {
            font.drawStringWithShadow(displayString, pX, pY, getFGColor() | MathHelper.ceil(this.alpha * 255.0F) << 24);
        } else {
            font.drawString(displayString, pX, pY, getFGColor() | MathHelper.ceil(this.alpha * 255.0F) << 24);
        }

        GlStateManager.popMatrix();
    }

    protected int getFGColor() {
        int j = 14737632;
        if (packedFGColour != 0) {
            j = packedFGColour;
        } else if (!this.enabled) {
            j = 10526880;
        } else if (this.hovered) {
            j = 16777120;
        }
        return j;
    }

    protected boolean isInputBlocked() {
        if (this.ignoreBlockedInput) {
            return false;
        }
        return MouseInput.isVanillaInputBlocked();
    }

    public void setBackgroundColor(@Nullable Color idle, @Nullable Color hovered, @Nullable Color idleBorder, @Nullable Color hoveredBorder, float borderWidth) {
        this.idleColor = idle;
        this.hoveredColor = hovered;
        this.hoveredBorderColor = hoveredBorder;
        this.idleBorderColor = idleBorder;

        if (borderWidth >= 0) {
            this.borderWidth = borderWidth;
        } else {
            borderWidth = 0;
        }
    }

    public void setBackgroundColor(@Nullable Color idle, @Nullable Color hovered, @Nullable Color idleBorder, @Nullable Color hoveredBorder, int borderWidth) {
        this.setBackgroundColor(idle, hovered, idleBorder, hoveredBorder, (float) borderWidth);
    }

    @Deprecated
    public void setBackgroundTexture(ResourceLocation normal, ResourceLocation hovered) {
        this.backgroundNormal = normal;
        this.backgroundHover = hovered;
    }

    @Deprecated
    public void setBackgroundTexture(ExternalTextureResourceLocation normal, ExternalTextureResourceLocation hovered) {
        if (normal != null) {
            if (!normal.isReady()) {
                normal.loadTexture();
            }
            this.backgroundNormal = normal.getResourceLocation();
        } else {
            this.backgroundNormal = null;
        }
        if (hovered != null) {
            if (!hovered.isReady()) {
                hovered.loadTexture();
            }
            this.backgroundHover = hovered.getResourceLocation();
        } else {
            this.backgroundHover = null;
        }
    }

    public void setBackgroundNormal(ResourceLocation texture) {
        this.backgroundNormal = texture;
    }

    public void setBackgroundNormal(IAnimationRenderer animation) {
        if (animation != null && !animation.isReady()) {
            animation.prepareAnimation();
        }
        this.backgroundAnimationNormal = animation;
    }

    public void setBackgroundHover(ResourceLocation texture) {
        this.backgroundHover = texture;
    }

    public void setBackgroundHover(IAnimationRenderer animation) {
        if (animation != null && !animation.isReady()) {
            animation.prepareAnimation();
        }
        this.backgroundAnimationHover = animation;
    }

    public boolean hasBorder() {
        return (this.hasColorBackground() && (this.idleBorderColor != null) && (this.hoveredBorderColor != null));
    }

    public boolean hasColorBackground() {
        return ((this.idleColor != null) && (this.hoveredColor != null));
    }

    @Deprecated
    public boolean hasCustomTextureBackground() {
        return this.hasCustomBackground();
    }

    public boolean hasCustomBackground() {
        return (((this.backgroundHover != null) || (this.backgroundAnimationHover != null)) && ((this.backgroundNormal != null) || (this.backgroundAnimationNormal != null)));
    }

    public boolean hasCustomBackgroundNormal() {
        return ((this.backgroundNormal != null) || (this.backgroundAnimationNormal != null));
    }

    public boolean hasCustomBackgroundHover() {
        return ((this.backgroundHover != null) || (this.backgroundAnimationHover != null));
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if(handleClick || !usable) return false;
        if (this.isMouseOver()) {
            this.press.onPress(this);
        }
        return super.mousePressed(mc, mouseX, mouseY);
    }

    @Override
    public void playPressSound(net.minecraft.client.audio.SoundHandler soundHandlerIn) {
        if (this.clicksound == null) {
            super.playPressSound(soundHandlerIn);
        } else {
            SoundHandler.resetSound(this.clicksound);
            SoundHandler.playSound(this.clicksound);
        }
    }


    public void setHandleClick(boolean b) {
        this.handleClick = b;
    }

    public void setPressAction(IPressable press) {
        this.press = press;
    }

    public void setClickSound(@Nullable String key) {
        this.clicksound = key;
    }

    public void setDescription(String... desc) {
        this.description = desc;
    }

    public String[] getDescription() {
        return this.description;
    }

    public void setLabelShadow(boolean shadow) {
        this.labelShadow = shadow;
    }

    public static boolean isAnyButtonLeftClicked() {
        return leftDown;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    public interface IPressable {
        void onPress(GuiButton button);
    }

}
