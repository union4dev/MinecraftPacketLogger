package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import com.cubk.nsc2.struct.Position;
import io.netty.buffer.ByteBuf;

public class C14PacketTabComplete implements ClientPacketWrapper {
    public String message;
    public Position targetBlock;

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.message = readStringFromBuffer(buf,32767);
        boolean flag = buf.readBoolean();

        if (flag) {
            this.targetBlock = readPosition(buf);
            packetData.getDataList().add(new PacketData.Data(Position.class, "targetBlock", targetBlock));
        }

        packetData.getDataList().add(new PacketData.Data(String.class, "message", message));
        packetData.getDataList().add(new PacketData.Data(Boolean.class, "hasTargetBlock", flag));
    }

    @Override
    public String wrapToString() {
        return "Wrap你妈大逼自己看不懂是不是";
    }
}
