package me.phoenixra.atumodcore.core.display.actions.server;

import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.api.display.annotations.RegisterDisplayAction;
import net.minecraftforge.fml.client.FMLClientHandler;

@RegisterDisplayAction(templateId = "connect_to_server")
public class ActionConnectToServer implements DisplayAction {
    @Override
    public void perform(ActionData data) {
        if(data.getActionArgs() == null) {
            System.out.println("No args provided for an action connect_to_server");
            return;
        }
        if(data.getActionArgs().getArgs().length == 0) {
            System.out.println("No args provided for an action connect_to_server");
            return;
        }
        String[] split = data.getActionArgs().getArgs()[0].split(":");
        if(split.length == 1){
            FMLClientHandler.instance().connectToServerAtStartup(
                    data.getActionArgs().getArgs()[0].split(":")[0],
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
