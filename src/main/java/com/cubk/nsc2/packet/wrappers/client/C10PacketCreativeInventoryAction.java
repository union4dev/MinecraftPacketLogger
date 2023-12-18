package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.ClientPacketWrapper;
import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.struct.ItemStack;
import io.netty.buffer.ByteBuf;

public class C10PacketCreativeInventoryAction implements ClientPacketWrapper {
    public int slotId;
    public ItemStack stack;

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.slotId = buf.readShort();
        this.stack = readItemStackFromBuffer(buf);
        packetData.getDataList().add(new PacketData.Data(Integer.class, "slotId", slotId));
        packetData.getDataList().add(new PacketData.Data(ItemStack.class, "stack", stack));
    }

    @Override
    public String wrapToString() {
        return String.format("new C10PacketCreativeInventoryAction(%s, %s)", slotId, stack != null ? stack.toString() : "null");
    }
}
