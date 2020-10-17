package fr.volax.anezia.commands;

import fr.volax.anezia.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class GiveEndCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("anezia.poisonsword.give")){
            sender.sendMessage("§cVous n'avez pas la permission d'executer cette commande !");
            return false;
        }

        if (cmd.getName().equalsIgnoreCase("giveend")) {
            if (args.length == 0) {
                sender.sendMessage("§7/giveend <player> <amount>");
                return true;
            }
            if (args.length == 2) {
                if (Bukkit.getPlayer(args[0]) != null && isInt(args[1])) {
                    Player target = Bukkit.getPlayer(args[0]);
                    ItemStack item = new ItemBuilder(Material.ENDER_STONE, 1).setName("§c§l✸ §6§lBloc d'End §c§l✸").addEnchant(Enchantment.PROTECTION_FIRE, 1).addItemFlag(ItemFlag.HIDE_ENCHANTS).setLore("§7§m------------------------------------", "§f§l» §7Le bloc d'end est très utile pour protéger vos bases,","§7   c'est le bloc le plus résistant, il résiste","§7   jusqu'à 20 coup de TNT/Creeper", "§7§m------------------------------------").toItemStack();
                    item.setAmount(Integer.parseInt(args[1]));
                    target.getInventory().addItem(item);
                    sender.sendMessage("§6Anézia §f» §eTu as donné §6" + args[1] + " §eEnd stone à §6" + target.getName() + "§e.");
                    return true;
                }
                sender.sendMessage("§6Anézia §f» §e/giveend <player> <amount");
                return true;
            }
            sender.sendMessage("§6Anézia §f» §e/giveend <player> <amount>");
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
