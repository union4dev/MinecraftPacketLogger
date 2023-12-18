package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import io.netty.buffer.ByteBuf;

public class C0DPacketCloseWindow implements ClientPacketWrapper {

    public int windowId;

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.windowId = buf.readByte();
        packetData.getDataList().add(new PacketData.Data(Integer.class,"windowId",windowId));
    }

    @Override
    public String wrapToString() {
        return String.format("new C0DPacketCloseWindow(%s)",windowId);
    }
}
