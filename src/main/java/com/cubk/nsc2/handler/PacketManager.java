package com.cubk.nsc2.handler;

import com.cubk.nsc2.NSC;
import com.cubk.nsc2.packet.PacketData;
import com.cubk.nsc2.struct.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PacketManager {

    public void onLoop() {
        while (true) {
            try {
                Object nh = Constant.method_getNetHandler.invoke(Constant.object_theMinecraft);
                if (nh != null) {
                    Object nm = Constant.method_getNetworkManager.invoke(nh);
                    if (nm != null)
                        inject(Constant.method_getNetworkManager.getReturnType(), nm);
                }

            } catch (Exception ignored) {
            }
        }
    }

    public void inject(Class<?> netClass, Object networkManager) {
        Channel channel = null;

        for (Field field : netClass.getDeclaredFields()) {
            try {
                if (field.getType().getName().equals("io.netty.channel.Channel")) {
                    try {
                        field.setAccessible(true);
                        channel = (Channel) field.get(networkManager);
                    } catch (Exception e) {
                        NSC.printStacktrace(e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (channel != null) {
            ChannelPipeline pipeline = channel.pipeline();
            if (pipeline.context(PacketListener.class) == null && pipeline.context("packet_handler") != null) {
                channel.pipeline().addBefore("packet_handler", "lunarclient", new PacketListener());

            }

        } else {
            JOptionPane.showMessageDialog(null, "Error [2]", "Internal error, failed to initialize\nNo channel found", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class PacketListener extends ChannelDuplexHandler {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PacketData wrapper = new PacketData(msg.getClass(), msg.getClass().getSimpleName(), false);
        if (!handle(false, msg, wrapper))
            super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        PacketData wrapper = new PacketData(msg.getClass(), msg.getClass().getSimpleName(), true);
        if (!handle(true, msg, wrapper))
            super.write(ctx, msg, promise);
    }

    private boolean handle(boolean outgoing, Object packet, PacketData wrapper) {
        if (outgoing) {
            try {
                Object sb = Constant.class_EnumConnectionState.getDeclaredField("PLAY").get(null);
                Object sb2 = Constant.class_EnumPacketDirection.getDeclaredField("SERVERBOUND").get(null);
                Integer id = (Integer) Constant.method_getPacketId.invoke(sb, sb2, packet);
                if (id == 3 && Constant.name_writeData == null) {
                    for (Method method : packet.getClass().getMethods()) {
                        try {
                            if (method.getParameters()[0].getType() != boolean.class) {
                                ByteBuf buf = Unpooled.buffer();
                                Constant.class_PacketBuffer = method.getParameters()[0].getType().getConstructor(ByteBuf.class);
                                Object packetBuffer = method.getParameters()[0].getType().getConstructor(ByteBuf.class).newInstance(buf);
                                try {
                                    method.invoke(packet, packetBuffer);
                                    Constant.name_writeData = method.getName();
                                } catch (Throwable ignored) {
                                }
                            }
                        } catch (Exception ignored) {

                        }
                    }
                }
                if (Constant.name_writeData != null) {
                    return NSC.getInstance().getPacketProcessor().processClientPacket(id, packet, wrapper);
                }
            } catch (Exception e) {
                NSC.printStacktrace(e);
            }
        } else {
            try {
                Object sb = Constant.class_EnumConnectionState.getDeclaredField("PLAY").get(null);
                Object sb2 = Constant.class_EnumPacketDirection.getDeclaredField("CLIENTBOUND").get(null);
                Integer id = (Integer) Constant.method_getPacketId.invoke(sb, sb2, packet);
                return NSC.getInstance().getPacketProcessor().processServerPacket(id, packet, wrapper);
            } catch (Exception e) {
                NSC.printStacktrace(e);
            }
        }
        return false;
    }
}
