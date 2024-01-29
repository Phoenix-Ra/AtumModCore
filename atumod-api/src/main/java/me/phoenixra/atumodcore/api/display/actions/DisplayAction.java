package me.phoenixra.atumodcore.api.display.actions;

import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.tuples.Pair;
import me.phoenixra.atumodcore.api.tuples.PairRecord;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface DisplayAction {

    /**
     * Perform the action.
     *
     * @param data The action data.
     */
    void perform(ActionData data);


    @Nullable
    static Pair<DisplayAction, ActionArgs> parseActionFromString(
            AtumMod atumMod,
            String actionString
    ){
        String[] split = actionString.split("@");
        DisplayAction action = atumMod.getDisplayManager().getActionRegistry()
                .getActionById(split[0]);
        if(action == null){
            atumMod.getLogger().error("Could not find action: " + split[0]);
            return null;
        }
        ActionArgs args = null;
        if (split.length > 1) {
            args = new ActionArgs(split[1]);
        }
        return new Pair<>(action,args);
    }
}
