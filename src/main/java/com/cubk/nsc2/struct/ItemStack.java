package com.cubk.nsc2.struct;

public class ItemStack {

    public int id,amount,meta;

    public ItemStack(int itemIn, int amount, int meta)
    {
        this.id = itemIn;
        this.amount = amount;
        this.meta = meta;
    }

    @Override
    public String toString() {
        return "ItemStack{" +
                "id=" + id +
                ", amount=" + amount +
                ", meta=" + meta +
                '}';
    }
}
