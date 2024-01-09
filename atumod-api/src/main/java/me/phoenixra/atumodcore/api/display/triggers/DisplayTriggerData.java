package me.phoenixra.atumodcore.api.display.triggers;

import lombok.Data;

@Data
public class DisplayTriggerData {

    private final String[] args;
    private final String rawArgs;

    public DisplayTriggerData(String args) {
        this.rawArgs = args;
        this.args = args.split(";");
    }

}
