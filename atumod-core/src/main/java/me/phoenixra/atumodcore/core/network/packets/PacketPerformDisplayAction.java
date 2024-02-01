package me.phoenixra.atumodcore.core.network.packets;

import io.netty.buffer.ByteBuf;
import me.phoenixra.atumodcore.api.AtumMod;
import me.phoenixra.atumodcore.api.network.data.DisplayActionData;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
public class PacketPerformDisplayAction implements IMessage {
    protected String atumModId;
    protected String elementId;
    protected String actionId;
    protected String args;

    protected int rendererId;


    public PacketPerformDisplayAction(){
        elementId = "";
        actionId = "";
        args = "";
        rendererId = 0;
    }
    public PacketPerformDisplayAction(AtumMod atumMod,
                                      String elementId,
                                      @NotNull String actionId,
                                      @NotNull String[] args,
                                      int rendererId){
        this.atumModId = atumMod.getModID();
        this.elementId = elementId;
        if(this.elementId==null)
            this.elementId = "";
        this.actionId = actionId;
        StringBuilder argsBuilder = new StringBuilder();
        for(String arg : args){
            argsBuilder.append(arg).append(";");
        }
        this.args = argsBuilder.toString();

        this.rendererId = rendererId;
    }
    public PacketPerformDisplayAction(DisplayActionData data){
        this.atumModId = data.getAtumModId();
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

        byte[] elementIdBytes = elementId.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(elementIdBytes.length);
        buf.writeBytes(elementIdBytes);

        byte[] actionIdBytes = actionId.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(actionIdBytes.length);
        buf.writeBytes(actionIdBytes);

        byte[] argsBytes = args.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(argsBytes.length);
        buf.writeBytes(argsBytes);

        buf.writeInt(rendererId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int atumModIdLength = buf.readInt();
        byte[] atumModIdBytes = new byte[atumModIdLength];
        buf.readBytes(atumModIdBytes);
        atumModId = new String(atumModIdBytes, StandardCharsets.UTF_8);

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

        rendererId = buf.readInt();
    }
}
