package fr.volax.anezia.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class DynamiteItem {
    private ItemStack dynamite;

    public DynamiteItem() {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwner("MHF_TNT");
        skullMeta.setDisplayName("§d✿ §6Dynamite");
        skullMeta.addEnchant(Enchantment.DURABILITY, 1, true);
        skullMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        skull.setItemMeta( skullMeta);
        this.dynamite = skull;
    }

    public String name() {
        return this.dynamite.getItemMeta().getDisplayName();
    }

    public Material type() {
        return this.dynamite.getType();
    }

    public ItemStack item() {
        return this.dynamite;
    }
}