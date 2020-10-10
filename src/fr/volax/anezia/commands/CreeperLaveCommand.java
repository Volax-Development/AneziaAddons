package fr.volax.anezia.commands;

import fr.volax.anezia.utils.GiveItems;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreeperLaveCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = (Player) sender;
        if (player.hasPermission("creepereau.use")) {
            if (args.length >= 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage("§e[§6§lCreeperLave§e] Ce joueur n'est actuellement pas connecté.");
                    return true;
                }
                if ((new GiveItems(target)).checkSlot()) {
                    target.getInventory().addItem((new GiveItems(1)).createCreeperLava());
                } else {
                    player.sendMessage("§e[§6§lCreeperLave§e] Vous ne pouvez pas vous donner §6l'oeuf §epuisque votre inventaire est complet.");
                    return true;
                }
            } else {
                player.sendMessage("§7Commande : /creeepereau <Joueur>");
                return true;
            }
        } else {
            player.sendMessage("§e[§6§lCreeperLave§e] Vous n'avez pas les permissions requises pour effectuer cette commande !");
            return true;
        }
        return true;
    }
}