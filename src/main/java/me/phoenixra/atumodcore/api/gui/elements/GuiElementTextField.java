package me.phoenixra.atumodcore.api.gui.elements;

import me.phoenixra.atumodcore.api.gui.IGuiElement;
import me.phoenixra.atumodcore.mod.input.CharacterFilter;
import me.phoenixra.atumodcore.mod.input.KeyboardData;
import me.phoenixra.atumodcore.mod.input.KeyboardHandler;
import me.phoenixra.atumodcore.mod.input.MouseInput;
import me.phoenixra.atumodcore.api.utils.MathUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class GuiElementTextField extends GuiTextField implements IGuiElement {

    private int tick = 0;
    private boolean handleSelf;
    private CharacterFilter filter;
    private boolean leftDown = false;

    public GuiElementTextField(FontRenderer fontRenderer, int x, int y, int width, int height, boolean handleSelf, @Nullable CharacterFilter filter) {
        super(MathUtils.randInt(50, 400), fontRenderer, x, y, width, height);
        this.handleSelf = handleSelf;
        this.filter = filter;
        if (this.handleSelf) {
            KeyboardHandler.addKeyPressedListener(this::onKeyPress);
        }
    }

    @Override
    public void drawTextBox() {
        super.drawTextBox();
        if(!handleSelf) return;
        this.updateCursorCounter();
        if (MouseInput.isLeftMouseDown() && !this.leftDown) {
            this.mouseClicked(MouseInput.getMouseX(), MouseInput.getMouseY(), 0);
            this.leftDown = true;
        }
        if (!MouseInput.isLeftMouseDown()) {
            this.leftDown = false;
        }
    }

    @Override
    public void writeText(@NotNull String textToWrite) {
        if (this.filter != null) {
            textToWrite = this.filter.filterForAllowedChars(textToWrite);
        }
        super.writeText(textToWrite);
    }

    public boolean isHovered() {
        int mouseX = MouseInput.getMouseX();
        int mouseY = MouseInput.getMouseY();
        return (mouseX >= this.x) && (mouseX <= this.x + this.width) && (mouseY >= this.y) && mouseY <= this.y + this.height;
    }

    public boolean isEditable() {
        try {
            Field f = ObfuscationReflectionHelper.findField(GuiTextField.class, "field_146226_p");
            return f.getBoolean(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isLeftClicked() {
        return (this.isHovered() && MouseInput.isLeftMouseDown());
    }

    public void onKeyPress(KeyboardData d) {
        this.textboxKeyTyped(d.typedChar, d.keycode);
    }

    @Deprecated
    public boolean isEnabled() {
        return this.isEditable();
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
    public int getHeight() {
        return this.height;
    }
}
