package fr.volax.anezia.events;

import com.massivecraft.factions.*;
import com.massivecraft.factions.integration.Econ;
import java.util.ArrayList;
import java.util.HashMap;

import fr.volax.anezia.AneziaAddons;
import fr.volax.anezia.commands.SellWandCmd;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SellWandListener implements Listener {
    AneziaAddons plugin;

    public SellWandListener(AneziaAddons plugin) {
        this.plugin = plugin;
    }

    public static HashMap<Material, Integer> items = new HashMap<>();

    public void setupItems() {
        items.clear();
        for (String item : this.plugin.getConfig().getConfigurationSection("items").getKeys(false)) {
            int amount = this.plugin.getConfig().getInt("items." + item);
            items.put(Material.getMaterial(Integer.parseInt(item)), amount);
        }
    }

    @EventHandler
    public void PlayerInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        if (e.getClickedBlock().getType() != Material.CHEST && e.getClickedBlock().getType() != Material.TRAPPED_CHEST) return;
        if (!e.getPlayer().getItemInHand().isSimilar((new SellWandCmd(this.plugin)).getSellWand())) return;
        Faction faction = Board.getInstance().getFactionAt(new FLocation(e.getClickedBlock()));
        FPlayer fp = FPlayers.getInstance().getByPlayer(e.getPlayer());
        Faction fac = fp.getFaction();
        if (faction != null && !faction.getId().equalsIgnoreCase("0") && !faction.getId().equalsIgnoreCase("none") &&
                !faction.getId().equalsIgnoreCase("safezone") && !faction.getId().equalsIgnoreCase("warzone") && !faction.equals(fac)) {
            e.getPlayer().sendMessage(
                    this.plugin.getConfig().getString("Messages.Territory").replaceAll("(&([a-f0-9]))", "§$2").replaceAll("&l", "§l")
                            .replaceAll("&o", "§o").replaceAll("&k", "§k").replaceAll("&r", "§r").replaceAll("&n", "§n")
                            .replaceAll("&m", "§m"));
            return;
        }
        e.setCancelled(true);
        Chest chestBlock = (Chest) e.getClickedBlock().getState();
        Inventory chest = chestBlock.getInventory();
        if (isEmpty(chest)) {
            e.getPlayer().sendMessage(
                    this.plugin.getConfig().getString("Messages.Empty").replaceAll("(&([a-f0-9]))", "§$2").replaceAll("&l", "§l")
                            .replaceAll("&o", "§o").replaceAll("&k", "§k").replaceAll("&r", "§r").replaceAll("&n", "§n")
                            .replaceAll("&m", "§m"));
            return;
        }
        ArrayList<ItemStack> removing = new ArrayList<>();
        double finalValue = 0.0D;
        byte b;
        int i;
        ItemStack[] arrayOfItemStack;
        for (i = (arrayOfItemStack = chest.getContents()).length, b = 0; b < i; ) {
            ItemStack item = arrayOfItemStack[b];
            if (SellWandListener.items != null && item != null &&
                    SellWandListener.items.containsKey(item.getType())) {
                double sellprice = SellWandListener.items.get(item.getType());
                if (sellprice > 0.0D) {
                    finalValue += sellprice * item.getAmount();
                    removing.add(item);
                }
            }
            b++;
        }
        addAmount(finalValue, e.getPlayer());
        for (ItemStack items : removing)
            chest.remove(items);
        e.getClickedBlock().getState().update();
    }

    public boolean isEmpty(Inventory inv) {
        byte b;
        int i;
        ItemStack[] arrayOfItemStack;
        for (i = (arrayOfItemStack = inv.getContents()).length, b = 0; b < i; ) {
            ItemStack item = arrayOfItemStack[b];
            if (item != null)
                return false;
            b++;
        }
        return true;
    }

    public void addAmount(double amount, Player player) {
        Econ.setBalance(player.getName(), Econ.getBalance(player.getName()) + amount);
        player.sendMessage(this.plugin.getConfig().getString("Messages.AmountAdded").replaceAll("(&([a-f0-9]))", "§$2").replaceAll("&l", "§l")
                .replaceAll("&o", "§o").replaceAll("&k", "§k").replaceAll("&r", "§r").replaceAll("&n", "§n")
                .replaceAll("&m", "§m").replaceAll("%amount%", (new StringBuilder(String.valueOf((int) amount))).toString()));
    }
}