package me.phoenixra.atumodcore.api.display.actions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
public class ActionArgs {
    private final String[] args;
    private final String rawArgs;

    public ActionArgs(String args) {
        this.rawArgs = args;
        this.args = args.split(";");
    }

}
