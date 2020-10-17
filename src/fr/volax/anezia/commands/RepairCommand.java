package fr.volax.anezia.commands;

import fr.volax.anezia.AneziaAddons;
import fr.volax.anezia.utils.TimeUnit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RepairCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("§cVous devez être un joueur pour executer cette commande !");
            return false;
        }
        Player player = (Player) sender;

        if(!player.hasPermission("anezia.repair.use")){
            player.sendMessage("§cVous n'avez pas la permission d'executer cette commande !");
            return false;
        }

        if(args.length == 0){
            helpMessage(sender);
            return false;
        }else if(args.length == 1){
            if(args[0].equalsIgnoreCase("hand")){
                ItemStack item = player.getItemInHand();

                if(item == null || item.getType() == Material.AIR || item.getType().isBlock() || item.getDurability() == 0 || item.getMaxStackSize() != 1 || item.getType().getMaxDurability() < 25)    {
                    player.sendMessage("§6Anézia §f» §eCet objet ne peut être réparé.");
                    return false;
                }

                if(AneziaAddons.getInstance().repair.containsKey(player.getUniqueId())){
                    float time = (System.currentTimeMillis() - AneziaAddons.getInstance().repair.get(player.getUniqueId())) / 1000;
                    float waitingTime = 3600;
                    if(player.hasPermission("repair.cooldown.30")) waitingTime = 1800;
                    if(player.hasPermission("repair.cooldown.15")) waitingTime = 900;
                    if(time < waitingTime){
                        player.sendMessage("§6Anézia §f» §eVous ne pouvez pas repair votre item pour le moment, veillez attendre §6" + getTimeLeft((int) (waitingTime - (int)time)) + " §e.");
                        return false;
                    }else { AneziaAddons.getInstance().repair.put(player.getUniqueId(), System.currentTimeMillis()); item.setDurability((short) 0); }
                } else{ AneziaAddons.getInstance().repair.put(player.getUniqueId(), System.currentTimeMillis()); item.setDurability((short) 0); }
                player.sendMessage("§6Anézia §f» §eVous venez de réparer l'item que vous avez dans votre main !");
                return false;
            }else if(args[0].equalsIgnoreCase("all")){
                if(!player.hasPermission("anezia.repairall.use")){
                    player.sendMessage("§cVous n'avez pas la permission d'executer cette commande !");
                    return false;
                }

                if(AneziaAddons.getInstance().repair.containsKey(player.getUniqueId())){
                    float time = (System.currentTimeMillis() - AneziaAddons.getInstance().repair.get(player.getUniqueId())) / 1000;
                    float waitingTime = 3600;
                    if(player.hasPermission("repairall.cooldown.30")) waitingTime = 1800;
                    if(player.hasPermission("repairall.cooldown.15")) waitingTime = 900;
                    if(time < waitingTime){
                        player.sendMessage("§6Anézia §f» §eVous ne pouvez pas repair vos items pour le moment, veillez attendre §6" + getTimeLeft((int) (waitingTime - (int)time)) + " §e.");
                        return false;
                    }else { AneziaAddons.getInstance().repair.put(player.getUniqueId(), System.currentTimeMillis()); }
                } else{ AneziaAddons.getInstance().repair.put(player.getUniqueId(), System.currentTimeMillis()); }
                repairAll(player);
                return false;
            }else{
                helpMessage(player);
                return false;
            }
        }else{
            helpMessage(player);
            return false;
        }
    }

    private void helpMessage(CommandSender sender){
        sender.sendMessage("§6Anézia §f» §e/repair [hand|all]");
    }

    public void repairAll(Player player){
        List<String> repairItems = new ArrayList<>();

        for(ItemStack item : player.getInventory().getContents()){
            if(item == null || item.getType() == Material.AIR || item.getType().isBlock() || item.getDurability() == 0 || item.getMaxStackSize() != 1 || item.getType().getMaxDurability() < 25) continue;

            item.setDurability((short) 0);
            repairItems.add(item.getItemMeta().getDisplayName());
        }

        for(ItemStack item : player.getInventory().getArmorContents()){
            if(item == null || item.getType() == Material.AIR || item.getType().isBlock() || item.getDurability() == 0 || item.getMaxStackSize() != 1 || item.getType().getMaxDurability() < 25) continue;

            item.setDurability((short) 0);
            repairItems.add(item.getItemMeta().getDisplayName());
        }

        if(repairItems.isEmpty()) player.sendMessage("§6Anézia §f» §eAucun item ne peut être réparé dans votre inventaire.");
        else player.sendMessage("§6Anézia §f» §eVous venez de réparer des items dans votre inventaire.");
    }

    public String getTimeLeft(int all_time){
        int heures = 0;
        int minutes = 0;
        int secondes = 0;


        while (all_time >= TimeUnit.HEURE.getToSecond()){
            heures++;
            all_time -= TimeUnit.HEURE.getToSecond();
        }

        while (all_time >= TimeUnit.MINUTE.getToSecond()){
            minutes++;
            all_time -= TimeUnit.MINUTE.getToSecond();
        }

        while (all_time >= TimeUnit.SECONDE.getToSecond()){
            secondes++;
            all_time -= TimeUnit.SECONDE.getToSecond();
        }

        if(heures == 0)
            if(minutes == 0) return secondes + "s";
            else return "§6" + minutes + "§em §6" + secondes + "§es";
        else return "§6" + heures + " §e" + TimeUnit.HEURE.getName() + " §6" + minutes + " §e" + TimeUnit.MINUTE.getName() + " §6" + secondes + " §e" + TimeUnit.SECONDE.getName();
    }
}
