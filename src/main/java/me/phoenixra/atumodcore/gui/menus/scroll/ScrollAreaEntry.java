package me.phoenixra.atumodcore.gui.menus.scroll;

import me.phoenixra.atumodcore.input.MouseInput;
import net.minecraft.client.gui.Gui;

public abstract class ScrollAreaEntry extends Gui {
    public int x = 0;
    public int y = 0;
    public final ScrollArea parent;

    public ScrollAreaEntry(ScrollArea parent) {
        this.parent = parent;
    }

    public abstract void renderEntry();

    public void render() {
        if (this.isVisible()) {
            this.renderEntry();
        }
    }

    public abstract int getHeight();

    public int getWidth() {
        return this.parent.width;
    }

    public boolean isHovered() {
        int mx = MouseInput.getMouseX();
        int my = MouseInput.getMouseY();
        return (this.x <= mx) && (this.y <= my) && ((this.x + this.parent.width) >= mx) && ((this.y + this.getHeight()) >= my);
    }

    public boolean isVisible() {
        return (this.parent.y < this.y + this.getHeight()) && (this.parent.y + this.parent.height > this.y);
    }
}
