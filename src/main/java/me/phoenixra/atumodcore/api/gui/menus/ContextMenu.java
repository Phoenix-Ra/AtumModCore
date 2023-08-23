package me.phoenixra.atumodcore.api.gui.menus;

import me.phoenixra.atumodcore.api.gui.IMenu;
import me.phoenixra.atumodcore.api.gui.elements.GuiElementButton;
import me.phoenixra.atumodcore.core.input.MouseInput;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

public class ContextMenu implements IMenu {

    protected int width;
    protected int buttonHeight;
    protected int x = 0;
    protected int y = 0;
    protected List<GuiElementButton> content = new ArrayList<GuiElementButton>();
    protected List<ContextMenu> children = new ArrayList<ContextMenu>();
    protected ContextMenu parent;
    protected GuiElementButton parentButton;
    protected boolean opened = false;
    protected boolean hovered = false;
    protected boolean autoclose = false;
    protected boolean autoalignment = true;
    protected int space;
    protected boolean alwaysOnTop = false;

    protected boolean up = false;
    protected boolean left = false;
    protected int lastHeight = 0;

    public float menuScale = 1.0F;

    protected boolean autocloseChilds = true;

    public ContextMenu(int width, int buttonHeight, int space) {
        this.width = width;
        this.buttonHeight = buttonHeight;
        this.space = space;
    }

    public void render(int mouseX, int mouseY, int screenWidth, int screenHeight) {
        this.updateHovered(mouseX, mouseY);

        float ticks = Minecraft.getMinecraft().getRenderPartialTicks();

        int stackedHeight = 0;

        if (!this.opened){
            return;
        }

        if (this.alwaysOnTop) {
            RenderUtils.setZLevelPre(400);
        }

        for (GuiElementButton b : this.content) {
            b.setHandleClick(true);
            b.width = this.getScaledWidth();
            b.height = this.getScaledButtonHeight();
            b.labelScale = this.menuScale;

            if (this.parent != null) {
                this.buttonHeight = parent.buttonHeight;

                if (parent.left) {
                    this.left = true;
                    this.x = parent.x - parent.getScaledWidth() - this.getScaledWidth() - 2;
                } else {
                    this.x = parent.x + parent.getScaledWidth() + 2;
                }
                if (this.autoalignment && ((this.x + this.getScaledWidth()) > screenWidth)) {
                    this.x = parent.x - this.getScaledWidth() - 2;
                    this.left = true;
                }
                b.x = this.x;

                if (!this.autoalignment) {
                    if (this.up) {
                        b.y = this.y + stackedHeight - this.lastHeight + this.getScaledButtonHeight() + this.space;
                    } else {
                        b.y = this.y + stackedHeight;
                    }
                } else {
                    b.y = this.y + stackedHeight;
                }

            } else {
                if (this.left) {
                    b.x = this.x - this.getScaledWidth();
                } else {
                    b.x = this.x;
                }

                if (!this.autoalignment) {
                    if (this.up) {
                        b.y = this.y + stackedHeight - this.lastHeight;
                    } else {
                        b.y = this.y + stackedHeight;
                    }
                } else {
                    b.y = this.y + stackedHeight;
                }
            }

            b.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, ticks);

            stackedHeight += this.getScaledButtonHeight() + this.space;
        }

        for (ContextMenu m : this.children) {
            m.render(mouseX, mouseY, screenWidth, screenHeight);
        }

        if (this.alwaysOnTop) {
            RenderUtils.setZLevelPost();
        }

        if (this.autoclose &&
                !this.isHovered() &&
                !this.isChildHovered() &&
                !this.isParentButtonHovered() &&
                (MouseInput.isLeftMouseDown() || MouseInput.isRightMouseDown()) &&
                (this.parent == null|| this.parentButton != null)) {
            this.opened = false;
        }


    }

    public void render(int mouseX, int mouseY) {
        GuiScreen c = Minecraft.getMinecraft().currentScreen;
        if (c != null) {
            this.render(mouseX, mouseY, c.width, c.height);
        }
    }

    public int getScaledWidth() {
        return (int) (this.width * this.menuScale);
    }

    private int getScaledButtonHeight() {
        return (int) (this.buttonHeight * this.menuScale);
    }

    private boolean isChildHovered() {
        for (ContextMenu m : this.children) {
            if (m.isOpen() && (m.isHovered() || m.isChildHovered())) {
                return true;
            }
        }
        return false;
    }

    private boolean isParentButtonHovered() {
        if (this.parentButton != null) {
            return this.parentButton.isMouseOver();
        }
        return false;
    }

    public void setParentButton(GuiElementButton parent) {
        this.parentButton = parent;
    }

    public GuiElementButton getParentButton() {
        return this.parentButton;
    }

    private void updateHovered(int mouseX, int mouseY) {
        for (GuiElementButton b : this.content) {
            if (b.isMouseInElement(mouseX, mouseY)) {
                this.hovered = true;
                return;
            }
        }
        this.hovered = false;
    }

    public boolean isLeftClicked() {
        for (GuiElementButton b : this.content) {
            if (b.isMouseOver() && MouseInput.isLeftMouseDown()) {
                return true;
            }
        }
        return false;
    }

    public boolean isHovered() {
        if (!this.isOpen()) {
            return false;
        }
        return this.hovered;
    }

    public boolean isOpen() {
        return this.opened;
    }

    public void setAutoClose(boolean b) {
        this.autoclose = b;
    }

    public void setUsable(boolean b) {
        for (GuiElementButton bt : this.content) {
            bt.setUsable(b);
        }
        if (!b) {
            this.opened = false;
        }
    }

    public boolean isUsable() {
        if ((this.content == null) || this.content.isEmpty()) {
            return false;
        }
        return this.content.get(0).isUsable();
    }

    public void openMenuAt(int x, int y, int screenWidth, int screenHeight) {

        if (this.parent != null) {
            this.autoalignment = this.parent.autoalignment;
            this.autocloseChilds = this.parent.autocloseChilds;
        }

        for (ContextMenu m : this.children) {
            m.closeMenu();
            if (this.autocloseChilds) {
                m.autoclose = true;
            }
            m.menuScale = this.menuScale;
        }

        this.x = x;
        this.y = y;

        this.lastHeight = (this.getScaledButtonHeight() + this.space) * this.content.size();

        if (this.autoalignment) {
            if ((this.y + this.lastHeight) > screenHeight) {
                int i = (this.y + this.lastHeight) - screenHeight;
                this.y -= i;
            }
            if ((this.parent == null) || (this.parent.parent == null)) {
                if ((this.x + this.getScaledWidth()) > screenWidth) {
                    this.left = true;
                } else {
                    this.left = false;
                }
            } else {
                this.left = this.parent.left;
            }
        } else {
            if (this.parent != null) {
                this.left = this.parent.left;
                this.up = this.parent.up;
            }
        }

        this.opened = true;
    }

    public void openMenuAt(int x, int y) {
        GuiScreen c = Minecraft.getMinecraft().currentScreen;
        if (c != null) {
            this.openMenuAt(x, y, c.width, c.height);
        }
    }

    public void closeMenu() {
        this.opened = false;
        for (ContextMenu m : this.children) {
            m.closeMenu();
        }
    }

    public void closeChildren() {
        for (ContextMenu m : this.children) {
            m.closeMenu();
        }
    }

    public void addContent(GuiElementButton button) {
        button.ignoreBlockedInput = true;
        this.content.add(button);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return this.width;
    }

    public int getLastHeight() {
        return this.lastHeight;
    }

    public boolean isRenderedLeft() {
        return this.left;
    }

    public boolean isRenderedUp() {
        return this.up;
    }

    public void addChild(ContextMenu menu) {
        if (!this.children.contains(menu)) {
            this.children.add(menu);
            menu.parent = this;
        }
    }

    public void removeChild(ContextMenu menu) {
        if (this.children.contains(menu)) {
            this.children.remove(menu);
            menu.parent = null;
        }
    }

    public void setAutoAlignment(boolean autoalign) {
        this.autoalignment = autoalign;
    }

    public void setAlignment(boolean up, boolean left) {
        this.up = up;
        this.left = left;
    }

    public void setAlwaysOnTop(boolean b) {
        this.alwaysOnTop = b;
    }

    public void setButtonHeight(int height) {
        this.buttonHeight = height;
    }

    public void autoCloseChildren(boolean autoclose) {
        this.autocloseChilds = autoclose;
    }
}
