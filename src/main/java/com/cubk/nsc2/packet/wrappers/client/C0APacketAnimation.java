package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import io.netty.buffer.ByteBuf;

public class C0APacketAnimation implements ClientPacketWrapper {

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
    }

    @Override
    public String wrapToString() {
        return "new C0APacketAnimation()";
    }
}
