package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.ClientPacketWrapper;
import com.cubk.nsc2.packet.PacketData;
import io.netty.buffer.ByteBuf;

public class C0CPacketInput implements ClientPacketWrapper {
    public float strafeSpeed;

    /**
     * Positive for forward, negative for backward
     */
    public float forwardSpeed;
    public boolean jumping;
    public boolean sneaking;

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.strafeSpeed = buf.readFloat();
        this.forwardSpeed = buf.readFloat();
        byte b0 = buf.readByte();
        this.jumping = (b0 & 1) > 0;
        this.sneaking = (b0 & 2) > 0;
        packetData.getDataList().add(new PacketData.Data(Float.class, "strafe", strafeSpeed));
        packetData.getDataList().add(new PacketData.Data(Float.class, "forward", forwardSpeed));
        packetData.getDataList().add(new PacketData.Data(Boolean.class, "jumping", jumping));
        packetData.getDataList().add(new PacketData.Data(Boolean.class, "sneaking", sneaking));
    }

    @Override
    public String wrapToString() {
        return String.format("new C0CPacketInput(%s,%s,%s,%s)", strafeSpeed, forwardSpeed, jumping, sneaking);
    }
}
