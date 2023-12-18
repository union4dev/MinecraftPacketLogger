package com.cubk.nsc2.module.modules;

import com.cubk.nsc2.module.Module;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import com.cubk.nsc2.packet.wrappers.client.C04PacketPlayerPosition;
import com.cubk.nsc2.packet.wrappers.client.C05PacketPlayerLook;
import com.cubk.nsc2.packet.wrappers.client.C06PacketPlayerPosLook;
import com.cubk.nsc2.struct.Position;
import com.cubk.nsc2.util.MathHelper;

public class MotionCalculator implements Module {
    public Position lastPosition = null;
    public float yaw;
    public float pitch;

    public double speed;
    public double motionX;
    public double motionY;
    public double motionZ;
    public float movementYaw;

    @Override
    public String getName() {
        return "Motion";
    }

    @Override
    public boolean handleClientPacket(ClientPacketWrapper packet) {
        if (packet instanceof C04PacketPlayerPosition || packet instanceof C06PacketPlayerPosLook || packet instanceof C05PacketPlayerLook) {
            double speed = 0;
            if (packet instanceof C04PacketPlayerPosition) {
                final C04PacketPlayerPosition playerPosition = (C04PacketPlayerPosition) packet;
                if (lastPosition != null) {
                    motionX = playerPosition.x - lastPosition.getX();
                    motionY = playerPosition.y - lastPosition.getY();
                    motionZ = playerPosition.z - lastPosition.getZ();
                    speed = Math.sqrt(motionX * motionX + motionZ * motionZ);
                    double movementYawRad = Math.atan2(-motionX, motionZ);
                    movementYaw = (float) Math.toDegrees(movementYawRad);
                    movementYaw = MathHelper.wrapAngleTo180_float(movementYaw);
                }
                lastPosition = new Position(playerPosition.x, playerPosition.y, playerPosition.z);
            } else if(packet instanceof C06PacketPlayerPosLook) {
                final C06PacketPlayerPosLook playerPosLook = (C06PacketPlayerPosLook) packet;
                if (lastPosition != null) {
                    this.yaw = playerPosLook.yaw;
                    this.pitch = playerPosLook.pitch;
                    motionX = playerPosLook.x - lastPosition.getX();
                    motionY = playerPosLook.y - lastPosition.getY();
                    motionZ = playerPosLook.z - lastPosition.getZ();
                    speed = Math.sqrt(motionX * motionX + motionZ * motionZ);
                    double movementYawRad = Math.atan2(-motionX, motionZ);
                    movementYaw = (float) Math.toDegrees(movementYawRad);
                    movementYaw = MathHelper.wrapAngleTo180_float(movementYaw);
                }
                lastPosition = new Position(playerPosLook.x, playerPosLook.y, playerPosLook.z);
            } else {
                C05PacketPlayerLook packetPlayerLook = (C05PacketPlayerLook) packet;
                this.yaw = packetPlayerLook.yaw;
                this.pitch = packetPlayerLook.pitch;
            }
            this.speed = speed * 20;
        }
        return false;
    }

    public float getStrafeDifferences(){
        return Math.abs(MathHelper.wrapAngleTo180_float(movementYaw - yaw));
    }
}
