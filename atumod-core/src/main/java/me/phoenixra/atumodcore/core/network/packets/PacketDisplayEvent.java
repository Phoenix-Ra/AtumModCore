package me.phoenixra.atumodcore.core.network.packets;

import io.netty.buffer.ByteBuf;
import me.phoenixra.atumodcore.api.network.data.DisplayEventData;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.nio.charset.StandardCharsets;

public class PacketDisplayEvent implements IMessage {
    protected String atumModId;
    protected String elementId;

    protected int rendererId;
    protected int eventId;

    public PacketDisplayEvent(){
        atumModId = "";
        elementId = "";
        rendererId = 0;
        eventId = 0;
    }
    public PacketDisplayEvent(DisplayEventData data){
        this.atumModId = data.getAtumModId();
        this.elementId = data.getElementId();
        this.rendererId = data.getRendererId();
        this.eventId = data.getEventId();
    }

    public PacketDisplayEvent(String atumModId,
                              String elementId,
                              int rendererId,
                              int eventId){
        this.atumModId = atumModId;
        this.elementId = elementId;
        this.rendererId = rendererId;
        this.eventId = eventId;
    }



    @Override
    public void fromBytes(ByteBuf buf) {
        int atumModIdLength = buf.readInt();
        byte[] atumModIdBytes = new byte[atumModIdLength];
        buf.readBytes(atumModIdBytes);
        atumModId = new String(atumModIdBytes, 0, atumModIdLength, StandardCharsets.UTF_8);

        int elementIdLength = buf.readInt();
        byte[] elementIdBytes = new byte[elementIdLength];
        buf.readBytes(elementIdBytes);
        elementId = new String(elementIdBytes, 0, elementIdLength, StandardCharsets.UTF_8);

        rendererId = buf.readInt();
        eventId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        byte[] atumModIdBytes = atumModId.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(atumModIdBytes.length);
        buf.writeBytes(atumModIdBytes);

        byte[] elementIdBytes = elementId.getBytes(StandardCharsets.UTF_8);
        buf.writeInt(elementIdBytes.length);
        buf.writeBytes(elementIdBytes);

        buf.writeInt(rendererId);

        buf.writeInt(eventId);

    }

    public DisplayEventData asDisplayEventData(){
        return new DisplayEventData(
                atumModId,
                elementId,
                rendererId,
                eventId
        );
    }
}
