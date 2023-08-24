package me.phoenixra.atumodcore.api.display;

import me.phoenixra.atumodcore.api.config.Config;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public interface DisplayCanvas extends DisplayElement{

    void addElement(@NotNull DisplayElement element);
    void removeElement(@NotNull DisplayElement element);
    @NotNull
    HashSet<DisplayElement> getDisplayedElements();

    @Nullable DisplayElement getHoveredElement(int mouseX, int mouseY);
    @Nullable DisplayElement getElementFromCoordinates(int posX, int posY);

}
