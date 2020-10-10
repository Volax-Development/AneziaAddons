package fr.volax.anezia.nbt;

public enum NBTType {
    NBTTagEnd(0),
    NBTTagCompound(10);

    private final int id;

    NBTType(int i) {
        this.id = i;
    }

    public int getId() {
        return this.id;
    }
}