package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.ClientPacketWrapper;
import com.cubk.nsc2.packet.PacketData;
import io.netty.buffer.ByteBuf;

public class C01PacketChatMessage implements ClientPacketWrapper {
    private String message;

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.message = readStringFromBuffer(buf, 100);
        packetData.getDataList().add(new PacketData.Data(String.class, "message", message));
    }

    @Override
    public String wrapToString() {
        return String.format("new C01PacketChatMessage(%s)", message);
    }
}
