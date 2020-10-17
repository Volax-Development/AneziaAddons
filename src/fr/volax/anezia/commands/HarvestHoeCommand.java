package fr.volax.anezia.commands;

import fr.volax.anezia.AneziaAddons;
import fr.volax.anezia.utils.CustomConfig;
import fr.volax.anezia.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class HarvestHoeCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("hh")) {
            if (args.length == 0 && sender.hasPermission("harvesthoe.help")) {
                sender.sendMessage("§7§m-------------------§aHarvesHoe§7§m-------------------");
                sender.sendMessage("§6- §7/hh give <player> §e<-- Give the harvest hoe to a player");
                sender.sendMessage("§7§m---------------------------------------------");
                return true;
            }
            if (args.length == 0 && !sender.hasPermission("harvesthoe.help")) {
                sender.sendMessage("§cVous n'avez pas la permission d'executer cette commande !");
                return true;
            }

            if (args[0].equalsIgnoreCase("give")) {
                if (sender.hasPermission("harvesthoe.give")) {
                    if (args.length >= 2) {
                        if (Bukkit.getPlayerExact(args[1]) == null) {
                            sender.sendMessage("§cPlayer not found");
                            return true;
                        }
                        Bukkit.getPlayerExact(args[1]).getInventory().addItem(new ItemBuilder(Material.DIAMOND_HOE, 1).setName("§c§l✸ §6§lHoue De Farm §c§l✸").setLore("§7§m------------------------------------", "", "§f§l» §eCette houe replante et duplique", "§evos plantations plus rapidement !", "" ,"§f§l» §e Utilisation §f: §7Infinie", "","§7§m------------------------------------").addItemFlag(ItemFlag.HIDE_ATTRIBUTES).addEnchant(Enchantment.LUCK, 3).toItemStack());
                    } else sender.sendMessage("§7Type §c/hh give <player> §7to give HarvestHoe !");
                } else sender.sendMessage("§cNo permission for that :(");
            } else sender.sendMessage("§7Type §c/hh §7to get HarvestHoe plugin's help");
        }
        return true;
    }
}
