package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.ClientPacketWrapper;
import com.cubk.nsc2.packet.PacketData;
import io.netty.buffer.ByteBuf;

public class C0FPacketConfirmTransaction implements ClientPacketWrapper {
    public int windowId;
    public short uid;
    public boolean accepted;

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.windowId = buf.readByte();
        this.uid = buf.readShort();
        this.accepted = buf.readByte() != 0;

        packetData.getDataList().add(new PacketData.Data(Integer.class, "windowId", windowId));
        packetData.getDataList().add(new PacketData.Data(Short.class, "uid", uid));
        packetData.getDataList().add(new PacketData.Data(Boolean.class, "accepted", accepted));
    }

    @Override
    public String wrapToString() {
        return String.format("new C0FPacketConfirmTransaction(%s,%s,%s)", windowId, uid, accepted);
    }
}
