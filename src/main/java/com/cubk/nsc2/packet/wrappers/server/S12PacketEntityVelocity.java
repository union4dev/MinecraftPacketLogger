package com.cubk.nsc2.packet.wrappers.server;

import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.packet.ServerPacketWrapper;
import io.netty.buffer.ByteBuf;

public class S12PacketEntityVelocity implements ServerPacketWrapper {
    public int entityID;
    public int motionX;
    public int motionY;
    public int motionZ;

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.entityID = readVarIntFromBuffer(buf);
        this.motionX = buf.readShort();
        this.motionY = buf.readShort();
        this.motionZ = buf.readShort();

        packetData.getDataList().add(new PacketData.Data(Integer.class,"entityId",entityID));
        packetData.getDataList().add(new PacketData.Data(Integer.class,"motionX",motionX));
        packetData.getDataList().add(new PacketData.Data(Integer.class,"motionY",motionY));
        packetData.getDataList().add(new PacketData.Data(Integer.class,"motionZ",motionZ));
    }
}
