package fr.volax.anezia.utils;

import org.bukkit.inventory.ItemStack;

public class ValidItem {
    private boolean b;

    public ValidItem(ItemStack item) {
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            this.b = false;
            return;
        }
        this.b = true;
    }

    public boolean b() {
        return this.b;
    }
}