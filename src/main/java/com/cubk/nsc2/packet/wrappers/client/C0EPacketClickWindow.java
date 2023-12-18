package com.cubk.nsc2.packet.wrappers.client;

import com.cubk.nsc2.packet.ClientPacketWrapper;
import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.struct.ItemStack;
import io.netty.buffer.ByteBuf;

public class C0EPacketClickWindow implements ClientPacketWrapper {
    public int windowId;
    public int slotId;
    public int usedButton;
    public short actionNumber;
    public ItemStack clickedItem;
    public int mode;

    @Override
    public void parser(ByteBuf buf, PacketData packetData) {
        this.windowId = buf.readByte();
        this.slotId = buf.readShort();
        this.usedButton = buf.readByte();
        this.actionNumber = buf.readShort();
        this.mode = buf.readByte();
        this.clickedItem = readItemStackFromBuffer(buf);

        packetData.getDataList().add(new PacketData.Data(Integer.class, "windowId", windowId));
        packetData.getDataList().add(new PacketData.Data(Integer.class, "slotId", slotId));
        packetData.getDataList().add(new PacketData.Data(Integer.class, "usedButton", usedButton));
        packetData.getDataList().add(new PacketData.Data(Short.class, "actionNumber", actionNumber));
        packetData.getDataList().add(new PacketData.Data(ItemStack.class, "clickedItem", clickedItem));
        packetData.getDataList().add(new PacketData.Data(Integer.class, "mode", mode));
    }

    @Override
    public String wrapToString() {
        return String.format("new C0EPacketClickWindow(%s,%s,%s,%s,%s,%s)", windowId, slotId, usedButton, mode, "item", actionNumber);
    }
}
