package me.phoenixra.atumodcore.api.display.actions;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.impl.BaseScreen;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data @AllArgsConstructor
public class ActionData {
    @NotNull
    private Minecraft minecraft;
    @Nullable
    private BaseScreen attachedGuiScreen;
    @Nullable
    private DisplayCanvas attachedCanvas;
    @Nullable
    private DisplayElement attachedElement;
    private int mouseX;
    private int mouseY;



}
