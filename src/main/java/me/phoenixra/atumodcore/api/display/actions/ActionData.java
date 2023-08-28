package me.phoenixra.atumodcore.api.display.actions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.jetbrains.annotations.Nullable;

@Data @AllArgsConstructor @Builder
public class ActionData {
    @Nullable
    private DisplayElement attachedElement;
    @Nullable
    private Event attachedEvent;
    private int mouseX;
    private int mouseY;




    public static ActionData empty(){
        return new ActionData(null, null, 0, 0);
    }



}
