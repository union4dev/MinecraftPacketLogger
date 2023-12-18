package com.cubk.nsc2.transformer.transformers;

import com.cubk.nsc2.gui.AddressSwapperFrame;
import com.cubk.nsc2.transformer.Transformer;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class BootstrapTransformer implements Transformer {

    public static ChannelFuture connectHook(Bootstrap bootstrap, SocketAddress remoteAddress) {
        if (remoteAddress instanceof InetSocketAddress) {
            InetSocketAddress address = (InetSocketAddress) remoteAddress;

            if (AddressSwapperFrame.getUse().isSelected()) {
                if (address.getHostName().startsWith(AddressSwapperFrame.getOriginalHostField().getText())) {
                    remoteAddress = new InetSocketAddress(AddressSwapperFrame.getTargetServerField().getText(), Integer.parseInt(AddressSwapperFrame.getTargetPortField().getText()));
                }
            }

            try {
                Method localAddressMethod = AbstractBootstrap.class.getDeclaredMethod("localAddress");
                localAddressMethod.setAccessible(true);
                SocketAddress localAddress = (SocketAddress) localAddressMethod.invoke(bootstrap);

                Method doConnectMethod = Bootstrap.class.getDeclaredMethod("doConnect", SocketAddress.class, SocketAddress.class);
                doConnectMethod.setAccessible(true);
                return (ChannelFuture) doConnectMethod.invoke(bootstrap, remoteAddress, localAddress);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public void handle(ClassNode classNode) {
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("connect") && methodNode.desc.equals("(Ljava/net/SocketAddress;)Lio/netty/channel/ChannelFuture;")) {
                InsnList instructions = methodNode.instructions;

                instructions.clear();

                instructions.add(new VarInsnNode(ALOAD, 0));
                instructions.add(new VarInsnNode(ALOAD, 1));
                instructions.add(new MethodInsnNode(INVOKESTATIC, BootstrapTransformer.class.getName().replace(".", "/"), "connectHook", "(Lio/netty/bootstrap/Bootstrap;Ljava/net/SocketAddress;)Lio/netty/channel/ChannelFuture;", false));
                instructions.add(new InsnNode(ARETURN));

                methodNode.instructions = instructions;
            }
        }
    }

    @Override
    public String getTarget() {
        return "io/netty/bootstrap/Bootstrap";
    }
}
