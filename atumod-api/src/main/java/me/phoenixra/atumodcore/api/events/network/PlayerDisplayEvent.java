package me.phoenixra.atumodcore.api.events.network;

import lombok.Getter;
import me.phoenixra.atumodcore.api.network.data.ActiveDisplayData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;
@Getter
public class PlayerDisplayEvent extends Event {
    private final EntityPlayerMP playerMP;
    private final ActiveDisplayData data;
    private final String elementId;
    private final int eventId;

    public PlayerDisplayEvent(EntityPlayerMP playerMP,
                              ActiveDisplayData data,
                              String elementId,
                              int eventId) {
        this.playerMP = playerMP;
        this.data = data;
        this.eventId = eventId;
        this.elementId = elementId;
    }
    public PlayerDisplayEvent(EntityPlayerMP playerMP,
                              ActiveDisplayData data,
                              int eventId) {
        this(playerMP, data, data.getBaseCanvasId(), eventId);
    }
}
