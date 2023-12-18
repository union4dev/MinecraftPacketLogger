package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import io.netty.buffer.ByteBuf;

public class C11PacketEnchantItem implements ClientPacketWrapper {
    public int windowId;
    public int button;

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.windowId = buf.readByte();
        this.button = buf.readByte();
        packetData.getDataList().add(new PacketData.Data(Integer.class, "windowId", windowId));
        packetData.getDataList().add(new PacketData.Data(Integer.class, "button", button));
    }

    @Override
    public String wrapToString() {
        return String.format("new C11PacketEnchantItem(%s, %s)", windowId, button);
    }
}
