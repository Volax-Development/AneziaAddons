package fr.volax.anezia.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class FinderCommand implements CommandExecutor {
    private ItemStack item;

    public FinderCommand(ItemStack item) {
        this.item = item;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        if (args.length == 1 && Bukkit.getPlayer(args[0]) != null) {
            Bukkit.getPlayer(args[0]).getInventory().addItem(this.item);
            sender.sendMessage("§fTu as donné un unclaim finder à §a" + args[0] + "§f.");
            return true;
        }
        sender.sendMessage("§fUsage /giveunclaimfinder <player>");
        return true;
    }
}