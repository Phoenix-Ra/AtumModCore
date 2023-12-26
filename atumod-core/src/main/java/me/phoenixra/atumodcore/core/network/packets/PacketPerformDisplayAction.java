package me.phoenixra.atumodcore.core.network.packets;

import io.netty.buffer.ByteBuf;
import me.phoenixra.atumodcore.api.network.data.DisplayActionData;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;
public class PacketPerformDisplayAction implements IMessage {
    protected String atumModId;
    protected String canvasId;
    protected String elementId;
    protected String actionId;
    protected String args;


    public PacketPerformDisplayAction(){
        atumModId = "";
        canvasId = "";
        elementId = "";
        actionId = "";
        args = "";
    }
    public PacketPerformDisplayAction(String atumModId,
                                      String canvasId,
                                      String elementId,
                                      String actionId,
                                      String[] args){
        this.atumModId = atumModId;
        this.canvasId = canvasId;
        this.elementId = elementId;
        this.actionId = actionId;
        StringBuilder argsBuilder = new StringBuilder();
        for(String arg : args){
            argsBuilder.append(arg).append(";");
        }
        this.args = argsBuilder.toString();
    }
    public PacketPerformDisplayAction(DisplayActionData data){
        this.atumModId = data.getAtumModId();
        this.canvasId = data.getCanvasId();
        this.elementId = data.getElementId();
        this.actionId = data.getActionId();
        StringBuilder argsBuilder = new StringBuilder();
        for(String arg : data.getArgs()){
            argsBuilder.append(arg).append(";");
        }
        this.args = argsBuilder.toString();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        byte[] atumModIdBytes = atumModId.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(atumModIdBytes.length);
        buf.writeBytes(atumModIdBytes);

        byte[] canvasIdBytes = canvasId.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(canvasIdBytes.length);
        buf.writeBytes(canvasIdBytes);

        byte[] elementIdBytes = elementId.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(elementIdBytes.length);
        buf.writeBytes(elementIdBytes);

        byte[] actionIdBytes = actionId.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(actionIdBytes.length);
        buf.writeBytes(actionIdBytes);

        byte[] argsBytes = args.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(argsBytes.length);
        buf.writeBytes(argsBytes);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int atumModIdLength = buf.readInt();
        byte[] atumModIdBytes = new byte[atumModIdLength];
        buf.readBytes(atumModIdBytes);
        atumModId = new String(atumModIdBytes, StandardCharsets.UTF_8);

        int canvasIdLength = buf.readInt();
        byte[] canvasIdBytes = new byte[canvasIdLength];
        buf.readBytes(canvasIdBytes);
        canvasId = new String(canvasIdBytes, StandardCharsets.UTF_8);

        int elementIdLength = buf.readInt();
        byte[] elementIdBytes = new byte[elementIdLength];
        buf.readBytes(elementIdBytes);
        elementId = new String(elementIdBytes, StandardCharsets.UTF_8);

        int actionIdLength = buf.readInt();
        byte[] actionIdBytes = new byte[actionIdLength];
        buf.readBytes(actionIdBytes);
        actionId = new String(actionIdBytes, StandardCharsets.UTF_8);

        int argsLength = buf.readInt();
        byte[] argsBytes = new byte[argsLength];
        buf.readBytes(argsBytes);
        args = new String(argsBytes, StandardCharsets.UTF_8);
    }
}
