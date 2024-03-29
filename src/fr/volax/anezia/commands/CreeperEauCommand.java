package fr.volax.anezia.commands;

import fr.volax.anezia.utils.GiveItems;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreeperEauCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = (Player) sender;
        if (player.hasPermission("creepereau.use")) {
            if (args.length >= 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage("§6Anézia §f» §eCe joueur n'est actuellement pas connecté.");
                    return true;
                }
                if ((new GiveItems(target)).checkSlot()) {
                    target.getInventory().addItem((new GiveItems(1)).createCreeperWater());
                } else {
                    player.sendMessage("§6Anézia §f» §eVous ne pouvez pas vous donner §bl'oeuf §epuisque votre inventaire est complet.");
                    return true;
                }
            } else {
                player.sendMessage("§6Anézia §f» §eCommande : /creeepereau <Joueur>");
                return true;
            }
        } else {
            player.sendMessage("§cVous n'avez pas la permission d'executer cette commande");
            return true;
        }
        return true;
    }
}