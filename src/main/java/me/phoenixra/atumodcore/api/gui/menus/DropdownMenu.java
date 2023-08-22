package me.phoenixra.atumodcore.api.gui.menus;

import me.phoenixra.atumodcore.api.gui.IMenu;
import me.phoenixra.atumodcore.api.gui.elements.GuiElementButton;
import me.phoenixra.atumodcore.mod.input.MouseInput;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class DropdownMenu implements IMenu {
    private int width;
    private int height;
    private int x;
    private int y;
    private List<GuiElementButton> content = new ArrayList<GuiElementButton>();
    private GuiElementButton dropdown;
    private boolean opened = false;
    private boolean hovered = false;
    private boolean autoclose = false;
    private int space;

    public DropdownMenu(String label, int width, int height, int x, int y, int space) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.space = space;

        this.dropdown = new GuiElementButton(0, 0, 0, 0, label, true, (press) -> {
            this.toggleMenu();
        });
    }

    public void render(int mouseX, int mouseY) {
        float ticks = Minecraft.getMinecraft().getRenderPartialTicks();

        this.updateHovered(mouseX, mouseY);

        this.dropdown.height = this.height;
        this.dropdown.width = this.width;
        this.dropdown.x = this.x;
        this.dropdown.y = this.y;

        this.dropdown.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, ticks);

        int stackedHeight = this.height + this.space;
        if (this.opened) {
            for (GuiElementButton b : this.content) {
                b.setHandleClick(true);
                b.width = this.width;
                b.x = this.x;
                b.y = this.y + stackedHeight;
                b.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, ticks);

                stackedHeight += b.height + this.space;
            }
        }

        if (this.autoclose && !this.isHovered() && (MouseInput.isLeftMouseDown() || MouseInput.isRightMouseDown())) {
            this.opened = false;
        }
    }

    private void updateHovered(int mouseX, int mouseY) {
        if (dropdown.isMouseInElement(mouseX, mouseY)) {
            this.hovered = true;
            return;
        }
        for (GuiElementButton b : this.content) {
            if (b.isMouseInElement(mouseX, mouseY)) {
                this.hovered = true;
                return;
            }
        }
        this.hovered = false;
    }

    public boolean isHovered() {
        if (!this.isOpen()) {
            return false;
        }
        return this.hovered;
    }

    public void setUsable(boolean b) {
        this.dropdown.setUsable(b);
        for (GuiElementButton bt : this.content) {
            bt.setUsable(b);
        }
        if (!b) {
            this.opened = false;
        }
    }

    public boolean isUsable() {
        if (this.dropdown == null) {
            return false;
        }
        return this.dropdown.isUsable();
    }

    public void setAutoClose(boolean b) {
        this.autoclose = b;
    }

    public boolean isOpen() {
        return this.opened;
    }

    public void openMenu() {
        this.opened = true;
    }

    public void closeMenu() {
        this.opened = false;
    }

    private void toggleMenu() {
        this.opened = !this.opened;
    }

    public void addContent(GuiElementButton button) {
        this.content.add(button);
    }

    public void setLabel(String text) {
        this.dropdown.displayString = text;
    }

    public GuiElementButton getDropdownParent() {
        return this.dropdown;
    }
}
