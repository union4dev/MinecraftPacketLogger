package com.cubk.nsc2.struct;


public final class Vector3 {

    public static Vector3 XN = new Vector3(-1.0F, 0.0F, 0.0F);

    public static Vector3 XP = new Vector3(1.0F, 0.0F, 0.0F);

    public static Vector3 YN = new Vector3(0.0F, -1.0F, 0.0F);

    public static Vector3 YP = new Vector3(0.0F, 1.0F, 0.0F);

    public static Vector3 ZN = new Vector3(0.0F, 0.0F, -1.0F);

    public static Vector3 ZP = new Vector3(0.0F, 0.0F, 1.0F);

    public static Vector3 ZERO = new Vector3(0.0F, 0.0F, 0.0F);

    private float x;

    private float y;

    private float z;

    public Vector3() {
    }

    public Vector3(float f, float g, float h) {
        this.x = f;
        this.y = g;
        this.z = h;
    }


    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        Vector3 vector3 = (Vector3) object;
        if (Float.compare(vector3.x, this.x) != 0)
            return false;
        if (Float.compare(vector3.y, this.y) != 0)
            return false;
        return (Float.compare(vector3.z, this.z) == 0);
    }

    public int hashCode() {
        int i = Float.floatToIntBits(this.x);
        i = 31 * i + Float.floatToIntBits(this.y);
        i = 31 * i + Float.floatToIntBits(this.z);
        return i;
    }

    public float x() {
        return this.x;
    }

    public float y() {
        return this.y;
    }

    public float z() {
        return this.z;
    }

    public void mul(float f) {
        this.x *= f;
        this.y *= f;
        this.z *= f;
    }

    public void mul(float f, float g, float h) {
        this.x *= f;
        this.y *= g;
        this.z *= h;
    }

    public void set(float f, float g, float h) {
        this.x = f;
        this.y = g;
        this.z = h;
    }

    public void load(Vector3 vector3) {
        this.x = vector3.x;
        this.y = vector3.y;
        this.z = vector3.z;
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

    public void sub(Vector3 vector3) {
        this.x -= vector3.x;
        this.y -= vector3.y;
        this.z -= vector3.z;
    }

    public float dot(Vector3 vector3) {
        return this.x * vector3.x + this.y * vector3.y + this.z * vector3.z;
    }


    public void cross(Vector3 vector3) {
        float f = this.x;
        float g = this.y;
        float h = this.z;
        float i = vector3.x();
        float j = vector3.y();
        float k = vector3.z();
        this.x = g * k - h * j;
        this.y = h * i - f * k;
        this.z = f * j - g * i;
    }

//    public void transform(Matrix3f matrix3f) {
//        float f = this.x;
//        float g = this.y;
//        float h = this.z;
//        this.x = matrix3f.m00 * f + matrix3f.m01 * g + matrix3f.m02 * h;
//        this.y = matrix3f.m10 * f + matrix3f.m11 * g + matrix3f.m12 * h;
//        this.z = matrix3f.m20 * f + matrix3f.m21 * g + matrix3f.m22 * h;
//    }


    public void lerp(Vector3 vector3, float f) {
        float g = 1.0F - f;
        this.x = this.x * g + vector3.x * f;
        this.y = this.y * g + vector3.y * f;
        this.z = this.z * g + vector3.z * f;
    }

    public Vector3 copy() {
        return new Vector3(this.x, this.y, this.z);
    }

    public String toString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + "]";
    }
}