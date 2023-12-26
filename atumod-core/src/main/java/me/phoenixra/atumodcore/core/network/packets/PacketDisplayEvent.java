package me.phoenixra.atumodcore.core.network.packets;

import io.netty.buffer.ByteBuf;
import me.phoenixra.atumodcore.api.network.data.DisplayEventData;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;

public class PacketDisplayEvent implements IMessage {
    protected String atumModId;
    protected String canvasId;
    protected String elementId;
    protected int eventId;

    public PacketDisplayEvent(){
        atumModId = "";
        canvasId = "";
        elementId = "";
        eventId = 0;
    }
    public PacketDisplayEvent(DisplayEventData data){
        this.atumModId = data.getAtumModId();
        this.canvasId = data.getCanvasId();
        this.elementId = data.getElementId();
        this.eventId = data.getEventId();
    }

    public PacketDisplayEvent(String atumModId,
                              String canvasId,
                              String elementId,
                              int eventId){
        this.atumModId = atumModId;
        this.canvasId = canvasId;
        this.elementId = elementId;
        this.eventId = eventId;
    }



    @Override
    public void fromBytes(ByteBuf buf) {
        int atumModIdLength = buf.readInt();
        byte[] atumModIdBytes = new byte[atumModIdLength];
        buf.readBytes(atumModIdBytes);
        atumModId = new String(atumModIdBytes, 0, atumModIdLength, StandardCharsets.UTF_8);

        int canvasIdLength = buf.readInt();
        byte[] canvasIdBytes = new byte[canvasIdLength];
        buf.readBytes(canvasIdBytes);
        canvasId = new String(canvasIdBytes, 0, canvasIdLength, StandardCharsets.UTF_8);

        int elementIdLength = buf.readInt();
        byte[] elementIdBytes = new byte[elementIdLength];
        buf.readBytes(elementIdBytes);
        elementId = new String(elementIdBytes, 0, elementIdLength, StandardCharsets.UTF_8);

        eventId = buf.readInt();
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

        buf.writeInt(eventId);

    }
}
