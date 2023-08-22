package me.phoenixra.atumodcore.api.gui.menus.popup;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.gui.elements.GuiElementButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Popup extends Gui {
    @Getter @Setter
    private boolean displayed = false;
    private int alpha;
    @Getter
    private List<GuiElementButton> buttons = new ArrayList<GuiElementButton>();

    public Popup(int backgroundAlpha) {
        this.alpha = backgroundAlpha;
    }

    public void render(int mouseX, int mouseY, GuiScreen renderIn) {
        if (!this.isDisplayed()) {
            return;
        }
        GlStateManager.enableBlend();
        Gui.drawRect(0, 0, renderIn.width, renderIn.height, new Color(0, 0, 0, this.alpha).getRGB());
        GlStateManager.disableBlend();
    }

    protected void addButton(GuiElementButton b) {
        if (!this.buttons.contains(b)) {
            this.buttons.add(b);
            b.ignoreBlockedInput = true;
            this.colorizePopupButton(b);
        }
    }

    protected void removeButton(GuiElementButton b) {
        if (this.buttons.contains(b)) {
            this.buttons.remove(b);
        }
    }

    protected void renderButtons(int mouseX, int mouseY) {
        for (GuiElementButton b : this.buttons) {
            b.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, Minecraft.getMinecraft().getRenderPartialTicks());
        }
    }

    protected void colorizePopupButton(GuiElementButton b) {
        b.setBackgroundColor(new Color(102, 102, 153), new Color(133, 133, 173), new Color(163, 163, 194), new Color(163, 163, 194), 1);
    }
}
