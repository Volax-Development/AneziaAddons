package fr.volax.anezia.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveItems {
    private Player player;

    private int amount;

    public GiveItems(Player player) {
        this.player = player;
    }

    public GiveItems(int amount) {
        this.amount = amount;
    }

    public ItemStack createCreeperWater() {
        ItemStack item = (new ItemBuilder(Material.MONSTER_EGG, this.amount, (byte) 50)).setName("§c§l✸ §6§lCreeper d'Eau §c§l✸").addEnchant(Enchantment.DURABILITY, 1).setLore("§7Grâce à ce nouveau creeper, vous allez pouvoir", "§7le faire exploser directement dans l'eau.").toItemStack();
        ItemMeta itemM = item.getItemMeta();
        itemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(itemM);
        return item;
    }

    public ItemStack createCreeperLava() {
        ItemStack item = (new ItemBuilder(Material.MONSTER_EGG, this.amount, (byte) 50)).setName("§d✿ §6Creeper de §nlave").addEnchant(Enchantment.DURABILITY, 1).setLore("§7Grâce à ce nouveau creeper, vous allez pouvoir", "§7le faire exploser directement dans la lave.").toItemStack();
        ItemMeta itemM = item.getItemMeta();
        itemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(itemM);
        return item;
    }

    public boolean checkSlot() {
        PlayerInventory playerInventory = this.player.getInventory();
        for (int i = 0; i < (playerInventory.getContents()).length; i++) {
            if (playerInventory.getItem(i) == null)
                return true;
        }
        return false;
    }
}