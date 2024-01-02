package me.phoenixra.atumodcore.api.display;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DisplayRenderer {

    void initRenderer();

    void render(int mouseX, int mouseY);


    void reloadRenderer();


    void closeRenderer();
    void onRendererClosed();

    @Nullable
    BaseScreen getAttachedGuiScreen();
    @NotNull
    AtumMod getAtumMod();
}
