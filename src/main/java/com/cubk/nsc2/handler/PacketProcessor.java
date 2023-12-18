package com.cubk.nsc2.handler;

import com.cubk.nsc2.NSC;
import com.cubk.nsc2.module.Module;
import com.cubk.nsc2.module.modules.AbuseDetector;
import com.cubk.nsc2.module.modules.MotionCalculator;
import com.cubk.nsc2.packet.ClientPacketWrapper;
import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.packet.ServerPacketWrapper;
import com.cubk.nsc2.packet.wrappers.client.*;
import com.cubk.nsc2.packet.wrappers.server.S12PacketEntityVelocity;
import com.cubk.nsc2.struct.Constant;
import com.cubk.nsc2.util.TimerUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class PacketProcessor {
    private static final Map<Integer, ClientPacketWrapper> wrappersClient = new ConcurrentHashMap<>();
    private static final Map<Integer, ServerPacketWrapper> wrappersServer = new ConcurrentHashMap<>();

    private final TimerUtil timer = new TimerUtil();
    private final List<Module> modules = new ArrayList<>();
    private final List<Long> tick = new ArrayList<>();

    private final MotionCalculator motionCalculator;

    public PacketProcessor(){
        modules.add(new AbuseDetector());
        modules.add(motionCalculator = new MotionCalculator());
    }

    static {
        wrappersClient.put(0x00,new C00PacketKeepAlive());
        wrappersClient.put(0x01,new C01PacketChatMessage());
        wrappersClient.put(0x02,new C02PacketUseEntity());
        wrappersClient.put(0x03,new C03PacketPlayer());
        wrappersClient.put(0x04,new C04PacketPlayerPosition());
        wrappersClient.put(0x05,new C05PacketPlayerLook());
        wrappersClient.put(0x06,new C06PacketPlayerPosLook());
        wrappersClient.put(0x07,new C07PacketPlayerDigging());
        wrappersClient.put(0x08,new C08PacketPlayerBlockPlacement());
        wrappersClient.put(0x09,new C09PacketHeldItemChange());
        wrappersClient.put(0x0A,new C0APacketAnimation());
        wrappersClient.put(0x0B,new C0BPacketEntityAction());
        wrappersClient.put(0x0C,new C0CPacketInput());
        wrappersClient.put(0x0D,new C0DPacketCloseWindow());
        wrappersClient.put(0x0E,new C0EPacketClickWindow());
        wrappersClient.put(0x0F,new C0FPacketConfirmTransaction());
        wrappersClient.put(0x10,new C10PacketCreativeInventoryAction());
        wrappersClient.put(0x11,new C11PacketEnchantItem());
        wrappersClient.put(0x13,new C13PacketPlayerAbilities());
        wrappersClient.put(0x14,new C14PacketTabComplete());
        wrappersClient.put(0x17,new C17PacketCustomPayload());

        wrappersServer.put(0x12,new S12PacketEntityVelocity());
    }

    public void loop() {

        NSC.getInstance().getClickGui().setData("");

        StringBuilder sb = new StringBuilder();

        sb.append("Timer: ").append(getPPS() / 20f).append("\n");
        sb.append("MotionX: ").append(motionCalculator.motionX).append("\n");
        sb.append("MotionY: ").append(motionCalculator.motionY).append("\n");
        sb.append("MotionZ: ").append(motionCalculator.motionZ).append("\n");

        sb.append("Rotation: (").append(motionCalculator.yaw).append("/").append(motionCalculator.pitch).append(")\n");

        if(motionCalculator.motionX != 0 || motionCalculator.motionZ != 0){
            sb.append("Speed: ").append(motionCalculator.speed).append("\n");
            sb.append("Movement Yaw: ").append(motionCalculator.movementYaw);

            sb.append(" (").append(motionCalculator.getStrafeDifferences()).append(")");

            sb.append("\n");
        }

        if(motionCalculator.lastPosition != null){
            sb.append("Position: ").append(motionCalculator.lastPosition).append("\n");
        }

        NSC.getInstance().getClickGui().setOtherInfo(sb.toString());

        timer.reset();
    }

    private int getPPS() {
        long time = System.currentTimeMillis();
        this.tick.removeIf(aLong -> aLong + 1000L < time);
        return this.tick.size();
    }


    public boolean processServerPacket(int id, Object packet, PacketData pw){

        try {
            ServerPacketWrapper wrapper = wrappersServer.getOrDefault(id,null);

            if(wrapper == null){
                return false;
            }

            wrapper = wrapper.getClass().newInstance();

            ByteBuf buf = Unpooled.buffer();
            Object buffer = Constant.class_PacketBuffer.newInstance(buf);
            packet.getClass().getDeclaredMethod(Constant.name_writeData,buffer.getClass()).invoke(packet, buffer);
            wrapper.parser(buf,pw);

            NSC.getInstance().getClickGui().addPacket(pw);

            AtomicBoolean AtomicFuck = new AtomicBoolean(false);
            ServerPacketWrapper finalWrapper = wrapper;
            modules.forEach(module -> AtomicFuck.set(module.handleServerPacket(finalWrapper)));
            loop();
            return AtomicFuck.get();
        }catch (Exception e){
            NSC.printStacktrace(e);
        }
        return false;
    }

    public boolean processClientPacket(int id, Object packet, PacketData pw){
        try {
            ClientPacketWrapper wrapper = wrappersClient.getOrDefault(id,null);

            if(wrapper == null){
                NSC.getInstance().getClickGui().log("Failed to parse packet: " + id + " " + packet.getClass().getSimpleName(),"warn");
                return false;
            }

            wrapper = wrapper.getClass().newInstance();

            ByteBuf buf = Unpooled.buffer();
            Object buffer = Constant.class_PacketBuffer.newInstance(buf);
            packet.getClass().getDeclaredMethod(Constant.name_writeData,buffer.getClass()).invoke(packet, buffer);
            pw.setPacketName(wrapper.getClass().getSimpleName());
            wrapper.parser(buf,pw);

            NSC.getInstance().getClickGui().addPacket(pw);

            if(wrapper instanceof C03PacketPlayer){
                tick.add(System.currentTimeMillis() + 10L);
            }

            AtomicBoolean AtomicFuck = new AtomicBoolean(false);
            ClientPacketWrapper finalWrapper = wrapper;
            modules.forEach(module -> AtomicFuck.set(module.handleClientPacket(finalWrapper)));
            loop();
            return AtomicFuck.get();
        }catch (Exception e){
            NSC.printStacktrace(e);
        }
        return false;
    }


}
