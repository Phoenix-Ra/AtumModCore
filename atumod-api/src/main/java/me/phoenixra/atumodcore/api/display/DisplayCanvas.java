package me.phoenixra.atumodcore.api.display;

import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public interface DisplayCanvas extends DisplayElement, Cloneable{

    void addElement(@NotNull DisplayElement element);
    void removeElement(@NotNull DisplayElement element);
    void clearElements();
    @NotNull
    HashSet<DisplayElement> getDisplayedElements();

    @Nullable DisplayElement getHoveredElement(int mouseX, int mouseY);
    @Nullable DisplayElement getElementFromCoordinates(int posX, int posY);


    @NotNull DisplayRenderer getDisplayRenderer();
    void setDisplayRenderer(@NotNull DisplayRenderer displayRenderer);


    boolean isSetupState();
    void setSetupState(boolean setupState);

    void reloadCanvas();

    DisplayElement clone();

}
