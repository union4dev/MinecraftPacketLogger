package com.cubk.nsc2;

import com.cubk.nsc2.gui.DebugFrame;
import com.cubk.nsc2.handler.PacketManager;
import com.cubk.nsc2.handler.PacketProcessor;
import com.cubk.nsc2.handler.ThreadScanner;
import lombok.Getter;

import java.io.PrintWriter;
import java.io.StringWriter;

public class NSC {

    @Getter
    private PacketProcessor packetProcessor;

    private DebugFrame debugFrame;

    @Getter
    private static NSC instance = new NSC();

    public static void printStacktrace(Throwable throwable) {
        throwable.printStackTrace();
    }

    public DebugFrame getClickGui() {
        return debugFrame;
    }

    public String getStacktrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    public void start() {
        System.out.println("[NSC] Initializing...");
        instance = this;
        PacketManager packetManager = new PacketManager();
        packetProcessor = new PacketProcessor();

        new ThreadScanner().start();
        new Thread(packetManager::onLoop).start();
        debugFrame = new DebugFrame();
    }

    public static native void log(String string);
}
