package me.phoenixra.atumodcore.api.network.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActiveDisplayData {
    private int rendererId;
    private String baseCanvasId;
}
