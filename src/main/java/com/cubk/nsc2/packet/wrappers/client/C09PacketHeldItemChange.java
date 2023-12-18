package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import io.netty.buffer.ByteBuf;

public class C09PacketHeldItemChange implements ClientPacketWrapper {
    public int slotId;

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.slotId = buf.readShort();
        packetData.getDataList().add(new PacketData.Data(Integer.class,"slot",slotId));
    }

    @Override
    public String wrapToString() {
        return String.format("new C09PacketHeldItemChange(%s)",slotId);
    }
}
