package me.phoenixra.atumodcore.api.display.triggers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.jetbrains.annotations.Nullable;

@Data @AllArgsConstructor @Builder
public class TriggerData {
    @Nullable
    private DisplayElement attachedElement;
    @Nullable
    private Event attachedEvent;
    private int mouseX;
    private int mouseY;




    public static TriggerData empty(){
        return new TriggerData(null, null, 0, 0);
    }



}
