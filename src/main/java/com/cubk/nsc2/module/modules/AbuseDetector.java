package com.cubk.nsc2.module.modules;

import com.cubk.nsc2.module.Module;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import com.cubk.nsc2.packet.wrappers.client.C03PacketPlayer;
import com.cubk.nsc2.packet.wrappers.client.C05PacketPlayerLook;
import com.cubk.nsc2.packet.wrappers.client.C06PacketPlayerPosLook;

public class AbuseDetector implements Module {

    @Override
    public boolean handleClientPacket(ClientPacketWrapper wrapper) {
        if (wrapper instanceof C03PacketPlayer) {
            if (wrapper instanceof C05PacketPlayerLook || wrapper instanceof C06PacketPlayerPosLook) {
                if (((C03PacketPlayer) wrapper).pitch > 90 || ((C03PacketPlayer) wrapper).pitch < -90) {
                    log("Invalid pitch detected, possibly exploit abuse! (" + ((C03PacketPlayer) wrapper).pitch + ")");
                }
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "Exploit Abuse Detector";
    }
}
