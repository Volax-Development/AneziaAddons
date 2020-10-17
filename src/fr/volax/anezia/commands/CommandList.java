package fr.volax.anezia.commands;

import fr.volax.anezia.AneziaAddons;
import fr.volax.anezia.crafts.HammerCraft;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandList implements CommandExecutor {
    private AneziaAddons main;

    public CommandList(AneziaAddons main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("bhammer"))
                if (args.length == 0) {
                    player.sendMessage("§e§m-----§6BestHammer§e-----");
                    player.sendMessage("§7/bhammer §6give §7- Give you the §chammer");
                    player.sendMessage("§e§m------------------------");
                    return false;
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("give")) {
                        player.getInventory().addItem(HammerCraft.getHammer());
                        player.updateInventory();
                    }else{
                        player.sendMessage("§e§m-----§6BestHammer§e-----");
                        player.sendMessage("§7/bhammer §6give §7- Give you the §chammer");
                        player.sendMessage("§e§m------------------------");
                    }
                    return false;
                }else{
                    player.sendMessage("§e§m-----§6BestHammer§e-----");
                    player.sendMessage("§7/bhammer §6give §7- Give you the §chammer");
                    player.sendMessage("§e§m------------------------");
                    return false;
                }
        }
        return false;
    }
}