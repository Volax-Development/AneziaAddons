package fr.volax.anezia.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fr.volax.anezia.AneziaAddons;
import fr.volax.anezia.utils.ConfigValues;
import fr.volax.anezia.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChunkBusterCommand implements TabExecutor {
    private AneziaAddons main;

    public ChunkBusterCommand(AneziaAddons main) {
        this.main = main;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>(Arrays.asList("give", "reload", "water"));
            for (String arg : Arrays.asList("give", "reload", "water")) {
                if (!arg.startsWith(args[0].toLowerCase()))
                    arguments.remove(arg);
            }
            return arguments;
        }
        return null;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "give":
                    if (sender.hasPermission("chunkbuster.give") || sender.hasPermission("chunkbuster.admin")) {
                        if (args.length > 1) {
                            Player p = Bukkit.getPlayerExact(args[1]);
                            if (p != null) {
                                if (args.length > 2) {
                                    int chunkArea;
                                    try {
                                        chunkArea = Integer.parseInt(args[2]);
                                    } catch (NumberFormatException ex) {
                                        sender.sendMessage("§6Anézia §f» §eThis isn't a valid number!");
                                        return false;
                                    }
                                    if (chunkArea > 0 && chunkArea % 2 != 0) {
                                        int giveAmount = 1;
                                        if (args.length > 3)
                                            try {
                                                giveAmount = Integer.parseInt(args[3]);
                                            } catch (NumberFormatException ex) {
                                                sender.sendMessage("§6Anézia §f» §eThis isn't a valid number!");
                                                return false;
                                            }
                                        ItemStack item = this.main.getUtils().getChunkBusterItem(giveAmount, chunkArea);
                                        if (!this.main.getConfigValues().dropFullInv())
                                            if (giveAmount < 65) {
                                                if (p.getInventory().firstEmpty() == -1) {
                                                    sender.sendMessage("§6Anézia §f» §eThis player doesn't have any empty slots in their inventory!");
                                                    return true;
                                                }
                                            } else {
                                                sender.sendMessage("§6Anézia §f» §eYou can only give 64 at a time!");
                                                return true;
                                            }
                                        Map<Integer, ItemStack> excessItems = p.getInventory().addItem(item);
                                        for (ItemStack excessItem : excessItems.values()) {
                                            int itemCount = excessItem.getAmount();
                                            while (itemCount > 64) {
                                                excessItem.setAmount(64);
                                                p.getWorld().dropItemNaturally(p.getLocation(), excessItem);
                                                itemCount -= 64;
                                            }
                                            if (itemCount > 0) {
                                                excessItem.setAmount(itemCount);
                                                p.getWorld().dropItemNaturally(p.getLocation(), excessItem);
                                            }
                                        }
                                        this.main.getUtils().sendMessage(p, ConfigValues.Message.GIVE, p.getName(), giveAmount);
                                        this.main.getUtils().sendMessage(p, ConfigValues.Message.RECEIVE, giveAmount);
                                    } else sender.sendMessage("§6Anézia §f» §eThe area must be greater than 0 and be an odd number!");
                                } else sender.sendMessage("§6Anézia §f» §ePlease specify the chunk area!");
                            } else sender.sendMessage("§6Anézia §f» §eThis player is not online!");
                        } else sender.sendMessage("§6Anézia §f» §ePlease specify a player!");
                    } else this.main.getUtils().sendMessage(sender, ConfigValues.Message.NO_PERMISSION_COMMAND);
                    return false;
                case "reload":
                    if (sender.hasPermission("chunkbuster.reload") || sender.hasPermission("chunkbuster.admin")) {
                        this.main.reloadConfig();
                        sender.sendMessage("§6Anézia §f» §eSuccessfully reloaded the config. Most values have been instantly updated.");
                    } else this.main.getUtils().sendMessage(sender, ConfigValues.Message.NO_PERMISSION_COMMAND);
                    return false;
                case "water":
                    if (sender.hasPermission("chunkbuster.water") || sender.hasPermission("chunkbuster.admin")) {
                        if (sender instanceof Player) {
                            if (this.main.getUtils().getWaterChunks().contains(((Player) sender).getLocation().getChunk())) {
                                this.main.getUtils().getWaterChunks().remove(((Player) sender).getLocation().getChunk());
                                sender.sendMessage("§6Anézia §f» §eWater can now flow normally into this chunk.");
                            } else sender.sendMessage("§6Anézia §f» §eThis chunk doesn't have water flow disabled!");
                        } else sender.sendMessage("§6Anézia §f» §eYou cannot use this command from here!");
                    } else this.main.getUtils().sendMessage(sender, ConfigValues.Message.NO_PERMISSION_COMMAND);
                    return false;
            }
            sender.sendMessage("§6Anézia §f» §eInvalid argument!");
        } else {
            sender.sendMessage(Utils.color("&7&m--------------&7[&6&l Anézia ChunkBuster &7]&7&m--------------"));
            sender.sendMessage(Utils.color("&6● /cb give <player> <chunk-area> [amount] &7- Give a player a chunk buster"));
            sender.sendMessage(Utils.color("&6● /cb reload &7- Reload the config"));
            sender.sendMessage(Utils.color("&6● /cb water &7- &e[DEBUG] &7 Allow water to flow normally in your current chunk"));
            sender.sendMessage(Utils.color("&7&m------------------------------------"));
        }
        return false;
    }
}