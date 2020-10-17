package fr.volax.anezia.commands;

import fr.volax.anezia.AneziaAddons;
import fr.volax.anezia.utils.CustomConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
                sender.sendMessage("");
                sender.sendMessage("§6- §7/hh                  §e<-- See all the HarvestHoe commands");
                sender.sendMessage("§6- §7/hh reload         §e<-- Reload the config");
                sender.sendMessage("§6- §7/hh give <player> §e<-- Give the harvest hoe to a player");
                sender.sendMessage("");
                sender.sendMessage("§7§m---------------------------------------------");
                return true;
            }
            if (args.length == 0 && !sender.hasPermission("harvesthoe.help")) {
                sender.sendMessage("§cNo permission for that :(");
                return true;
            }
            if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("harvesthoe.reload")) {
                AneziaAddons.getInstance().reloadConfig();
                CustomConfig.reload();
                for (Map.Entry<UUID, Boolean> safe : AneziaAddons.getInstance().safemode.entrySet())
                    CustomConfig.get().set(safe.getKey().toString(), safe.getValue());
                CustomConfig.save();
                sender.sendMessage(ChatColor.GREEN + "Harvest Hoe has been reloaded !");
            }
            if (args[0].equalsIgnoreCase("reload") && !sender.hasPermission("harvesthoe.reload")) {
                sender.sendMessage("§cNo permission for that :(");
                return true;
            }
            if (args[0].equalsIgnoreCase("safemode") && sender.hasPermission(AneziaAddons.getInstance().getConfig().getString("main-config.safe-mode.permission-require.permission")) && AneziaAddons.getInstance().getConfig().getBoolean("main-config.safe-mode.permission-require.enabled")) {
                if (!(sender instanceof Player))
                    return true;
                Player p = (Player)sender;
                if (!AneziaAddons.getInstance().getConfig().getBoolean("main-config.safe-mode.enabled")) {
                    p.sendMessage("§cSafe mode is not active on the server !");
                    return true;
                }
                if (!AneziaAddons.getInstance().safemode.containsKey(p.getUniqueId())) {
                    AneziaAddons.getInstance().safemode.put(p.getUniqueId(), Boolean.FALSE);
                    p.sendMessage(AneziaAddons.getInstance().getConfig().getString("main-config.safe-mode.disable-message").replace("&", "§"));
                    return true;
                }
                if (!(Boolean) AneziaAddons.getInstance().safemode.get(p.getUniqueId())) {
                    AneziaAddons.getInstance().safemode.put(p.getUniqueId(), Boolean.TRUE);
                    p.sendMessage(AneziaAddons.getInstance().getConfig().getString("main-config.safe-mode.enable-message").replace("&", "§"));
                } else {
                    AneziaAddons.getInstance().safemode.put(p.getUniqueId(), Boolean.FALSE);
                    p.sendMessage(AneziaAddons.getInstance().getConfig().getString("main-config.safe-mode.disable-message").replace("&", "§"));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("safemode") && sender.hasPermission(AneziaAddons.getInstance().getConfig().getString("main-config.safe-mode.permission-require.permission")) && !AneziaAddons.getInstance().getConfig().getBoolean("main-config.safe-mode.permission-require.enabled")) {
                sender.sendMessage("§cSafemode is disabled");
                return true;
            }
            if (args[0].equalsIgnoreCase("safemode") && !sender.hasPermission(AneziaAddons.getInstance().getConfig().getString("main-config.safe-mode.permission-require.permission")) && !AneziaAddons.getInstance().getConfig().getBoolean("main-config.safe-mode.permission-require.enabled")) {
                sender.sendMessage("§cSafemode is disabled");
                return true;
            }
            if (args[0].equalsIgnoreCase("safemode") && !sender.hasPermission(AneziaAddons.getInstance().getConfig().getString("main-config.safe-mode.permission-require.permission")) && AneziaAddons.getInstance().getConfig().getBoolean("main-config.safe-mode.permission-require.enabled")) {
                sender.sendMessage("§cNo permission for that :(");
                return true;
            }
            if (args[0].equalsIgnoreCase("give")) {
                if (sender.hasPermission("harvesthoe.give")) {
                    if (args.length >= 2) {
                        if (Bukkit.getPlayerExact(args[1]) == null) {
                            sender.sendMessage("§cPlayer not found :(");
                            return true;
                        }
                        Player p = Bukkit.getPlayerExact(args[1]);
                        PlayerInventory inventory = p.getInventory();
                        ItemStack harvesthoe = new ItemStack(Material.DIAMOND_HOE, 1);
                        ItemMeta harvesthoeM = harvesthoe.getItemMeta();
                        harvesthoeM.setDisplayName("§c§l✸ §6§lHoue De Farm §c§l✸");
                        harvesthoeM.setLore(Arrays.asList("§7§m------------------------------------", "", "§f§l» §eCette houe replante et duplique", "§evos plantations plus rapidement !", "" ,"§f§l» §e Utilisation §f: §7Infinie", "","§7§m------------------------------------"));
                        harvesthoeM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        harvesthoe.setItemMeta(harvesthoeM);
                        inventory.addItem(new ItemStack(harvesthoe));
                    } else sender.sendMessage("§7Type §c/hh give <player> §7to give HarvestHoe !");
                } else sender.sendMessage("§cNo permission for that :(");
            } else sender.sendMessage("§7Type §c/hh §7to get HarvestHoe plugin's help");
        }
        return true;
    }
}
