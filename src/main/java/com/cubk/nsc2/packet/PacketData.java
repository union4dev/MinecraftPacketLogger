package com.cubk.nsc2.packet;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class PacketData {
    public final Class<?> packetClass;
    @Getter
    private final List<Data> dataList;
    public final boolean outgoing;
    public final long time;
    @Setter
    public String packetName;

    public PacketData(Class<?> packetClass, String packetName, boolean outgoing) {
        this.packetClass = packetClass;
        this.packetName = packetName;
        this.outgoing = outgoing;
        this.time = System.currentTimeMillis();
        this.dataList = new ArrayList<>();
    }

    public static class Data {
        public final Class<?> type;
        public final String name;
        public final String value;

        public Data(Class<?> type, String name, Object value) {
            this.type = type;
            this.name = name;
            this.value = String.valueOf(value);
        }
    }
}
