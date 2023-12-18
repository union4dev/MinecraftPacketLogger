package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import io.netty.buffer.ByteBuf;

public class C13PacketPlayerAbilities implements ClientPacketWrapper {
    public boolean invulnerable;
    public boolean flying;
    public boolean allowFlying;
    public boolean creativeMode;
    public float flySpeed;
    public float walkSpeed;

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        byte b0 = buf.readByte();
        this.invulnerable = (b0 & 1) > 0;
        this.flying = (b0 & 2) > 0;
        this.allowFlying = (b0 & 4) > 0;
        this.creativeMode = (b0 & 8) > 0;
        this.flySpeed = buf.readFloat();
        this.walkSpeed = buf.readFloat();

        packetData.getDataList().add(new PacketData.Data(Boolean.class, "invulnerable", invulnerable));
        packetData.getDataList().add(new PacketData.Data(Boolean.class, "flying", flying));
        packetData.getDataList().add(new PacketData.Data(Boolean.class, "allowFlying", allowFlying));
        packetData.getDataList().add(new PacketData.Data(Boolean.class, "creativeMode", creativeMode));
        packetData.getDataList().add(new PacketData.Data(Float.class, "flySpeed", flySpeed));
        packetData.getDataList().add(new PacketData.Data(Float.class, "walkSpeed", walkSpeed));
    }

    @Override
    public String wrapToString() {
        return "Wrap你妈大逼自己看不懂是不是";
    }
}
