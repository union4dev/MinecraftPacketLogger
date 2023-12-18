package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import com.cubk.nsc2.struct.ItemStack;
import com.cubk.nsc2.struct.Position;
import io.netty.buffer.ByteBuf;

public class C08PacketPlayerBlockPlacement implements ClientPacketWrapper {

    public Position position;
    public int placedBlockDirection;
    public ItemStack stack;
    public float facingX;
    public float facingY;
    public float facingZ;

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.position = readPosition(buf);
        this.placedBlockDirection = buf.readUnsignedByte();
        this.stack = readItemStackFromBuffer(buf);
        this.facingX = (float) buf.readUnsignedByte() / getOffset();
        this.facingY = (float) buf.readUnsignedByte() / getOffset();
        this.facingZ = (float) buf.readUnsignedByte() / getOffset();

        packetData.getDataList().add(new PacketData.Data(Position.class,"position",position));
        packetData.getDataList().add(new PacketData.Data(Integer.class,"placedBlockDirection",placedBlockDirection));
        packetData.getDataList().add(new PacketData.Data(ItemStack.class,"stack",stack));
        packetData.getDataList().add(new PacketData.Data(Float.class,"facingX",facingX));
        packetData.getDataList().add(new PacketData.Data(Float.class,"facingY",facingY));
        packetData.getDataList().add(new PacketData.Data(Float.class,"facingZ",facingZ));
    }

    private float getOffset() {
        return 16.0F;
    }

    @Override
    public String wrapToString() {
        return String.format("new C08PacketPlayerBlockPlacement(new BlockPos(%s,%s,%s),%s,%s,%s,%s,%s)",position.getX(),position.getY(),position.getZ(),placedBlockDirection,"item",facingX,facingY,facingZ);
    }
}
