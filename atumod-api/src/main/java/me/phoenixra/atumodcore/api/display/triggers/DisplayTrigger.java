package me.phoenixra.atumodcore.api.display.triggers;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DisplayTrigger extends Cloneable{


    void trigger(DisplayTriggerData triggerData);






    DisplayTrigger cloneWithNewVariables(@NotNull Config config,
                                         @Nullable DisplayRenderer owner);
    DisplayTrigger clone();

    DisplayRenderer getOwner();


    AtumMod getAtumMod();



}