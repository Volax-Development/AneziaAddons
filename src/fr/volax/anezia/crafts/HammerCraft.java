package fr.volax.anezia.crafts;

import fr.volax.anezia.AneziaAddons;
import fr.volax.anezia.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

public class HammerCraft {
    public static void setOldestCraft() {
        ShapedRecipe recette = new ShapedRecipe(getHammer()).shape("GGG", " S ", " S ");
        recette.setIngredient('G', Material.EMERALD_BLOCK, 0);
        recette.setIngredient('S', Material.STICK, 0);
        Bukkit.addRecipe(recette);
    }

    public static ItemStack getHammer() {
        return new ItemBuilder(Material.GOLD_PICKAXE, 1).setName("§c§l✸ §6§lHammer 3x3 §c§l✸").setLore("§7§m---------------------------------","§f§l» §eCe hammer vous permez de casser les blocks en 3x3","§7§m---------------------------------").addEnchant(Enchantment.DIG_SPEED, 1).addEnchant(Enchantment.DURABILITY, 6).addItemFlag(ItemFlag.HIDE_ENCHANTS).toItemStack();
    }
}
