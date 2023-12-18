package com.cubk.nsc2.packet;

import com.cubk.nsc2.struct.ItemStack;
import com.cubk.nsc2.struct.Position;
import com.cubk.nsc2.struct.nbt.NBTBase;
import com.cubk.nsc2.struct.nbt.NBTSizeTracker;
import com.cubk.nsc2.struct.nbt.NBTTagCompound;
import com.cubk.nsc2.struct.nbt.NBTTagEnd;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.DecoderException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public interface Packet {
    void parser(ByteBuf buf, PacketData packetData);

    default Position readPosition(ByteBuf buf) {
        return Position.fromLong(buf.readLong());
    }

    default <T extends Enum<T>> T readEnumValue(Class<T> enumClass, ByteBuf buf) {
        return (T) ((Enum[]) enumClass.getEnumConstants())[readVarIntFromBuffer(buf)];
    }

    default String readStringFromBuffer(ByteBuf buf, int maxLength) {
        int i = this.readVarIntFromBuffer(buf);

        if (i > maxLength * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
        } else if (i < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            String s = new String(buf.readBytes(i).array(), StandardCharsets.UTF_8);

            if (s.length() > maxLength) {
                throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
            } else {
                return s;
            }
        }
    }

    default ItemStack readItemStackFromBuffer(ByteBuf buf) {
        ItemStack itemstack = null;
        int i = buf.readShort();

        if (i >= 0) {
            try {
                int j = buf.readByte();
                int k = buf.readShort();
                itemstack = new ItemStack(i, j, k);
                this.readNBTTagCompoundFromBuffer(buf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return itemstack;
    }

    default NBTTagCompound readNBTTagCompoundFromBuffer(ByteBuf buf) throws IOException {
        int i = buf.readerIndex();
        byte b0 = buf.readByte();

        if (b0 == 0) {
            return null;
        } else {
            buf.readerIndex(i);
            return read(new ByteBufInputStream(buf), new NBTSizeTracker(2097152L));
        }
    }

    default NBTTagCompound read(File p_74797_0_) throws IOException {
        if (!p_74797_0_.exists()) {
            return null;
        } else {
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(p_74797_0_));
            NBTTagCompound nbttagcompound;

            try {
                nbttagcompound = read(datainputstream, NBTSizeTracker.INFINITE);
            } finally {
                datainputstream.close();
            }

            return nbttagcompound;
        }
    }

    default NBTTagCompound read(DataInput p_152456_0_, NBTSizeTracker p_152456_1_) throws IOException {
        NBTBase nbtbase = func_152455_a(p_152456_0_, 0, p_152456_1_);

        if (nbtbase instanceof NBTTagCompound) {
            return (NBTTagCompound) nbtbase;
        } else {
            throw new IOException("Root tag must be a named compound tag");
        }
    }

    default NBTBase func_152455_a(DataInput p_152455_0_, int p_152455_1_, NBTSizeTracker p_152455_2_) throws IOException {
        byte b0 = p_152455_0_.readByte();

        if (b0 == 0) {
            return new NBTTagEnd();
        } else {


            try {
                p_152455_0_.readUTF();
                NBTBase nbtbase = NBTBase.createNewByType(b0);
                nbtbase.read(p_152455_0_, p_152455_1_, p_152455_2_);
                return nbtbase;
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
                return null;
            }
        }
    }


    default int readVarIntFromBuffer(ByteBuf buf) {
        int i = 0;
        int j = 0;

        while (true) {
            byte b0 = buf.readByte();
            i |= (b0 & 127) << j++ * 7;

            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }

            if ((b0 & 128) != 128) {
                break;
            }
        }

        return i;
    }
}
