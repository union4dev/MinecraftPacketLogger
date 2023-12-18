package com.cubk.nsc2.module;

import com.cubk.nsc2.NSC;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import com.cubk.nsc2.packet.ServerPacketWrapper;

public interface Module {

    // Return true for cancel

    default boolean handleClientPacket(ClientPacketWrapper wrapper) {
        return false;
    }

    default boolean handleServerPacket(ServerPacketWrapper wrapper) {
        return false;
    }

    String getName();

    default void log(String message) {
        NSC.getInstance().getClickGui().log(message, getName());
    }
}
