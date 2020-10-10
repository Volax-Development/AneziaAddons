package fr.volax.anezia.utils;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OrbeItem {
    private final ItemStack item;

    public OrbeItem(OrbeType type, int i) {
        ItemStack it = null;
        if (type == OrbeType.SPEED) {
            it = new ItemStack(Material.SUGAR);
            ItemMeta meta = it.getItemMeta();
            meta.setDisplayName("§7Orbe de §bVitesse");
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setLore(Arrays.asList("§7Cette orbe vous permet d'avoir", "§bVitesse§7 tant que",
                    "§7vous l'avez dans votre inventaire."));
            it.setItemMeta(meta);
            it.setAmount(i);
            this.item = it;
            return;
        }
        if (type == OrbeType.FORCE) {
            it = new ItemStack(Material.BLAZE_POWDER);
            ItemMeta meta = it.getItemMeta();
            meta.setDisplayName("§7Orbe de §cForce");
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setLore(Arrays.asList("§7Cette orbe vous permet d'avoir", "§cForce§7 tant que",
                    "§7vous l'avez dans votre inventaire."));
            it.setItemMeta(meta);
            it.setAmount(i);
            this.item = it;
            return;
        }
        if (type == OrbeType.FIRE) {
            it = new ItemStack(Material.MAGMA_CREAM);
            ItemMeta meta = it.getItemMeta();
            meta.setDisplayName("§7Orbe de §6Fire résistance");
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setLore(Arrays.asList("§7Cette orbe vous permet d'avoir", "§6Fire résistance§7 tant que",
                    "§7vous l'avez dans votre inventaire."));
            it.setItemMeta(meta);
            it.setAmount(i);
            this.item = it;
            return;
        }
        if (type == OrbeType.FALL) {
            it = new ItemStack(Material.SLIME_BALL);
            ItemMeta meta = it.getItemMeta();
            meta.setDisplayName("§7Orbe de §aFall");
            meta.addEnchant(Enchantment.DURABILITY, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setLore(Arrays.asList("§7Cette orbe vous permet de ne pas ", "§7avoir de §adégat de chute §7tant que",
                    "§7vous l'avez dans votre inventaire."));
            it.setItemMeta(meta);
            it.setAmount(i);
            this.item = it;
            return;
        }
        this.item = it;
    }

    public ItemStack item() {
        return this.item;
    }
}