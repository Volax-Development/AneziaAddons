package fr.volax.anezia.nbt;

import org.bukkit.Bukkit;

public enum ClassWrapper {
    CRAFT_ITEMSTACK("org.bukkit.craftbukkit.", ".inventory.CraftItemStack"),
    NMS_ITEMSTACK("net.minecraft.server.", ".ItemStack"),
    NMS_NBTTAGCOMPOUND("net.minecraft.server.", ".NBTTagCompound");

    private Class<?> clazz;

    ClassWrapper(String pre, String suffix) {
        try {
            String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
            this.clazz = Class.forName(pre + version + suffix);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Class<?> getClazz() {
        return this.clazz;
    }
}