package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DisplayElementCanvas extends BaseCanvas {
    public DisplayElementCanvas(@Nullable DisplayCanvas elementOwner) {
        super(elementOwner);
    }

    @Override
    protected BaseCanvas onClone(BaseCanvas clone) {
        return clone;
    }
}
