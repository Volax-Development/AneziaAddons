package fr.volax.anezia.crafts;

import fr.volax.anezia.utils.GiveItems;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.MaterialData;

public class NewCrafts {
    private static NewCrafts instance = new NewCrafts();

    public static NewCrafts getInstance() {
        return instance;
    }

    public void crafts() {
        ItemStack water = (new GiveItems(1)).createCreeperWater();
        ShapedRecipe waterRecipe = new ShapedRecipe(water);
        waterRecipe.shape("ABA", "BCB", "ABA");
        waterRecipe.setIngredient('A', Material.EMERALD_BLOCK);
        waterRecipe.setIngredient('B', new MaterialData(Material.MONSTER_EGG, (byte) 50));
        waterRecipe.setIngredient('C', Material.WATER_BUCKET);
        ItemStack lava = (new GiveItems(1)).createCreeperLava();
        ShapedRecipe lavaRecipe = new ShapedRecipe(lava);
        lavaRecipe.shape("ABA", "BCB", "ABA");
        lavaRecipe.setIngredient('A', Material.EMERALD_BLOCK);
        lavaRecipe.setIngredient('B', new MaterialData(Material.MONSTER_EGG, (byte) 50));
        lavaRecipe.setIngredient('C', Material.LAVA_BUCKET);
        Bukkit.getServer().addRecipe((Recipe) waterRecipe);
        Bukkit.getServer().addRecipe((Recipe) lavaRecipe);
    }
}