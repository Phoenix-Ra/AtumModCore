package me.phoenixra.atumodcore.mod.test;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import me.phoenixra.atumodcore.mod.AtumModCore;
import org.jetbrains.annotations.NotNull;

public class TestMenu extends BaseScreen {

    private static TestMenu instance;

    public TestMenu(@NotNull AtumMod atumMod) {
        super(atumMod,
                (DisplayCanvas)( AtumModCore.getInstance().getDisplayElementRegistry().getCanvasTemplate(
                        AtumModCore.getInstance().getConfigManager().getConfig("test")
                                .getString("main_menu")
                ).clone())
        );
        if(instance != null) {
            instance.getCanvas().onRemove();
        }
        instance = this;
    }
}