package me.phoenixra.atumodcore.api.network.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data class for display action packets
 *
 * Use canvas and element null values if u perform global action
 */
@Data @AllArgsConstructor
public class DisplayActionData {
    private String atumModId;
    private String canvasId;
    private String elementId;
    private String actionId;
    private String[] args;
}
