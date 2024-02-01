package me.phoenixra.atumodcore.core.network.packets;


import me.phoenixra.atumodcore.api.AtumAPI;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.display.DisplayCanvas;
import me.phoenixra.atumodcore.api.display.DisplayElement;
import me.phoenixra.atumodcore.api.display.DisplayRenderer;
import me.phoenixra.atumodcore.api.display.actions.ActionArgs;
import me.phoenixra.atumodcore.api.display.actions.ActionData;
import me.phoenixra.atumodcore.api.display.actions.DisplayAction;
import me.phoenixra.atumodcore.core.network.packets.PacketPerformDisplayAction;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketHandlerPerformDisplayAction implements IMessageHandler<PacketPerformDisplayAction, IMessage> {
    @Override
    public IMessage onMessage(PacketPerformDisplayAction message, MessageContext ctx) {
        System.out.println("ATUM MOD NAME FROM PACKET PerformDisplayAction: " + message.atumModId);
        AtumMod atumMod = AtumAPI.getInstance().getLoadedAtumMod(message.actionId);
        if(atumMod==null) {
            return null;
        }
        if(message.elementId.equals("null") || message.elementId.isEmpty()){
            DisplayAction action = atumMod.getDisplayManager().getActionRegistry().getActionById(message.actionId);
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
                            .actionArgs(new ActionArgs(message.args))
                            .build()
            );

        }
        DisplayRenderer renderer = atumMod.getDisplayManager().getRenderer(message.rendererId);
        if(renderer==null) {
            return null;
        }
        DisplayElement element = renderer.getBaseCanvas().getElement(message.elementId);
        if(element==null) {
            return null;
        }
        element.performAction(
                message.actionId,
                message.args
        );


        return null;
    }
}
