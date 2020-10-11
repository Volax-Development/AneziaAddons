package fr.volax.anezia.commands;

import java.util.ArrayList;
import java.util.List;

import fr.volax.anezia.AneziaAddons;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SellWandCmd implements CommandExecutor {
    AneziaAddons plugin;

    public SellWandCmd(AneziaAddons plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sellwand")) {
            if (!sender.hasPermission("SELLWAND.GIVE") && !sender.isOp()) {
                sender.sendMessage(this.plugin.getConfig().getString("Messages.NoPermission").replaceAll("(&([a-f0-9]))", "§$2")
                        .replaceAll("&l", "§l").replaceAll("&o", "§o").replaceAll("&k", "§k").replaceAll("&r", "§r")
                        .replaceAll("&n", "§n").replaceAll("&m", "§m"));
                return false;
            }
            if (args.length != 1) {
                sender.sendMessage(this.plugin.getConfig().getString("Messages.CorrectUsage").replaceAll("(&([a-f0-9]))", "§$2")
                        .replaceAll("&l", "§l").replaceAll("&o", "§o").replaceAll("&k", "§k").replaceAll("&r", "§r")
                        .replaceAll("&n", "§n").replaceAll("&m", "§m"));
                return false;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage(this.plugin.getConfig().getString("Messages.PlayerNotFound").replaceAll("(&([a-f0-9]))", "§$2")
                        .replaceAll("&l", "§l").replaceAll("&o", "§o").replaceAll("&k", "§k").replaceAll("&r", "§r")
                        .replaceAll("&n", "§n").replaceAll("&m", "§m").replaceAll("%player%", args[0]));
                return false;
            }
            Player target = Bukkit.getPlayer(args[0]);
            target.getInventory().addItem(getSellWand());
            target.sendMessage(this.plugin.getConfig().getString("Messages.ItemReceived").replaceAll("(&([a-f0-9]))", "§$2")
                    .replaceAll("&l", "§l").replaceAll("&o", "§o").replaceAll("&k", "§k").replaceAll("&r", "§r")
                    .replaceAll("&n", "§n").replaceAll("&m", "§m"));
            sender.sendMessage(this.plugin.getConfig().getString("Messages.ItemGiven").replaceAll("(&([a-f0-9]))", "§$2").replaceAll("&l", "§l")
                    .replaceAll("&o", "§o").replaceAll("&k", "§k").replaceAll("&r", "§r").replaceAll("&n", "§n")
                    .replaceAll("&m", "§m").replaceAll("%player%", args[0]));
        }
        return false;
    }

    public ItemStack getSellWand() {
        ItemStack sellWand = new ItemStack(Material.getMaterial(this.plugin.getConfig().getString("sellwand.type")));
        ItemMeta sellWandMeta = sellWand.getItemMeta();
        sellWandMeta.setDisplayName(this.plugin.getConfig().getString("sellwand.name").replaceAll("(&([a-f0-9]))", "§$2").replaceAll("&l", "§l")
                .replaceAll("&o", "§o").replaceAll("&k", "§k").replaceAll("&r", "§r").replaceAll("&n", "§n")
                .replaceAll("&m", "§m"));
        List<String> lores = this.plugin.getConfig().getStringList("sellwand.lore");
        List<String> replacedLores = new ArrayList<>();
        for (String lore : lores)
            replacedLores.add(lore.replaceAll("(&([a-f0-9]))", "§$2").replaceAll("&l", "§l").replaceAll("&o", "§o")
                    .replaceAll("&k", "§k").replaceAll("&r", "§r").replaceAll("&n", "§n").replaceAll("&m", "§m"));
        sellWandMeta.setLore(replacedLores);
        sellWand.setItemMeta(sellWandMeta);
        return sellWand;
    }
}