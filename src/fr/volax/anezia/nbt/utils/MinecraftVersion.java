package fr.volax.anezia.nbt.utils;

import org.bukkit.Bukkit;

public enum MinecraftVersion {
    Unknown(2147483647),
    MC1_7_R4(174),
    MC1_8_R3(183),
    MC1_9_R1(191),
    MC1_9_R2(192),
    MC1_10_R1(1101),
    MC1_11_R1(1111),
    MC1_12_R1(1121),
    MC1_13_R1(1131),
    MC1_13_R2(1132),
    MC1_14_R1(1141);

    private static MinecraftVersion version;

    private final int versionId;

    MinecraftVersion(int versionId) {
        this.versionId = versionId;
    }

    public int getVersionId() {
        return this.versionId;
    }

    public static MinecraftVersion getVersion() {
        if (version != null)
            return version;
        String ver = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        System.out.println("[NBTAPI] Found Spigot: " + ver + "! Trying to find NMS support");
        try {
            version = valueOf(ver.replace("v", "MC"));
        } catch (IllegalArgumentException ex) {
            version = Unknown;
        }
        if (version != Unknown) {
            System.out.println("[NBTAPI] NMS support '" + version.name() + "' loaded!");
        } else {
            System.out.println("[NBTAPI] Wasn't able to find NMS Support! Some functions may not work!");
        }
        return version;
    }
}