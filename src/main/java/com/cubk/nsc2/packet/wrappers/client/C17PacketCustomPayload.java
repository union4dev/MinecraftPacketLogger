package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import io.netty.buffer.ByteBuf;

public class C17PacketCustomPayload implements ClientPacketWrapper {
    public String channel;
    public ByteBuf data;

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.channel = readStringFromBuffer(buf,20);
        int length = buf.readableBytes();

        if (length >= 0 && length <= 32767) {
            this.data = buf.readBytes(length).readSlice(length); // Retain the slice to prevent early release
            packetData.getDataList().add(new PacketData.Data(String.class, "channel", channel));
            packetData.getDataList().add(new PacketData.Data(ByteBuf.class, "data", data));
        }
    }

    @Override
    public String wrapToString() {
        return String.format("new C17PacketCustomPayload(\"%s\", %s)", channel, data);
    }
}
