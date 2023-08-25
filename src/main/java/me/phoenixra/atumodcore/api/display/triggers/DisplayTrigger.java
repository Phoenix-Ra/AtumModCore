package me.phoenixra.atumodcore.api.display.triggers;

import lombok.Getter;
import lombok.Setter;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.input.event.InputPressEvent;
import me.phoenixra.atumodcore.api.input.event.InputReleaseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class DisplayTrigger implements Cloneable{

    @Getter @Setter
    private DisplayElement attachedElement;
    @Getter
    private List<DisplayAction> actions = new ArrayList<>();

    public void dispatch(@NotNull TriggerData data){
        actions.forEach(action -> action.perform(data));
    }

    public void addAction(@NotNull DisplayAction action){
        actions.add(action);
    }



    @Override
    public DisplayTrigger clone() {
        try {
            DisplayTrigger clone = (DisplayTrigger) super.clone();
            clone.actions = new ArrayList<>(actions);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
