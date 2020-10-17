package fr.volax.anezia.commands;

import fr.volax.anezia.AneziaAddons;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;

public class ChestViewerCommand implements CommandExecutor {
    private AneziaAddons main;

    public ChestViewerCommand(AneziaAddons main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) throwHelp(sender);
        else if (args.length == 1) {
            String str;
            switch ((str = args[0]).hashCode()) {
                case 3198785:
                    if (!str.equals("help"))
                        break;
                    if (sender.hasPermission("chestviewer.help")) {
                        throwHelp(sender);
                        break;
                    }
                    sender.sendMessage(this.main.getConfig().getString("no-perm"));
                    break;
                case 3619493:
                    if (!str.equals("view"))
                        break;
                    if (sender instanceof Player) {
                        Player p = (Player)sender;
                        if (p.hasPermission("chestviewer.mode")) {
                            if (p.getTargetBlock((HashSet<Byte>) null, 100).getType() == Material.CHEST) {
                                Chest c = (Chest)p.getTargetBlock((HashSet<Byte>) null, 100).getState();
                                if (!this.main.getConfig().getBoolean("gui")) {
                                    byte b1;
                                    int j;
                                    ItemStack[] arrayOfItemStack1;
                                    for (j = (arrayOfItemStack1 = c.getInventory().getContents()).length, b1 = 0; b1 < j; ) {
                                        ItemStack itemStack = arrayOfItemStack1[b1];
                                        if (itemStack != null &&
                                                !this.main.getConfig().getStringList("blacklisted-items").contains(itemStack.getType().name()))
                                            if (this.main.getConfig().getBoolean("show-amount")) {
                                                p.sendMessage(this.main.getConfig().getString("item-type-amount")
                                                        .replaceAll("%item%", itemStack.getType().name().toLowerCase())
                                                        .replaceAll("%num%", String.valueOf(itemStack.getAmount())));
                                            } else {
                                                p.sendMessage(this.main.getConfig().getString("item-type")
                                                        .replaceAll("%item%", itemStack.getType().name().toLowerCase()));
                                            }
                                        b1++;
                                    }
                                    break;
                                }
                                int slots = c.getInventory().getSize();
                                Inventory inv = Bukkit.createInventory(p, 54, this.main.getConfig()
                                        .getString("chest-name").replaceAll("%size%", Integer.toString(slots)));
                                byte b;
                                int i;
                                ItemStack[] arrayOfItemStack;
                                for (i = (arrayOfItemStack = c.getInventory().getContents()).length, b = 0; b < i; ) {
                                    ItemStack itemStack = arrayOfItemStack[b];
                                    if (itemStack != null &&
                                            !this.main.getConfig().getStringList("blacklisted-items").contains(itemStack.getType().name()))
                                        if (this.main.getConfig().getBoolean("show-amount")) {
                                            inv.addItem(itemStack);
                                        } else {
                                            inv.addItem(new ItemStack(itemStack.getType(), 1));
                                        }
                                    b++;
                                }
                                p.openInventory(inv);
                                break;
                            }
                            p.sendMessage(this.main.getConfig().getString("chest-warning"));
                            break;
                        }
                        p.sendMessage(this.main.getConfig().getString("no-perm"));
                        break;
                    }
                    sender.sendMessage(this.main.getConfig().getString("only-players"));
                    break;
            }
        } else {
            sender.sendMessage(this.main.getConfig().getString("args"));
        }
        return false;
    }

    private void throwHelp(CommandSender sender) {
        sender.sendMessage("§6Anézia §f» §eChestViewer §ehelp page");
        sender.sendMessage("§6Anézia §f» §e/chestviewer help - §dopens this page");
        sender.sendMessage("§6Anézia §f» §e/chestviewer view - §dview chest");
    }
}
