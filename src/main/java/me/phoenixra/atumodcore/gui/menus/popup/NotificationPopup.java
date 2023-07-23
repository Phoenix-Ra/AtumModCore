package me.phoenixra.atumodcore.gui.menus.popup;

import me.phoenixra.atumodcore.gui.elements.GuiElementButton;
import me.phoenixra.atumodcore.input.KeyboardData;
import me.phoenixra.atumodcore.input.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationPopup extends Popup{
    protected List<String> text;
    protected GuiElementButton accept;
    protected int width;
    protected Color color = new Color(76, 0, 128);
    protected Runnable callback;

    public NotificationPopup(int width, @Nullable Color color, int backgroundAlpha, @Nullable Runnable callback, @Nonnull String... text) {
        super(backgroundAlpha);

        this.setNotificationText(text);
        this.width = width;
        this.accept = new GuiElementButton(0, 0, 100, 20, "Accept", true, (press) -> {
            this.setDisplayed(false);
            if (this.callback != null) {
                this.callback.run();
            }
        });
        this.addButton(this.accept);

        if (color != null) {
            this.color = color;
        }

        this.callback = callback;

        KeyboardHandler.addKeyPressedListener(this::onEnterOrEscapePressed);
    }

    @Override
    public void render(int mouseX, int mouseY, GuiScreen renderIn) {
        super.render(mouseX, mouseY, renderIn);

        if (this.isDisplayed()) {
            int height = 50;

            for (int i = 0; i < this.text.size(); i++) {
                height += 10;
            }

            GlStateManager.enableBlend();
            Gui.drawRect((renderIn.width / 2) - (this.width / 2), (renderIn.height / 2) - (height / 2), (renderIn.width / 2) + (this.width / 2), (renderIn.height / 2) + (height / 2), this.color.getRGB());
            GlStateManager.disableBlend();

            int i = 0;
            for (String s : this.text) {
                renderIn.drawCenteredString(Minecraft.getMinecraft().fontRenderer, s, renderIn.width / 2, (renderIn.height / 2) - (height / 2) + 10 + i, Color.WHITE.getRGB());
                i += 10;
            }

            this.accept.x = (renderIn.width / 2) - (this.accept.width / 2);
            this.accept.y = ((renderIn.height / 2) + (height / 2)) - this.accept.height - 5;

            this.renderButtons(mouseX, mouseY);
        }
    }

    public void setNotificationText(String... text) {
        if (text != null) {
            List<String> l = new ArrayList<String>();
            for (String s : text) {
                if (s.contains("%n%")) {
                    for (String s2 : s.split("%n%")) {
                        l.add(s2);
                    }
                } else {
                    l.add(s);
                }
            }
            this.text = l;
        }
    }

    public void onEnterOrEscapePressed(KeyboardData d) {
        if (((d.keycode == 28) || (d.keycode == 1)) && this.isDisplayed()) {
            this.setDisplayed(false);
            if (this.callback != null) {
                this.callback.run();
            }
        }
    }
}
