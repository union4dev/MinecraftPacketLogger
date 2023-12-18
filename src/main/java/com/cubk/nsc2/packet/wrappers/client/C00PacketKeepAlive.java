package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import io.netty.buffer.ByteBuf;

public class C00PacketKeepAlive implements ClientPacketWrapper {
    public int key;
    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.key = readVarIntFromBuffer(buf);
        packetData.getDataList().add(new PacketData.Data(Integer.class,"key",key));
    }

    @Override
    public String wrapToString() {
        return String.format("new C00PacketKeepAlive(%s)",key);
    }
}
