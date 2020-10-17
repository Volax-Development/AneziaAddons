package fr.volax.anezia.events;

import fr.volax.anezia.AneziaAddons;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

public class ChestViewerListeners implements Listener {
    private AneziaAddons main;

    public ChestViewerListeners(AneziaAddons aneziaAddons) {
        this.main = aneziaAddons;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Chest c;
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.CHEST) c = (Chest)e.getClickedBlock().getState();
        else if (e.getAction() == Action.RIGHT_CLICK_AIR && e.getPlayer().getTargetBlock((HashSet<Byte>) null, 100).getType() == Material.CHEST) c = (Chest)e.getPlayer().getTargetBlock((HashSet<Byte>) null, 100).getState();
        else return;
        if (!(e.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) && e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("§c§l✸ §6§lChestViewer §c§l✸")) {
            Player p = e.getPlayer();
            Inventory inv = Bukkit.createInventory(e.getPlayer(), 54, this.main.getConfig().getString("chest-name"));
            byte b;
            int i;
            ItemStack[] arrayOfItemStack;
            for (i = (arrayOfItemStack = c.getInventory().getContents()).length, b = 0; b < i; ) {
                ItemStack itemStack = arrayOfItemStack[b];
                if (itemStack != null && !this.main.getConfig().getStringList("blacklisted-items").contains(itemStack.getType().name()))
                    if (this.main.getConfig().getBoolean("show-amount")) inv.addItem(itemStack);
                    else inv.addItem(new ItemStack(itemStack.getType(), 1));
                b++;
            }
            p.openInventory(inv);
            if (this.main.getConfig().getBoolean("remove-item")) p.getInventory().setItemInHand(null);
        }

    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (e.getClickedInventory().getHolder() == e.getWhoClicked() && e.getClickedInventory().getSize() == 54 && e.getClickedInventory().getViewers().size() == 1) e.setCancelled(true);
    }
}
