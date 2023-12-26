package me.phoenixra.atumodcore.core.network.packets;


import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerPerformDisplayAction implements IMessageHandler<PacketPerformDisplayAction, IMessage> {
    @Override
    public IMessage onMessage(PacketPerformDisplayAction message, MessageContext ctx) {
        AtumMod atumMod = AtumAPI.getInstance().getLoadedAtumMod(message.atumModId);
        if(atumMod==null) {
            return null;
        }
        if(message.canvasId.equals("null")){
            DisplayAction action = atumMod.getDisplayActionRegistry().getActionById(message.actionId);
            if(action==null) {
                return null;
            }
            action.perform(
                    ActionData.builder()
                            .atumMod(atumMod)
                            .attachedElement(null)
                            .attachedEvent(null)
                            .mouseX(0)
                            .mouseY(0)
                            .args(message.args.split(";"))
                            .build()
            );

        }
        DisplayCanvas canvas = atumMod.getEnabledCanvasRegistry().getCanvasById(message.canvasId);
        if(canvas==null) {
            return null;
        }
        canvas.getDisplayedElements().stream().filter(
                el -> el.getId().equals(message.elementId)
        ).findFirst().ifPresent(
                el -> el.performAction(
                        message.actionId,
                        message.args.split(";")
                )
        );


        return null;
    }
}
