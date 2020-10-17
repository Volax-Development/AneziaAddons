package fr.volax.anezia.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class OrbeVerifier {
    private boolean b = false;

    public OrbeVerifier(Player player, OrbeType type) {
        Material material;
        String name;
        switch (type) {
            case SPEED:
                material = Material.SUGAR;
                name = "§c§l✸ §6§lOrbe de Vitesse§c§l✸";
                break;
            case FORCE:
                material = Material.BLAZE_POWDER;
                name = "§c§l✸ §6§lOrbe de Force§c§l✸";
                break;
            case FIRE:
                material = Material.MAGMA_CREAM;
                name = "§c§l✸ §6§lOrbe de Résistance§c§l✸";
                break;
            default:
                material = Material.SLIME_BALL;
                name = "§c§l✸ §6§lOrbe de Fall§c§l✸";
                break;
        }
        byte b;
        int i;
        ItemStack[] arrayOfItemStack;
        for (i = (arrayOfItemStack = player.getInventory().getContents()).length, b = 0; b < i; ) {
            ItemStack itemStack = arrayOfItemStack[b];
            if (itemStack != null && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().hasLore() &&
                    itemStack.getType() == material && itemStack.getItemMeta().getDisplayName().equals(name))
                this.b = true;
            b++;
        }
    }

    public boolean b() {
        return this.b;
    }
}