package me.phoenixra.atumodcore.api.display.actions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data @AllArgsConstructor @Builder
public class ActionData {
    @NotNull
    private AtumMod atumMod;
    @Nullable
    private DisplayElement attachedElement;
    @Nullable
    private Event attachedEvent;
    private int mouseX;
    private int mouseY;
    @Nullable
    private ActionArgs actionArgs;




    /**
     * Create an empty action data.
     *
     * @param atumMod The mod.
     * @return The action data.
     */
    public static ActionData empty(AtumMod atumMod){
        return new ActionData(atumMod,null, null, 0, 0, null);
    }



}
