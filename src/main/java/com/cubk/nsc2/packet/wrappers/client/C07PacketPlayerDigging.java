package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import com.cubk.nsc2.struct.EnumFacing;
import com.cubk.nsc2.struct.Position;
import io.netty.buffer.ByteBuf;

public class C07PacketPlayerDigging implements ClientPacketWrapper
{
    public Position position;
    public EnumFacing facing;
    public Action status;

    @Override
    public void parser(ByteBuf buf, PacketData packetData)
    {
        this.status = readEnumValue(Action.class,buf);
        this.position = readPosition(buf);
        this.facing = EnumFacing.getFront(buf.readUnsignedByte());
        packetData.getDataList().add(new PacketData.Data(Position.class,"position",position));
        packetData.getDataList().add(new PacketData.Data(EnumFacing.class,"facing",facing));
        packetData.getDataList().add(new PacketData.Data(Action.class,"action",status));
    }

    @Override
    public String wrapToString() {
        return String.format("new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.%s,new BlockPos(%s,%s,%s),EnumFacing.%s)",status.name(),position.getX(),position.getY(),position.getZ(),facing.name());
    }

    @SuppressWarnings("unused")
    public enum Action
    {
        START_DESTROY_BLOCK,
        ABORT_DESTROY_BLOCK,
        STOP_DESTROY_BLOCK,
        DROP_ALL_ITEMS,
        DROP_ITEM,
        RELEASE_USE_ITEM,
        SWAP_HELD_ITEMS,
    }
}
