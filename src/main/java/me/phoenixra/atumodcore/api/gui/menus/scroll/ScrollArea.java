package me.phoenixra.atumodcore.api.gui.menus.scroll;

import me.phoenixra.atumodcore.core.input.MouseInput;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ScrollArea extends Gui {
    public Color backgroundColor = new Color(0, 0, 0, 240);
    public Color grabberColorNormal = Color.LIGHT_GRAY;
    public Color grabberColorHover = Color.GRAY;
    public ResourceLocation grabberTextureNormal = null;
    public ResourceLocation grabberTextureHover = null;
    public int x;
    public int y;
    public int width;
    public int height;
    public int grabberheight = 20;
    public int grabberwidth = 10;
    public boolean enableScrolling = true;
    private List<ScrollAreaEntry> entries = new ArrayList<ScrollAreaEntry>();

    private boolean grabberHovered = false;
    private boolean grabberPressed = false;

    private int scrollpos = 0;
    private int entryheight = 0;

    private int startY = 0;
    private int startPos = 0;

    public ScrollArea(int x, int y, int width, int height) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        //MouseInput.registerMouseListener(this::onMouseScroll);
    }

    public void render() {

        GlStateManager.enableBlend();

        this.renderBackground();

        this.renderScrollbar();

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc);
        double scaleW = mc.displayWidth / res.getScaledWidth_double();
        double scaleH = mc.displayHeight / res.getScaledHeight_double();
        int sciBottom = this.height + this.y;
        RenderUtils.enableScissor((int) (x * scaleW), (int) (mc.displayHeight - (sciBottom * scaleH)), (int) (width * scaleW), (int) (height * scaleH));

        int i = 0;
        for (ScrollAreaEntry e : this.entries) {
            int i2 = (this.height - this.grabberheight);
            if (i2 == 0) {
                i2 = 1;
            }
            int scroll = this.scrollpos * (this.entryheight / i2);
            e.x = this.x;
            e.y = this.y + i - scroll;
            e.render();

            i += e.getHeight();
        }

        RenderUtils.disableScissor();

    }

    protected void renderScrollbar() {
        if (this.height >= this.entryheight) return;
        int mouseX = 1;//MouseInput.getMouseX();
        int mouseY = 1;//MouseInput.getMouseY();

        //update grabber hover state
        this.grabberHovered = ((this.x + this.width) <= mouseX) && ((this.x + this.width + grabberwidth) >= mouseX) && ((this.y + this.scrollpos) <= mouseY) && ((this.y + this.scrollpos + grabberheight) >= mouseY);

        //Update grabber pressed state
        if (this.isGrabberHovered()){ //&& MouseInput.isLeftMouseDown()) {
            this.grabberPressed = true;
        }
       /* if (!MouseInput.isLeftMouseDown()) {
            this.grabberPressed = false;
        }*/

        //Render scroll grabber
        if (this.enableScrolling) {
            GlStateManager.enableBlend();
            int scrollXStart = this.x + this.width;
            int scrollYStart = this.y + this.scrollpos;
            int scrollXEnd = this.x + this.width + grabberwidth;
            int scrollYEnd = this.y + this.scrollpos + grabberheight;
            if (!this.isGrabberHovered()) {
                if (this.grabberTextureNormal == null) {
                    drawRect(scrollXStart, scrollYStart, scrollXEnd, scrollYEnd, this.grabberColorNormal.getRGB());
                } else {
                    RenderUtils.bindTexture(this.grabberTextureNormal);
                    drawModalRectWithCustomSizedTexture(scrollXStart, scrollYStart, 0.0F, 0.0F, grabberwidth, grabberheight, grabberwidth, grabberheight);
                }
            } else {
                if (this.grabberTextureHover == null) {
                    drawRect(scrollXStart, scrollYStart, scrollXEnd, scrollYEnd, this.grabberColorHover.getRGB());
                } else {
                    RenderUtils.bindTexture(this.grabberTextureHover);
                    drawModalRectWithCustomSizedTexture(scrollXStart, scrollYStart, 0.0F, 0.0F, grabberwidth, grabberheight, grabberwidth, grabberheight);
                }
            }
        }

        //Handle scroll
        if (!enableScrolling) {
            this.scrollpos = 0;
            return;
        }
        if (this.isGrabberPressed()) {
            this.handleGrabberScrolling();
            return;
        }
        this.startY = 1;//MouseInput.getMouseY();
        this.startPos = this.scrollpos;

    }

    public boolean isAreaHovered() {
        int mouseX = 1;//MouseInput.getMouseX();
        int mouseY = 1;//MouseInput.getMouseY();
        return (this.x <= mouseX) && ((this.x + this.width + this.grabberwidth) >= mouseX) && (this.y <= mouseY) && ((this.y + this.height) >= mouseY);
    }

    protected void handleGrabberScrolling() {
        int i = this.startY - 1;//MouseInput.getMouseY();
        int scroll = this.startPos - i;

        if (scroll < 0) {
            this.scrollpos = 0;
            return;
        }
        this.scrollpos = Math.min(scroll, this.height - this.grabberheight);
    }

    protected void renderBackground() {
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        drawRect(this.x, this.y, this.x + this.width, this.y + this.height, this.backgroundColor.getRGB());
        GlStateManager.popMatrix();
    }

    public void addEntry(ScrollAreaEntry e) {
        this.entries.add(e);
        this.scrollpos = 0;
        this.entryheight += e.getHeight();
    }

    public void removeEntry(ScrollAreaEntry e) {
        if(!this.entries.contains(e)) return;
        this.entries.remove(e);
        this.scrollpos = 0;
        this.entryheight -= e.getHeight();
    }

    public List<ScrollAreaEntry> getEntries() {
        return this.entries;
    }

    public int getStackedEntryHeight() {
        return this.entryheight;
    }

    public boolean isGrabberHovered() {
        return this.grabberHovered;
    }

    public boolean isGrabberPressed() {
        return this.grabberPressed;
    }

    public void onMouseScroll(){//MouseInput.MouseData d) {
        if(!this.isAreaHovered()) return;
        int i = 1;//d.deltaZ / 120;
        int scroll = this.scrollpos - i * 7;
        if (scroll < 0) {
            this.scrollpos = 0;
            return;
        }
        this.scrollpos = Math.min(scroll, this.height - this.grabberheight);
    }
}
