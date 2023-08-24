package me.phoenixra.atumodcore.api.display.actions;

@FunctionalInterface
public interface DisplayAction {
    void perform(ActionData data);
}
