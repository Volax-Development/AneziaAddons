package fr.volax.anezia.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class OrbeVerifier {
    private boolean b = false;

    public OrbeVerifier(Player player, OrbeType type) {
        Material material = Material.SUGAR;
        String name = "§cError";
        switch (type) {
            case SPEED:
                material = Material.SUGAR;
                name = "§7Orbe de §bVitesse";
                break;
            case FORCE:
                material = Material.BLAZE_POWDER;
                name = "§7Orbe de §cForce";
                break;
            case FIRE:
                material = Material.MAGMA_CREAM;
                name = "§7Orbe de §6Fire résistance";
                break;
            default:
                material = Material.SLIME_BALL;
                name = "§7Orbe de §aFall";
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