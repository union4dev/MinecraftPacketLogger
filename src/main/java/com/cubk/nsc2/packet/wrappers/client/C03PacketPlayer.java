package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.ClientPacketWrapper;
import com.cubk.nsc2.packet.PacketData;
import io.netty.buffer.ByteBuf;

public class C03PacketPlayer implements ClientPacketWrapper {

    public double x;
    public double y;
    public double z;
    public float yaw;
    public float pitch;
    public boolean onGround;
    public boolean rotating;

    @Override
    public void parser(ByteBuf buf, PacketData data) {
        onGround = buf.readUnsignedByte() != 0;
        data.getDataList().add(new PacketData.Data(Boolean.class, "onGround", onGround));
        data.getDataList().add(new PacketData.Data(Boolean.class, "rotating", rotating));

        data.getDataList().add(new PacketData.Data(Double.class, "x", this.x));
        data.getDataList().add(new PacketData.Data(Double.class, "y", this.y));
        data.getDataList().add(new PacketData.Data(Double.class, "z", this.z));

        data.getDataList().add(new PacketData.Data(Float.class, "yaw", this.yaw));
        data.getDataList().add(new PacketData.Data(Float.class, "pitch", this.pitch));
    }

    @Override
    public String wrapToString() {
        return String.format("new C03PacketPlayer(%s)", onGround);
    }
}
