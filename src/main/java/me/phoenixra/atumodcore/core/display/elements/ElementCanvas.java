package me.phoenixra.atumodcore.core.display.elements;

import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseCanvas;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class ElementCanvas extends BaseCanvas {
    public ElementCanvas(@Nullable DisplayCanvas elementOwner) {
        super(elementOwner);
    }

    @Override
    protected BaseCanvas onClone(BaseCanvas clone) {
        LinkedHashSet<DisplayElement> elements = new LinkedHashSet<>();
        clone.getElements().values().forEach(elements::addAll);
        clone.getElements().clear();
        for(DisplayElement element : elements){
            clone.addElement(element.clone());
        }
        return clone;
    }
}
