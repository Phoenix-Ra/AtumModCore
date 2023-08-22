package me.phoenixra.atumodcore.mod.test;

import me.phoenixra.atumodcore.api.config.Config;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.misc.AtumDebugger;
import me.phoenixra.atumodcore.api.utils.RenderUtils;
import me.phoenixra.atumodcore.mod.AtumModCore;
import net.minecraft.client.gui.GuiMainMenu;

public class TestMenu extends GuiMainMenu {

    private DisplayCanvas canvas;
    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(canvas==null){
            Config config = AtumModCore.getInstance().getConfigManager().getConfig("test")
                    .getSubsectionOrNull("loading_screen");
            canvas = AtumModCore.getInstance().getDisplayElementRegistry().compile(config);
        }
        new AtumDebugger(AtumModCore.getInstance(),"testMenu","Drawing the menu")
                .start(()->{
                    canvas.draw(RenderUtils.getScaleFactor(), 1, 1,mouseX,mouseY);
                });
    }
}
