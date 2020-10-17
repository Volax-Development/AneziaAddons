package fr.volax.anezia.commands;

import fr.volax.anezia.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveChestViewerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("anezia.chestviewer.give")){
            sender.sendMessage("§cVous n'avez pas la permission d'executer cette commande !");
            return false;
        }

        if (cmd.getName().equalsIgnoreCase("givechestviewer")) {
            if (args.length == 0) {
                sender.sendMessage("§7/givechestviewer <player> <amount>");
                return true;
            }
            if (args.length == 2) {
                if (Bukkit.getPlayer(args[0]) != null && isInt(args[1])) {
                    Player target = Bukkit.getPlayer(args[0]);
                    ItemStack item = new ItemBuilder(Material.GOLD_NUGGET, 1).setName("§c§l✸ §6§lChestViewer §c§l✸").setLore("§7§m------------------------------------", "§eLe ChestViewer vous permet de voir le contenu d'un coffre", "§esans pour autant pouvoir le modifier.", "§7§m------------------------------------").toItemStack();
                    item.setAmount(Integer.parseInt(args[1]));
                    target.getInventory().addItem(item);
                    sender.sendMessage("§6Anézia §f» §eTu as donné §6" + args[1] + " §eChestViewer à §6" + target.getName() + "§e.");
                    return true;
                }
                sender.sendMessage("§6Anézia §f» §e/givechestviewer <player> <amount");
                return true;
            }
            sender.sendMessage("§6Anézia §f» §e/givechestviewer <player> <amount>");
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
