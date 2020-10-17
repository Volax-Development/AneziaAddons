package fr.volax.anezia.commands;

import java.util.HashMap;

import fr.volax.anezia.AneziaAddons;
import fr.volax.anezia.timers.Timer;
import fr.volax.anezia.utils.ConfigHandler;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HLRCommandMain implements CommandExecutor {
    private AneziaAddons plugin;

    public HLRCommandMain(AneziaAddons instance) {
        this.plugin = instance;
    }

    public static HashMap<Player, Boolean> start = new HashMap<>();
    public ConfigHandler configHandler;

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        boolean canUseCommand = true;
        boolean paying = false;
        boolean convert = true;
        this.configHandler = this.plugin.configHandler;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            double fee = this.configHandler.fee;
            boolean useEco = this.configHandler.useEco;
            double money = 0.0D;
            if (useEco)
                money = AneziaAddons.getMoney(player);
            if (this.configHandler.usePerms)
                canUseCommand = player.hasPermission("HLR.convert");
            if (!canUseCommand)
                sender.sendMessage(ConfigHandler.prefix + ConfigHandler.msgMap.get("ConvertCmd.NoPermsConvert"));
            if (canUseCommand && fee > 0.0D && useEco) {
                paying = true;
                if (money < fee) {
                    canUseCommand = false;
                    player.sendMessage(ConfigHandler.prefix + ConfigHandler.msgMap.get("ConvertCmd.NoMoneyConvert"));
                }
                if (this.configHandler.usePerms) {
                    if (player.hasPermission("HLR.nofee"))
                        paying = false;
                } else if (player.isOp()) {
                    paying = false;
                }
            }
            if (canUseCommand) {
                ItemStack item;
                ItemMeta meta;
                if (Integer.parseInt(AneziaAddons.ver.split("\\.")[1].trim()) >= 9) {
                    item = player.getInventory().getItemInHand();
                    meta = player.getInventory().getItemInHand().getItemMeta();
                } else {
                    item = player.getItemInHand();
                    meta = player.getItemInHand().getItemMeta();
                }
                String CHname = AneziaAddons.CHname;
                int maxamount = this.configHandler.maxamount;
                if (this.configHandler.usePerms) {
                    if (player.hasPermission("HLR.limitbypass")) maxamount = 64;
                } else if (player.isOp()) maxamount = 64;
                if (start.isEmpty() || !start.containsKey(player))
                    start.put(player, false);
                if (!this.configHandler.cooldown || !(Boolean) start.get(player)) {
                    if (item.getType().equals(Material.HOPPER)) {
                        if (item.getAmount() <= maxamount) {
                            if (meta.hasLore() && meta.hasDisplayName() && (
                                    meta.getLore().equals(AneziaAddons.hopperlore) || meta.getDisplayName().equals(AneziaAddons.CHname)))
                                convert = false;
                            if (convert) {
                                meta.setDisplayName(CHname);
                                meta.setLore(AneziaAddons.hopperlore);
                                item.setItemMeta(meta);
                                player.sendMessage(ConfigHandler.prefix + ConfigHandler.msgMap.get("ConvertCmd.ConvertSuccess"));
                                if (this.configHandler.cooldown)
                                    if (this.configHandler.usePerms) {
                                        if (!player.hasPermission("HLR.nocooldown")) {
                                            start.put(player, true);
                                            Timer timer = new Timer();
                                            timer.main(player);
                                        }
                                    } else if (!player.isOp()) {
                                        start.put(player, true);
                                        Timer timer = new Timer();
                                        timer.main(player);
                                    }
                                if (paying) {
                                    AneziaAddons.economy.withdrawPlayer(player, fee);
                                    player.sendMessage(ConfigHandler.prefix + ConfigHandler.msgMap.get("ConvertCmd.TransactionCost"));
                                }
                            } else player.sendMessage(ConfigHandler.prefix + ConfigHandler.msgMap.get("ConvertCmd.ConvertedHopper"));
                        } else player.sendMessage(ConfigHandler.prefix + ConfigHandler.msgMap.get("ConvertCmd.TooMuchHopperAtOnce"));
                    } else player.sendMessage(ConfigHandler.prefix + ConfigHandler.msgMap.get("ConvertCmd.NotHoldingHopper"));
                } else player.sendMessage(ConfigHandler.prefix + ConfigHandler.msgMap.get("ConvertCmd.StillCoolingDown"));
            }
        } else sender.sendMessage(ConfigHandler.msgMap.get("ConvertCmd.PlayerUseOnly"));
        return true;
    }
}