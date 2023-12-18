package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import com.cubk.nsc2.struct.Vector3;
import io.netty.buffer.ByteBuf;

public class C02PacketUseEntity implements ClientPacketWrapper {
    public int entityId;
    public Action action;
    public Vector3 hitVec;


    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.entityId = readVarIntFromBuffer(buf);
        this.action = readEnumValue(Action.class,buf);
        packetData.getDataList().add(new PacketData.Data(Integer.class,"entityId",entityId));
        packetData.getDataList().add(new PacketData.Data(Action.class,"action",action));
        if (this.action == Action.INTERACT_AT)
        {
            this.hitVec = new Vector3(buf.readFloat(), buf.readFloat(), buf.readFloat());
            packetData.getDataList().add(new PacketData.Data(Vector3.class,"hitVec",hitVec));
        }
    }

    @Override
    public String wrapToString() {
        if(action == Action.INTERACT_AT){
            return String.format("new C02PacketUseEntity(%s,new Vec3(%s,%s,%s))",entityId,hitVec.x(),hitVec.y(),hitVec.z());
        }else {
            return String.format("new C02PacketUseEntity(%s,C02PacketUseEntity.Action.%s)",entityId,action.name());
        }
    }

    @SuppressWarnings("unused")
    public enum Action
    {
        INTERACT,
        ATTACK,
        INTERACT_AT;
    }
}