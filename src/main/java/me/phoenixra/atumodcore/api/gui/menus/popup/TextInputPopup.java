package me.phoenixra.atumodcore.api.gui.menus.popup;
import me.phoenixra.atumodcore.api.gui.elements.GuiElementTextField;
import me.phoenixra.atumodcore.api.gui.elements.GuiElementButton;
import me.phoenixra.atumodcore.api.misc.CharacterFilter;
import me.phoenixra.atumodcore.core.input.KeyboardData;
import me.phoenixra.atumodcore.core.input.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.util.function.Consumer;

public class TextInputPopup extends Popup {

    protected Consumer<String> callback;
    protected String input = null;
    protected GuiElementTextField textField;
    protected GuiElementButton doneButton;
    protected Color color;
    protected int width = 250;
    protected String title = "";

    public TextInputPopup(Color color, String title, CharacterFilter filter, int alpha) {
        super(alpha);
        this.init(color, title, filter, null);
    }

    public TextInputPopup(Color color, String title, CharacterFilter filter, int backgroundAlpha, Consumer<String> callback) {
        super(backgroundAlpha);
        this.init(color, title, filter, callback);
    }

    protected void init(Color color, String title, CharacterFilter filter, Consumer<String> callback) {
        this.textField = new GuiElementTextField(Minecraft.getMinecraft().fontRenderer, 0, 0, 200, 20, true, filter);
        this.textField.setCanLoseFocus(true);
        this.textField.setFocused(false);
        this.textField.setMaxStringLength(1000);

        this.doneButton = new GuiElementButton(0, 0, 100, 20, "Done", true, (press) -> {
            this.input = this.textField.getText();
            this.setDisplayed(false);
            if (this.callback != null) {
                this.callback.accept(this.input);
            }
        });
        this.addButton(this.doneButton);

        if (title != null) {
            this.title = title;
        }

        this.color = color;
        this.callback = callback;

        KeyboardHandler.addKeyPressedListener(this::onEnterPressed);
        KeyboardHandler.addKeyPressedListener(this::onEscapePressed);
    }

    @Override
    public void render(int mouseX, int mouseY, GuiScreen renderIn) {
        super.render(mouseX, mouseY, renderIn);

        if(!this.isDisplayed()) {
            return;
        }
        int height = 100;

        GlStateManager.enableBlend();
        Gui.drawRect((renderIn.width / 2) - (this.width / 2), (renderIn.height / 2) - (height / 2), (renderIn.width / 2) + (this.width / 2), (renderIn.height / 2) + (height / 2), this.color.getRGB());
        GlStateManager.disableBlend();

        renderIn.drawCenteredString(Minecraft.getMinecraft().fontRenderer, title, renderIn.width / 2, (renderIn.height / 2) - (height / 2) + 10, Color.WHITE.getRGB());

        this.textField.x = (renderIn.width / 2) - (this.textField.width / 2);
        this.textField.y = (renderIn.height / 2) - (this.textField.height / 2);
        this.textField.drawTextBox();

        this.doneButton.x = (renderIn.width / 2) - (this.doneButton.width / 2);
        this.doneButton.y = ((renderIn.height / 2) + (height / 2)) - this.doneButton.height - 5;

        this.renderButtons(mouseX, mouseY);
    }

    public void setText(String text) {
        this.textField.setText("");
        this.textField.writeText(text);
    }

    public String getInput() {
        return this.input;
    }

    public void onEnterPressed(KeyboardData d) {
        if(d.keycode != 28 || !this.isDisplayed()) {
            return;
        }
        this.input = this.textField.getText().replace("\n", "").replace("\r", "");
        this.setDisplayed(false);
        if (this.callback != null) {
            this.callback.accept(this.input);
        }
    }

    public void onEscapePressed(KeyboardData d) {
        if(d.keycode != 1 || !this.isDisplayed()) {
            return;
        }
        this.setDisplayed(false);
        if (this.callback != null) {
            this.callback.accept(null);
        }
    }
}
