package me.phoenixra.atumodcore.mod.test;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import me.phoenixra.atumodcore.mod.AtumModCore;
import org.jetbrains.annotations.NotNull;

public class TestMenu extends BaseScreen {

    public TestMenu(@NotNull AtumMod atumMod) {
        super(atumMod,
                AtumModCore.getInstance().getDisplayElementRegistry().compile(
                        AtumModCore.getInstance().getConfigManager().getConfig("test")
                                .getSubsectionOrNull("loading_screen")
                )
        );
    }
}
