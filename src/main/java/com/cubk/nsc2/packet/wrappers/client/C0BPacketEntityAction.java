package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import io.netty.buffer.ByteBuf;

public class C0BPacketEntityAction implements ClientPacketWrapper {
    public int entityID;
    public Action action;
    public int auxData;

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.entityID = readVarIntFromBuffer(buf);
        this.action = readEnumValue(Action.class,buf);
        this.auxData = readVarIntFromBuffer(buf);
        packetData.getDataList().add(new PacketData.Data(Integer.class,"entityID",entityID));
        packetData.getDataList().add(new PacketData.Data(Action.class,"action",action));
        packetData.getDataList().add(new PacketData.Data(Integer.class,"auxData",auxData));
    }

    @Override
    public String wrapToString() {
        if(auxData == 0){
            return String.format("new C0BPacketEntityAction(%s,C0BPacketEntityAction.Action.%s)",entityID,action.name());
        }else {
            return String.format("new C0BPacketEntityAction(%s,C0BPacketEntityAction.Action.%s,%s)",entityID,action.name(),auxData);
        }
    }

    @SuppressWarnings("unused")
    public enum Action
    {
        START_SNEAKING,
        STOP_SNEAKING,
        STOP_SLEEPING,
        START_SPRINTING,
        STOP_SPRINTING,
        RIDING_JUMP,
        OPEN_INVENTORY;
    }
}