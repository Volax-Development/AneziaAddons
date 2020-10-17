package fr.volax.anezia.commands;

import fr.volax.anezia.utils.OrbeItem;
import fr.volax.anezia.utils.OrbeType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class OrbeCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
        if (cmd.getName().equalsIgnoreCase("orbespeed")) {
            if (args.length != 2) sender.sendMessage("§6Anézia §f» §e/orbespeed <player> <amount>");
            else {
                if (Bukkit.getPlayer(args[0]) != null && isInt(args[1])) {
                    Bukkit.getPlayer(args[0]).getInventory()
                            .addItem((new OrbeItem(OrbeType.SPEED, Integer.parseInt(args[1]))).item());
                    return true;
                }
            }
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("orberesi")) {
            if (args.length != 2) sender.sendMessage("§6Anézia §f» §e/orberesi <player> <amount>");
            else {
                if (Bukkit.getPlayer(args[0]) != null && isInt(args[1])) {
                    Bukkit.getPlayer(args[0]).getInventory()
                            .addItem((new OrbeItem(OrbeType.FIRE, Integer.parseInt(args[1]))).item());
                    return true;
                }
            }
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("orbeforce")) {
            if (args.length != 2) sender.sendMessage("§6Anézia §f» §e/orbeforce <player> <amount>");
            else {
                if (Bukkit.getPlayer(args[0]) != null && isInt(args[1])) {
                    Bukkit.getPlayer(args[0]).getInventory()
                            .addItem((new OrbeItem(OrbeType.FORCE, Integer.parseInt(args[1]))).item());
                    return true;
                }
            }
            return true;
        }
        if (cmd.getName().equalsIgnoreCase("orbefall")) {
            if (args.length != 2) sender.sendMessage("§6Anézia §f» §e/orbefall <player> <amount>");
            else {
                if (Bukkit.getPlayer(args[0]) != null && isInt(args[1])) {
                    Bukkit.getPlayer(args[0]).getInventory()
                            .addItem((new OrbeItem(OrbeType.FALL, Integer.parseInt(args[1]))).item());
                    return true;
                }
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