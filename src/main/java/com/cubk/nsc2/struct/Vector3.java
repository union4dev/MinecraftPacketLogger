package com.cubk.nsc2.struct;


import lombok.Data;

@Data
public final class Vector3 {
    private float x;
    private float y;
    private float z;

    public Vector3(float f, float g, float h) {
        this.x = f;
        this.y = g;
        this.z = h;
    }

    public void add(float f, float g, float h) {
        this.x += f;
        this.y += g;
        this.z += h;
    }

    public void add(Vector3 vector3) {
        this.x += vector3.x;
        this.y += vector3.y;
        this.z += vector3.z;
    }
}