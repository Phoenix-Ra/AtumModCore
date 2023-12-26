package me.phoenixra.atumodcore.api.network.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DisplayEventData {
    private String atumModId;
    private String canvasId;
    private String elementId;
    private int eventId;
}
