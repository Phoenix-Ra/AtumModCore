package me.phoenixra.atumodcore.api.gui.menus.background;

import lombok.Getter;
import me.phoenixra.atumodcore.api.registry.Registrable;
import me.phoenixra.atumodcore.api.registry.Registry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class MenuBackgroundType implements Registrable {

    @Getter
    private final String ID;

    protected Registry<MenuBackground> backgrounds = new Registry<>();

    public MenuBackgroundType(@Nonnull String id) {
        this.ID = id;
    }

    public abstract void loadBackgrounds();


    public void addBackground(@NotNull MenuBackground background) {
        this.backgrounds.register(background);
    }


    public void removeBackground(@NotNull String backgroundIdentifier) {
        this.backgrounds.remove(backgroundIdentifier);
    }

    @Nonnull
    public List<MenuBackground> getBackgrounds() {
        return new ArrayList<>(this.backgrounds.values());
    }

    public MenuBackground getBackgroundByIdentifier(String backgroundIdentifier) {
        return this.backgrounds.get(backgroundIdentifier);
    }


    public abstract String getDisplayName();

    public abstract List<String> getDescription();

    public abstract boolean needsInputString();


    public abstract MenuBackground createInstanceFromInputString(String inputString);


    public String inputStringButtonLabel() {
        return "Choose File";
    }

    public List<String> inputStringButtonTooltip() {
        return null;
    }


    @Override
    public void onRegister() {
        loadBackgrounds();
    }
}
