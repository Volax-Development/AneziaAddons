package fr.volax.anezia.crafts;

import fr.volax.anezia.AneziaAddons;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class HammerCraft {
    public static void setOldestCraft() {
        ShapedRecipe recette = new ShapedRecipe(getHammer());
        recette.shape(AneziaAddons.getInstance().getConfig().getString("craft.etage0"), AneziaAddons.getInstance().getConfig().getString("craft.etage1"), AneziaAddons.getInstance().getConfig().getString("craft.etage2"));
        for (String s : AneziaAddons.getInstance().getConfig().getStringList("key")) {
            String[] poir = s.split("/");
            recette.setIngredient(poir[0].charAt(0), Material.getMaterial(poir[1]), Integer.parseInt(poir[2]));
        }
        Bukkit.addRecipe(recette);
    }

    public static ItemStack getHammer() {
        ItemStack hammer = new ItemStack(Material.GOLD_PICKAXE);
        ItemMeta metahammer = hammer.getItemMeta();
        metahammer.setDisplayName(AneziaAddons.getInstance().getConfig().getString("hammer-name"));
        metahammer.setLore(Collections.singletonList(AneziaAddons.getInstance().getConfig().getString("lore")));
        metahammer.addEnchant(Enchantment.DIG_SPEED, 1, true);
        metahammer.addEnchant(Enchantment.DURABILITY, 6, true);
        hammer.setItemMeta(metahammer);
        return hammer;
    }
}
