package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import org.jetbrains.annotations.Nullable;

public class ElementCanvas extends BaseCanvas {
    public ElementCanvas(@Nullable DisplayCanvas elementOwner) {
        super(elementOwner);
    }

    @Override
    protected BaseCanvas onClone(BaseCanvas clone) {
        return clone;
    }
}
