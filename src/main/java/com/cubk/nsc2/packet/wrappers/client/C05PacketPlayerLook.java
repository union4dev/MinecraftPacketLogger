package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.PacketData;
import io.netty.buffer.ByteBuf;

public class C05PacketPlayerLook extends C03PacketPlayer {
    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.rotating = true;
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();


        super.parser(buf, packetData);
    }

    @Override
    public String wrapToString() {
        return String.format("new C03PacketPlayer.C05PacketPlayerLook(%s,%s,%s)", yaw, pitch, onGround);
    }
}
