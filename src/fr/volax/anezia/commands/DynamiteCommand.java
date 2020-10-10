package fr.volax.anezia.commands;

import fr.volax.anezia.utils.DynamiteItem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DynamiteCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        if (cmd.getName().equalsIgnoreCase("givedynamite")) {
            if (args.length == 0) {
                sender.sendMessage("§7/givedynamite <player> <amount>");
                return true;
            }
            if (args.length == 2) {
                if (Bukkit.getPlayer(args[0]) != null &&
                        isInt(args[1])) {
                    Player target = Bukkit.getPlayer(args[0]);
                    ItemStack item = (new DynamiteItem()).item();
                    item.setAmount(Integer.parseInt(args[1]));
                    target.getInventory().addItem(item);
                    sender.sendMessage("§aTu as donné §2" + args[1] + " dynamite à §2" + target.getName() + "§a.");
                    return true;
                }
                sender.sendMessage("§7/givedynamite <player> <amount");
                return true;
            }
            return true;
        }
        return false;
    }

    private boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}