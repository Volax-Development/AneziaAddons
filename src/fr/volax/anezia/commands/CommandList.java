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
                    player.sendMessage("§7-----BestHammer-----");
                    player.sendMessage("§7/bhammer §6give §7- Give you the §chammer");
                    player.sendMessage("§7/bhammer §6craft §7- Change craft of the §chammer");
                    player.sendMessage("§7/bhammer §6name §7- Change name of the §chammer §4[/!\\]");
                    player.sendMessage("§7/bhammer §6desc §7- Change lore of the §chammer §4[/!\\]");
                    player.sendMessage("§7-----Made By ArT3k_-----");
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("craft")) {
                        if (player.hasPermission("bhammer.setcraft")) {
                            Inventory inv = Bukkit.createInventory(null, 27, "§cSet Hammer Craft");
                            ItemStack stick = new ItemStack(Material.RECORD_9, 1);
                            ItemMeta metastick = stick.getItemMeta();
                            metastick.setDisplayName("§cDont Touch");
                            metastick.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                            stick.setItemMeta(metastick);
                            int i;
                            for (i = 0; i < 3; i++)
                                inv.setItem(i, stick);
                            for (i = 6; i < 9; i++)
                                inv.setItem(i, stick);
                            for (i = 9; i < 12; i++)
                                inv.setItem(i, stick);
                            for (i = 15; i < 18; i++)
                                inv.setItem(i, stick);
                            for (i = 18; i < 21; i++)
                                inv.setItem(i, stick);
                            for (i = 24; i < 27; i++)
                                inv.setItem(i, stick);
                            player.openInventory(inv);
                        }
                    } else if (args[0].equalsIgnoreCase("give")) {
                        player.getInventory().setItemInHand(HammerCraft.getHammer());
                        player.updateInventory();
                    }
                } else if (args.length >= 2) {
                    if (args[0].equalsIgnoreCase("desc") &&
                            player.hasPermission("bhammer.desc")) {
                        String motfinal = "";
                        for (int i = 1; i < args.length; i++) {
                            if (i == 1) {
                                motfinal = String.valueOf(motfinal) + args[1];
                            } else {
                                motfinal = String.valueOf(motfinal) + " " + args[i];
                            }
                        }
                        this.main.getConfig().set("lore", motfinal.replace("&", "§"));
                        this.main.saveConfig();
                    }
                    if (args[0].equalsIgnoreCase("name") &&
                            player.hasPermission("bhammer.name")) {
                        String motfinal = "";
                        for (int i = 1; i < args.length; i++) {
                            if (i == 1) {
                                motfinal = String.valueOf(motfinal) + args[1];
                            } else {
                                motfinal = String.valueOf(motfinal) + " " + args[i];
                            }
                        }
                        this.main.getConfig().set("hammer-name", motfinal.replace("&", "§"));
                        this.main.saveConfig();
                    }
                }
        }
        return false;
    }
}