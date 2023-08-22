package me.phoenixra.atumodcore.api.gui.elements;

import me.phoenixra.atumodcore.api.gui.IGuiElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiElementImageButton extends GuiElementButton implements IGuiElement {

    private ResourceLocation image;

    public GuiElementImageButton(int x, int y, int widthIn, int heightIn, ResourceLocation image, boolean handleClick, IPressable onPress) {
        super(x, y, widthIn, heightIn, "", handleClick, onPress);
        this.image = image;
    }

    public GuiElementImageButton(int x, int y, int widthIn, int heightIn, ResourceLocation image, IPressable onPress) {
        super(x, y, widthIn, heightIn, "", onPress);
        this.image = image;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        super.drawButton(mc, mouseX, mouseY, partialTicks);

        Minecraft.getMinecraft().getTextureManager().bindTexture(this.image);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawModalRectWithCustomSizedTexture(this.x, this.y, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
    }

    public void setImage(ResourceLocation image) {
        this.image = image;
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
}
