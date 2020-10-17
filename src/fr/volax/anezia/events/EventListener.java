package fr.volax.anezia.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.volax.anezia.AneziaAddons;
import fr.volax.anezia.crafts.HammerCraft;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class EventListener implements Listener {
  private AneziaAddons main;
  
  public static int counter = 0;
  
  public EventListener(AneziaAddons main) {
    this.main = main;
  }
  
  @EventHandler
  public void onMine(BlockBreakEvent event) {
    Player player = event.getPlayer();
    Block block = event.getBlock();
    int[] blockallowed = { 1, 4, 12, 13, 14, 15, 16, 21, 56, 73, 74, 129 };
    if (player.getItemInHand().getType() == Material.GOLD_PICKAXE &&
      player.getItemInHand().getItemMeta().hasDisplayName() && 
      player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§c§l✸ §6§lHammer 3x3 §c§l✸")) {
      byte b;
      int i;
      int[] arrayOfInt;
      for (i = (arrayOfInt = blockallowed).length, b = 0; b < i; ) {
        int j = arrayOfInt[b];
        World world = player.getWorld();
        for (int x = -1; x < 2; x++) {
          for (int y = -1; y < 2; y++) {
            for (int z = -1; z < 2; z++) {
              Block blockAtLoc = world.getBlockAt(block.getLocation().getBlockX() + x, 
                  block.getLocation().getBlockY() + y, block.getLocation().getBlockZ() + z);
              if (blockAtLoc.getTypeId() == j)
                blockAtLoc.breakNaturally(new ItemStack(Material.DIAMOND_PICKAXE)); 
            } 
          } 
        } 
        b++;
      } 
    } 
  }
  
  public void removeRecipe() {
    Iterator<Recipe> it = this.main.getServer().recipeIterator();
    while (it.hasNext()) {
      Recipe recipe = it.next();
      if (recipe != null && recipe.getResult().getType() == Material.GOLD_PICKAXE && 
        recipe.getResult().getItemMeta().hasDisplayName() && 
        recipe.getResult().getItemMeta().getDisplayName().equalsIgnoreCase(this.main.getConfig().getString("hammer-name")))
        it.remove(); 
    } 
  }
  
  @EventHandler
  public void getContent(InventoryCloseEvent event) {
    Player player = (Player)event.getPlayer();
    Inventory inv = event.getInventory();
    ShapedRecipe recette = new ShapedRecipe(HammerCraft.getHammer());
    String[] etage = { "", "", "" };
    String[] alone = { "BKT", "AJS", "CLU" };
    ArrayList<Integer> noair = new ArrayList<>();
    if (inv.getName().equalsIgnoreCase("§cSet Hammer Craft")) {
      int i;
      for (i = 0; i < 27; i++) {
        if (inv.getItem(i) == null) {
          counter++;
          if (counter == 9) {
            player.sendMessage("§7[§cBestHammer§7] §6Craft forbidden !");
            counter = 0;
            return;
          } 
        } else if (inv.getItem(i).getType() != Material.RECORD_9) {
          char c = (char)(i + 62);
          if (i < 9) {
            etage[0] = etage[0] + c;
          } else if (i < 18) {
            etage[1] = etage[1] + c;
          } else {
            etage[2] = etage[2] + c;
          } 
          noair.add(i);
        } 
      } 
      removeRecipe();
      for (i = 0; i < 3; i++) {
        if (etage[i].length() < 3)
          for (int j = 0; j < 3; j++) {
            if (etage[i].equalsIgnoreCase("")) {
              etage[i] = "   ";
            } else if (etage[i].equalsIgnoreCase(String.valueOf(alone[0].charAt(j)))) {
              etage[i] = " " + etage[i] + " ";
            } else if (etage[i].equalsIgnoreCase(String.valueOf(alone[1].charAt(j)))) {
              etage[i] = etage[i] + "  ";
            } else if (etage[i].equalsIgnoreCase(String.valueOf(alone[2].charAt(j)))) {
              etage[i] = "  " + etage[i];
            } else if (etage[i].equalsIgnoreCase(String.valueOf(alone[1].charAt(j) + alone[0].charAt(j)))) {
              etage[i] = etage[i] + " ";
            } else if (etage[i].equalsIgnoreCase(String.valueOf(alone[1].charAt(j) + alone[2].charAt(j)))) {
              etage[i] = alone[1].charAt(j) + " " + alone[2].charAt(j);
            } else if (etage[i].equalsIgnoreCase(String.valueOf(alone[0].charAt(j) + alone[2].charAt(j)))) {
              etage[i] = " " + etage[i];
            } 
          }  
      } 
      List<String> list = this.main.getConfig().getStringList("key");
      list.clear();
      for (Iterator<Integer> iterator = noair.iterator(); iterator.hasNext(); ) {
        int e = iterator.next();
        char c = (char)(e + 62);
        recette.shape(etage[0], etage[1], etage[2]);
        recette.setIngredient(c, inv.getItem(e).getType(), inv.getItem(e).getData().getData());
        list.add(c + "/" + inv.getItem(e).getType().name() + "/" + inv.getItem(e).getData().getData());
        this.main.getConfig().set("key", list);
        this.main.saveConfig();
      } 
      this.main.getConfig().set("craft.etage0", etage[0]);
      this.main.getConfig().set("craft.etage1", etage[1]);
      this.main.getConfig().set("craft.etage2", etage[2]);
      this.main.saveConfig();
      Bukkit.addRecipe(recette);
    } 
  }
  
  @EventHandler
  public void onClick(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    Inventory inv = event.getInventory();
    ItemStack current = event.getCurrentItem();
    if (inv.getName().equalsIgnoreCase("§cSet Hammer Craft"))
      if (current.getType() == Material.RECORD_9 && current.getItemMeta().hasDisplayName() && 
        current.getItemMeta().getDisplayName().equalsIgnoreCase("§cDont Touch")) {
        player.closeInventory();
        event.setCancelled(true);
      }  
  }
}