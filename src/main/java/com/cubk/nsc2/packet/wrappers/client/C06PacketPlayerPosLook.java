package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.PacketData;
import io.netty.buffer.ByteBuf;


public class C06PacketPlayerPosLook extends C03PacketPlayer {

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        super.parser(buf, packetData);
    }

    @Override
    public String wrapToString() {
        return String.format("new C03PacketPlayer.C06PacketPlayerPosLook(%s,%s,%s,%s,%s,%s)", x, y, z, yaw, pitch, onGround);
    }
}
