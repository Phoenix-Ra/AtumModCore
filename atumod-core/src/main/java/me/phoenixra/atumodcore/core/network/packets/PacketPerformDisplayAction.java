package me.phoenixra.atumodcore.core.network.packets;

import io.netty.buffer.ByteBuf;
import me.phoenixra.atumodcore.api.network.data.DisplayActionData;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
public class PacketPerformDisplayAction implements IMessage {
    protected String canvasId;
    protected String elementId;
    protected String actionId;
    protected String args;


    public PacketPerformDisplayAction(){
        canvasId = "";
        elementId = "";
        actionId = "";
        args = "";
    }
    public PacketPerformDisplayAction(String canvasId,
                                      String elementId,
                                      @NotNull String actionId,
                                      @NotNull String[] args){
        this.canvasId = canvasId;
        if(this.canvasId==null)
            this.canvasId = "";
        this.elementId = elementId;
        if(this.elementId==null)
            this.elementId = "";
        this.actionId = actionId;
        StringBuilder argsBuilder = new StringBuilder();
        for(String arg : args){
            argsBuilder.append(arg).append(";");
        }
        this.args = argsBuilder.toString();
    }
    public PacketPerformDisplayAction(DisplayActionData data){
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
