package fr.volax.anezia.commands;

import fr.volax.anezia.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GivePoisonSwordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("anezia.poisonsword.give")){
            sender.sendMessage("§cVous n'avez pas la permission d'executer cette commande !");
            return false;
        }

        if (cmd.getName().equalsIgnoreCase("givepoisonsword")) {
            if (args.length == 0) {
                sender.sendMessage("§7/givepoisonsword <player> <amount>");
                return true;
            }
            if (args.length == 2) {
                if (Bukkit.getPlayer(args[0]) != null && isInt(args[1])) {
                    Player target = Bukkit.getPlayer(args[0]);
                    ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD, 1).setName("§c§l✸ §6§lPoison Sword §c§l✸").addEnchant(Enchantment.DAMAGE_ALL, 5).addEnchant(Enchantment.DURABILITY, 3).addEnchant(Enchantment.FIRE_ASPECT, 2).setLore("§7§m------------------------------------", "§eLa Poison Sword permet de donner un effet de poison", "§ede 5 secondes avec un cooldown de 20 secondes.", "§7§m------------------------------------").toItemStack();
                    item.setAmount(Integer.parseInt(args[1]));
                    target.getInventory().addItem(item);
                    sender.sendMessage("§6Anézia §f» §eTu as donné §6" + args[1] + " §ePoison Sword à §6" + target.getName() + "§e.");
                    return true;
                }
                sender.sendMessage("§6Anézia §f» §e/givepoisonsword <player> <amount");
                return true;
            }
            sender.sendMessage("§6Anézia §f» §e/givepoisonsword <player> <amount>");
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