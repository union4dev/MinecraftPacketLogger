package com.cubk.nsc2.struct;

import com.cubk.nsc2.util.MathHelper;

public enum EnumFacing {
    DOWN,
    UP,
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public static EnumFacing getFront(int index)
    {
        return values()[MathHelper.abs_int(index % values().length)];
    }
}
