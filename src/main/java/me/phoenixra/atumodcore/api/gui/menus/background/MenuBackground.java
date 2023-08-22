package me.phoenixra.atumodcore.api.gui.menus.background;

import lombok.Getter;
import me.phoenixra.atumodcore.api.registry.Registrable;
import net.minecraft.client.gui.GuiScreen;
import org.jetbrains.annotations.NotNull;

public abstract class MenuBackground implements Registrable {

    @Getter
    private final String ID;
    private final MenuBackgroundType type;

    public MenuBackground(@NotNull String id, @NotNull MenuBackgroundType type) {
        this.ID = id;
        this.type = type;
    }


    public abstract void onOpenMenu();

    public abstract void render(GuiScreen screen, boolean keepAspectRatio);

    public void onResetBackground() {
    }

    public MenuBackgroundType getType() {
        return this.type;
    }

}
