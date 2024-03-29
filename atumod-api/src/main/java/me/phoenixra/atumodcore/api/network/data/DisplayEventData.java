package me.phoenixra.atumodcore.api.network.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DisplayEventData {
    private String atumModId;
    private String elementId;
    private int rendererId;
    private int eventId;



    public static final int EVENT_OPENED = 0;
    public static final int EVENT_CLOSED = 1;

}
