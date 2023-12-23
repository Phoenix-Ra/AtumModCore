package me.phoenixra.atumodcore.core.display.actions;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import net.minecraftforge.fml.client.FMLClientHandler;

public class ActionConnectToServer implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getArgs() == null) {
            System.out.println("No args provided for an action connect_to_server");
            return;
        }
        if(data.getArgs().length == 0) {
            System.out.println("No args provided for an action connect_to_server");
            return;
        }
        String[] split = data.getArgs()[0].split(":");
        if(split.length == 1){
            FMLClientHandler.instance().connectToServerAtStartup(
                    data.getArgs()[0].split(":")[0],
                    25565
            );
        }else {
            FMLClientHandler.instance().connectToServerAtStartup(
                    split[0],
                    Integer.parseInt(split[1])
            );
        }
    }
}
