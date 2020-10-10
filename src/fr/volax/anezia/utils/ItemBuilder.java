package fr.volax.anezia.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;

public class ItemBuilder implements Cloneable {
    private ItemStack item;

    public ItemBuilder(Material material, int amount, short anInt) {
        this(material, 1);
    }

    public ItemBuilder(ItemStack item) {
        this.item = item;
    }

    public ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
    }

    public ItemBuilder(Material material, int amount, byte data) {
        this.item = new ItemStack(material, amount, data);
    }

    public ItemBuilder(Potion potion, int amount) {
        this.item = potion.toItemStack(amount);
    }

    public ItemBuilder clone() {
        return new ItemBuilder(this.item);
    }

    public ItemBuilder setDurability(short durability) {
        this.item.setDurability(durability);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.setDisplayName(name);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
        this.item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        this.item.removeEnchantment(enchantment);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        try {
            SkullMeta skullMeta = (SkullMeta) this.item.getItemMeta();
            skullMeta.setOwner(owner);
            this.item.setItemMeta((ItemMeta) skullMeta);
        } catch (ClassCastException localClassCastException) {
            localClassCastException.printStackTrace();
        }
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addEnchant(enchantment, level, true);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        this.item.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder setInfinityDurability() {
        this.item.setDurability((short) 0);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.setLore(Arrays.asList(lore));
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.setLore(lore);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeLoreLine(String line) {
        ItemMeta itemMeta = this.item.getItemMeta();
        List<String> lore = new ArrayList<String>(itemMeta.getLore());
        if (!lore.contains(line))
            return this;
        lore.remove(line);
        itemMeta.setLore(lore);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeLoreLine(int index) {
        ItemMeta itemMeta = this.item.getItemMeta();
        List<String> lore = new ArrayList<String>(itemMeta.getLore());
        if (index < 0 || index > lore.size())
            return this;
        lore.remove(index);
        itemMeta.setLore(lore);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        ItemMeta itemMeta = this.item.getItemMeta();
        List<String> lore = new ArrayList<String>();
        if (itemMeta.hasLore())
            lore = new ArrayList<String>(itemMeta.getLore());
        lore.add(line);
        itemMeta.setLore(lore);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addLoreLine(String line, int pos) {
        ItemMeta itemMeta = this.item.getItemMeta();
        List<String> lore = new ArrayList<String>(itemMeta.getLore());
        lore.set(pos, line);
        itemMeta.setLore(lore);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setDyeColor(DyeColor color) {
        this.item.setDurability(color.getWoolData());
        return this;
    }

    @Deprecated
    public ItemBuilder setWoolColor(DyeColor color) {
        if (!this.item.getType().equals(Material.WOOL))
            return this;
        this.item.setDurability(color.getWoolData());
        return this;
    }

    public ItemBuilder setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta itemMeta = (LeatherArmorMeta) this.item.getItemMeta();
            itemMeta.setColor(color);
            this.item.setItemMeta((ItemMeta) itemMeta);
        } catch (ClassCastException localClassCastException) {
            localClassCastException.printStackTrace();
        }
        return this;
    }

    public ItemStack toItemStack() {
        return this.item;
    }
}