package com.cubk.nsc2.struct.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NBTTagList extends NBTBase {
    private List<NBTBase> tagList = new ArrayList<>();

    /**
     * The type byte for the tags in the list - they must all be of the same type.
     */
    private byte tagType = 0;

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    void write(DataOutput output) throws IOException {
        if (!this.tagList.isEmpty()) {
            this.tagType = ((NBTBase) this.tagList.get(0)).getId();
        } else {
            this.tagType = 0;
        }

        output.writeByte(this.tagType);
        output.writeInt(this.tagList.size());

        for (int i = 0; i < this.tagList.size(); ++i) {
            ((NBTBase) this.tagList.get(i)).write(output);
        }
    }

    public void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(296L);

        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        } else {
            this.tagType = input.readByte();
            int i = input.readInt();

            if (this.tagType == 0 && i > 0) {
                throw new RuntimeException("Missing type on ListTag");
            } else {
                sizeTracker.read(32L * (long) i);
                this.tagList = newArrayListWithCapacity(i);

                for (int j = 0; j < i; ++j) {
                    NBTBase nbtbase = NBTBase.createNewByType(this.tagType);
                    nbtbase.read(input, depth + 1, sizeTracker);
                    this.tagList.add(nbtbase);
                }
            }
        }
    }

    public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
        return new ArrayList(initialArraySize);
    }


    /**
     * Gets the type byte for the tag.
     */
    public byte getId() {
        return (byte) 9;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[");

        for (int i = 0; i < this.tagList.size(); ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append(i).append(':').append(this.tagList.get(i));
        }

        return stringbuilder.append(']').toString();
    }

    /**
     * Adds the provided tag to the end of the list. There is no check to verify this tag is of the same type as any
     * previous tag.
     */
    public void appendTag(NBTBase nbt) {
        if (nbt.getId() == 0) {
        } else {
            if (this.tagType == 0) {
                this.tagType = nbt.getId();
            } else if (this.tagType != nbt.getId()) {
                return;
            }

            this.tagList.add(nbt);
        }
    }

    /**
     * Set the given index to the given tag
     */
    public void set(int idx, NBTBase nbt) {
        if (nbt.getId() == 0) {
        } else if (idx >= 0 && idx < this.tagList.size()) {
            if (this.tagType == 0) {
                this.tagType = nbt.getId();
            } else if (this.tagType != nbt.getId()) {
                return;
            }

            this.tagList.set(idx, nbt);
        } else {
        }
    }

    /**
     * Removes a tag at the given index.
     */
    public NBTBase removeTag(int i) {
        return (NBTBase) this.tagList.remove(i);
    }

    /**
     * Return whether this compound has no tags.
     */
    public boolean hasNoTags() {
        return this.tagList.isEmpty();
    }

    /**
     * Retrieves the NBTTagCompound at the specified index in the list
     */
    public NBTTagCompound getCompoundTagAt(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase nbtbase = (NBTBase) this.tagList.get(i);
            return nbtbase.getId() == 10 ? (NBTTagCompound) nbtbase : new NBTTagCompound();
        } else {
            return new NBTTagCompound();
        }
    }

    public int[] getIntArrayAt(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase nbtbase = (NBTBase) this.tagList.get(i);
            return nbtbase.getId() == 11 ? ((NBTTagIntArray) nbtbase).getIntArray() : new int[0];
        } else {
            return new int[0];
        }
    }

    public double getDoubleAt(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase nbtbase = (NBTBase) this.tagList.get(i);
            return nbtbase.getId() == 6 ? ((NBTTagDouble) nbtbase).getDouble() : 0.0D;
        } else {
            return 0.0D;
        }
    }

    public float getFloatAt(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase nbtbase = (NBTBase) this.tagList.get(i);
            return nbtbase.getId() == 5 ? ((NBTTagFloat) nbtbase).getFloat() : 0.0F;
        } else {
            return 0.0F;
        }
    }

    /**
     * Retrieves the tag String value at the specified index in the list
     */
    public String getStringTagAt(int i) {
        if (i >= 0 && i < this.tagList.size()) {
            NBTBase nbtbase = (NBTBase) this.tagList.get(i);
            return nbtbase.getId() == 8 ? nbtbase.getString() : nbtbase.toString();
        } else {
            return "";
        }
    }

    /**
     * Get the tag at the given position
     */
    public NBTBase get(int idx) {
        return (NBTBase) (idx >= 0 && idx < this.tagList.size() ? (NBTBase) this.tagList.get(idx) : new NBTTagEnd());
    }

    /**
     * Returns the number of tags in the list.
     */
    public int tagCount() {
        return this.tagList.size();
    }

    /**
     * Creates a clone of the tag.
     */
    public NBTBase copy() {
        NBTTagList nbttaglist = new NBTTagList();
        nbttaglist.tagType = this.tagType;

        for (NBTBase nbtbase : this.tagList) {
            NBTBase nbtbase1 = nbtbase.copy();
            nbttaglist.tagList.add(nbtbase1);
        }

        return nbttaglist;
    }

    public boolean equals(Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            NBTTagList nbttaglist = (NBTTagList) p_equals_1_;

            if (this.tagType == nbttaglist.tagType) {
                return this.tagList.equals(nbttaglist.tagList);
            }
        }

        return false;
    }

    public int hashCode() {
        return super.hashCode() ^ this.tagList.hashCode();
    }

    public int getTagType() {
        return this.tagType;
    }
}
