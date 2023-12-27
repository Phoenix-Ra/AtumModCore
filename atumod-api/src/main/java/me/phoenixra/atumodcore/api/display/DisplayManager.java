package me.phoenixra.atumodcore.api.display;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.actions.DisplayActionRegistry;
import org.jetbrains.annotations.NotNull;

public interface DisplayManager {


    DisplayElementRegistry getElementRegistry();
    DisplayActionRegistry getActionRegistry();




    void addElementForHUD(@NotNull DisplayElement element);
    void removeElementFromHUD(@NotNull DisplayElement element);







    DisplayCanvas getEnabledCanvas(@NotNull String id);

    void registerEnabledCanvas(@NotNull String id, @NotNull DisplayCanvas canvas);
    void unregisterEnabledCanvas(@NotNull String id);
    /**
     * Get the mod.
     *
     * @return The mod instance.
     */
    @NotNull
    AtumMod getAtumMod();
}
