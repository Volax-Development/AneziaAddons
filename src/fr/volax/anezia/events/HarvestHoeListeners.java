package fr.volax.anezia.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import fr.volax.anezia.AneziaAddons;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.CocoaPlant;
import org.bukkit.material.Crops;
import org.bukkit.material.NetherWarts;

public class HarvestHoeListeners implements Listener {
    private final AneziaAddons main;

    private final HashMap<UUID, Boolean> safemd;

    public HarvestHoeListeners(AneziaAddons harvestHoe) {
        this.main = harvestHoe;
        this.safemd = this.main.getSafeMode();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (this.safemd.containsKey(p.getUniqueId()))
            return;
        this.safemd.put(p.getUniqueId(), false);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Block blockAbove;
        Crops crops;
        CropState state = null;
        NetherWarts warts;
        NetherWartsState netherstate = null;
        CocoaPlant cocoa;
        CocoaPlant.CocoaPlantSize cocoastate = null;
        if (block.getType() == Material.COCOA) {
            cocoa = (CocoaPlant) block.getState().getData();
            cocoastate = cocoa.getSize();
        }
        if (block.getType() == Material.NETHER_WARTS) {
            warts = (NetherWarts) block.getState().getData();
            netherstate = warts.getState();
        }
        if (block.getType() == Material.CROPS || block.getType() == Material.CARROT || block.getType() == Material.POTATO) {
            crops = (Crops) block.getState().getData();
            state = crops.getState();
        }
        Location blockPosAbove = block.getLocation();
        ItemStack item = player.getInventory().getItemInHand();
        int amount = 0;
        double totalEarned;
        if (!AneziaAddons.getInstance().setupEconomy())
            return;
        if (item.getType() == Material.DIAMOND_HOE && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(this.main.getConfig().getString("main-config.hoe-name").replace("&", "§"))) {
            if (!this.safemd.containsKey(player.getUniqueId()))
                this.safemd.put(player.getUniqueId(), Boolean.FALSE);
            if (this.safemd.get(player.getUniqueId()) && this.main.getConfig().getBoolean("main-config.safe-mode.enabled"))
                event.setCancelled(true);
            if (block.getType() == Material.SUGAR_CANE_BLOCK) {
                Location blockUnder = new Location(event.getBlock().getWorld(), block.getX(), (block.getY() - 1), block.getZ());
                if (!blockUnder.getBlock().getType().equals(Material.SUGAR_CANE_BLOCK) && this.safemd.get(player.getUniqueId()) && this.main.getConfig().getBoolean("main-config.safe-mode.enabled")) {
                    event.setCancelled(true);
                    return;
                }
                blockPosAbove.add(0.0D, 1.0D, 0.0D);
                blockAbove = blockPosAbove.getBlock();
                amount = amount + 1;
                checkForMoney("scane", player, event);
                if (this.main.getConfig().getBoolean("sounds.scane.enabled"))
                    PlaySound(player, this.main.getConfig().getString("sounds.scane.sound"), (float) this.main.getConfig().getDouble("sounds.scane.volume"), (float) this.main.getConfig().getDouble("sounds.scane.pitch"));
                while (blockAbove.getType() == Material.SUGAR_CANE_BLOCK) {
                    blockPosAbove.add(0.0D, 1.0D, 0.0D);
                    amount = amount + 1;
                    blockAbove = blockPosAbove.getBlock();
                    checkForMoney("scane", player, event);
                }
                totalEarned = checkForSellPrice("scane", player, amount);
                for (int bpa = blockPosAbove.getBlockY() - 1; bpa >= block.getY(); bpa--) {
                    blockPosAbove.add(0.0D, -1.0D, 0.0D);
                    blockAbove = blockPosAbove.getBlock();
                    blockAbove.setType(Material.AIR);
                }
                if (this.main.getConfig().getBoolean("messages.scane.chat.enabled")) {
                    String message = this.main.getConfig().getString("messages.scane.chat.message").replace("&", "§").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName());
                    message = message.replace("%price%", Double.toString(totalEarned));
                    player.sendMessage(this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message);
                }
                if (this.main.getConfig().getBoolean("messages.scane.hotbar.enabled")) {
                    String message = this.main.getConfig().getString("messages.scane.hotbar.message").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName());
                    message = message.replace("%price%", Double.toString(totalEarned));
                    message = this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message.replace("&", "§");
                    sendActionBar(player, message);
                }
            }
            if (block.getType() == Material.CROPS && state == CropState.RIPE) {
                amount = (new Random()).nextInt(3) + 1;
                checkForMoney("wheat", player, event);
                if (this.main.getConfig().getBoolean("sounds.wheat.enabled"))
                    PlaySound(player, this.main.getConfig().getString("sounds.wheat.sound"), (float) this.main.getConfig().getDouble("sounds.wheat.volume"), (float) this.main.getConfig().getDouble("sounds.wheat.pitch"));
                totalEarned = checkForSellPrice("wheat", player, amount);
                if (this.main.getConfig().getBoolean("auto-replant.wheat")) {
                    block.setType(Material.CROPS);
                } else {
                    block.setType(Material.AIR);
                }
                if (this.main.getConfig().getBoolean("messages.wheat.chat.enabled")) {
                    String message = this.main.getConfig().getString("messages.wheat.chat.message").replace("&", "§").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName());
                    message = message.replace("%price%", Double.toString(totalEarned));
                    player.sendMessage(this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message);
                }
                if (this.main.getConfig().getBoolean("messages.wheat.hotbar.enabled")) {
                    String message = this.main.getConfig().getString("messages.wheat.hotbar.message").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName());
                    message = message.replace("%price%", Double.toString(totalEarned));
                    message = this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message.replace("&", "§");
                    sendActionBar(player, message);
                }
            }
            if (block.getType() == Material.CARROT && state == CropState.RIPE) {
                amount = (new Random()).nextInt(3) + 1;
                checkForMoney("carrots", player, event);
                if (this.main.getConfig().getBoolean("sounds.carrots.enabled"))
                    PlaySound(player, this.main.getConfig().getString("sounds.carrots.sound"), (float) this.main.getConfig().getDouble("sounds.carrots.volume"), (float) this.main.getConfig().getDouble("sounds.carrots.pitch"));
                totalEarned = checkForSellPrice("carrots", player, amount);
                if (this.main.getConfig().getBoolean("auto-replant.carrots")) {
                    block.setType(Material.CARROT);
                } else {
                    block.setType(Material.AIR);
                }
                if (this.main.getConfig().getBoolean("messages.carrots.chat.enabled")) {
                    String message = this.main.getConfig().getString("messages.carrots.chat.message").replace("&", "§").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName());
                    message = message.replace("%price%", Double.toString(totalEarned));
                    player.sendMessage(this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message);
                }
                if (this.main.getConfig().getBoolean("messages.carrots.hotbar.enabled")) {
                    String message = this.main.getConfig().getString("messages.carrots.hotbar.message").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName());
                    message = message.replace("%price%", Double.toString(totalEarned));
                    message = this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message.replace("&", "§");
                    sendActionBar(player, message);
                }
            }
            if (block.getType() == Material.POTATO && state == CropState.RIPE) {
                amount = (new Random()).nextInt(3) + 1;
                checkForMoney("potatoes", player, event);
                if (this.main.getConfig().getBoolean("sounds.potatoes.enabled"))
                    PlaySound(player, this.main.getConfig().getString("sounds.potatoes.sound"), (float) this.main.getConfig().getDouble("sounds.potatoes.volume"), (float) this.main.getConfig().getDouble("sounds.potatoes.pitch"));
                totalEarned = checkForSellPrice("potatoes", player, amount);
                if (this.main.getConfig().getBoolean("auto-replant.potatoes")) {
                    block.setType(Material.POTATO);
                } else {
                    block.setType(Material.AIR);
                }
                if (this.main.getConfig().getBoolean("messages.potatoes.chat.enabled")) {
                    String message = this.main.getConfig().getString("messages.potatoes.chat.message").replace("&", "§").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName());
                    message = message.replace("%price%", Double.toString(totalEarned));
                    player.sendMessage(this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message);
                }
                if (this.main.getConfig().getBoolean("messages.potatoes.hotbar.enabled")) {
                    String message = this.main.getConfig().getString("messages.potatoes.hotbar.message").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName());
                    message = message.replace("%price%", Double.toString(totalEarned));
                    message = this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message.replace("&", "§");
                    sendActionBar(player, message);
                }
            }
            if (block.getType() == Material.MELON_BLOCK) {
                amount = (new Random()).nextInt(3) + 1;
                checkForMoney("melons", player, event);
                if (this.main.getConfig().getBoolean("sounds.melons.enabled"))
                    PlaySound(player, this.main.getConfig().getString("sounds.melons.sound"), (float) this.main.getConfig().getDouble("sounds.melons.volume"), (float) this.main.getConfig().getDouble("sounds.melons.pitch"));
                totalEarned = checkForSellPrice("melons", player, amount);
                block.setType(Material.AIR);
                if (this.main.getConfig().getBoolean("messages.melons.chat.enabled")) {
                    String message = this.main.getConfig().getString("messages.melons.chat.message").replace("&", "§").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName());
                    message = message.replace("%price%", Double.toString(totalEarned));
                    player.sendMessage(this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message);
                }
                if (this.main.getConfig().getBoolean("messages.melons.hotbar.enabled")) {
                    String message = this.main.getConfig().getString("messages.melons.hotbar.message").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName());
                    message = message.replace("%price%", Double.toString(totalEarned));
                    message = this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message.replace("&", "§");
                    sendActionBar(player, message);
                }
            }
            if (block.getType() == Material.PUMPKIN) {
                amount = amount + 1;
                checkForMoney("pumpkins", player, event);
                if (this.main.getConfig().getBoolean("sounds.pumpkins.enabled"))
                    PlaySound(player, this.main.getConfig().getString("sounds.pumpkins.sound"), (float) this.main.getConfig().getDouble("sounds.pumpkins.volume"), (float) this.main.getConfig().getDouble("sounds.pumpkins.pitch"));
                totalEarned = checkForSellPrice("pumpkins", player, amount);
                block.setType(Material.AIR);
                if (this.main.getConfig().getBoolean("messages.pumpkins.chat.enabled")) {
                    String message = this.main.getConfig().getString("messages.pumpkins.chat.message").replace("&", "§").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName()).replace("%price%", Double.toString(totalEarned));
                    player.sendMessage(this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message);
                }
                if (this.main.getConfig().getBoolean("messages.pumpkins.hotbar.enabled")) {
                    String message = this.main.getConfig().getString("messages.pumpkins.hotbar.message").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName()).replace("%price%", Double.toString(totalEarned));
                    message = this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message.replace("&", "§");
                    sendActionBar(player, message);
                }
            }
            if (block.getType() == Material.NETHER_WARTS && netherstate == NetherWartsState.RIPE) {
                amount = (new Random()).nextInt(3) + 1;
                checkForMoney("netherwarts", player, event);
                if (this.main.getConfig().getBoolean("sounds.netherwarts.enabled"))
                    PlaySound(player, this.main.getConfig().getString("sounds.netherwarts.sound"), (float) this.main.getConfig().getDouble("sounds.netherwarts.volume"), (float) this.main.getConfig().getDouble("sounds.netherwarts.pitch"));
                totalEarned = checkForSellPrice("netherwarts", player, amount);
                if (this.main.getConfig().getBoolean("auto-replant.netherwarts")) {
                    block.setType(Material.NETHER_WARTS);
                } else {
                    block.setType(Material.AIR);
                }
                if (this.main.getConfig().getBoolean("messages.netherwarts.chat.enabled")) {
                    String message = this.main.getConfig().getString("messages.netherwarts.chat.message").replace("&", "§").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName()).replace("%price%", Double.toString(totalEarned));
                    player.sendMessage(this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message);
                }
                if (this.main.getConfig().getBoolean("messages.netherwarts.hotbar.enabled")) {
                    String message = this.main.getConfig().getString("messages.netherwarts.hotbar.message").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName()).replace("%price%", Double.toString(totalEarned));
                    message = this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message.replace("&", "§");
                    sendActionBar(player, message);
                }
            }
            if (block.getType() == Material.COCOA && cocoastate == CocoaPlant.CocoaPlantSize.LARGE) {
                amount = (new Random()).nextInt(3) + 1;
                checkForMoney("cocoabeans", player, event);
                if (this.main.getConfig().getBoolean("sounds.cocoabeans.enabled"))
                    PlaySound(player, this.main.getConfig().getString("sounds.cocoabeans.sound"), (float) this.main.getConfig().getDouble("sounds.cocoabeans.volume"), (float) this.main.getConfig().getDouble("sounds.cocoabeans.pitch"));
                totalEarned = checkForSellPrice("cocoabeans", player, amount);
                if (this.main.getConfig().getBoolean("auto-replant.cocoabeans")) {
                    BlockFace face = ((CocoaPlant) block.getState().getData()).getFacing();
                    BlockState st = block.getState();
                    st.setData(new CocoaPlant(CocoaPlant.CocoaPlantSize.SMALL, face));
                    st.update();
                } else {
                    block.setType(Material.AIR);
                }
                if (this.main.getConfig().getBoolean("messages.cocoabeans.chat.enabled")) {
                    String message = this.main.getConfig().getString("messages.cocoabeans.chat.message").replace("&", "§").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName());
                    message = message.replace("%price%", Double.toString(totalEarned));
                    player.sendMessage(this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message);
                }
                if (this.main.getConfig().getBoolean("messages.cocoabeans.hotbar.enabled")) {
                    String message = this.main.getConfig().getString("messages.cocoabeans.hotbar.message").replace("%quantity%", Integer.toString(amount));
                    message = message.replace("%player%", player.getDisplayName());
                    message = message.replace("%price%", Double.toString(totalEarned));
                    message = this.main.getConfig().getString("messages.prefix-sell-messages").replace("&", "§") + message.replace("&", "§");
                    sendActionBar(player, message);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack it = event.getItem();
        if (it == null)
            return;
        if (it.getType() == Material.DIAMOND_HOE && (
                action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) &&
                it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equals(this.main.getConfig().getString("main-config.hoe-name").replace("&", "§")))
            if ((player.hasPermission(this.main.getConfig().getString("main-config.open-permission")) && this.main.getConfig().getBoolean("main-config.requires-permission")) || !this.main.getConfig().getBoolean("main-config.requires-permission")) {
                event.setCancelled(true);
                if (player.hasPermission("*"))
                    player.sendMessage("§cYou are currently op so the level up system consider you as level three");
                if (this.main.getConfig().getBoolean("main-config.open-sound.enabled"))
                    PlaySound(player, this.main.getConfig().getString("main-config.open-sound.sound"), (float) this.main.getConfig().getDouble("main-config.open-sound.volume"), (float) this.main.getConfig().getDouble("main-config.open-sound.pitch"));
                boolean sendmsg = this.main.getConfig().getBoolean("main-config.enable-open-menu-message");
                if (sendmsg)
                    player.sendMessage(this.main.getConfig().getString("main-config.open-menu-message").replace("&", "§"));
                Inventory inv = Bukkit.createInventory(null, this.main.getConfig().getInt("gui.gui-size"), this.main.getConfig().getString("gui.gui-name").replace("&", "§"));
                ArrayList<String> lorelevelone = getLore("gui.levelone.lore");
                ArrayList<String> loreleveltwo = getLore("gui.leveltwo.lore");
                ArrayList<String> lorelevelthree = getLore("gui.levelthree.lore");
                ArrayList<String> lorequit = getLore("gui.quit.lore");
                ArrayList<String> loreother = getLore("gui.other.lore");
                Material matLevelOne, matLevelTwo, matLevelThree, matQuit,matOther;
                matLevelOne = Material.matchMaterial(this.main.getConfig().getString("gui.levelone.material"));
                matLevelTwo = Material.matchMaterial(this.main.getConfig().getString("gui.leveltwo.material"));
                matLevelThree = Material.matchMaterial(this.main.getConfig().getString("gui.levelthree.material"));
                matQuit = Material.matchMaterial(this.main.getConfig().getString("gui.quit.material"));
                matOther = Material.matchMaterial(this.main.getConfig().getString("gui.other.material"));
                byte LevelOnedata = (byte) 0, LevelTwodata = (byte) 0, LevelThreedata = (byte) 0, Quitdata = (byte) 0, Otherdata = (byte) 0;
                if (this.main.getConfig().getInt("gui.levelone.data") != 0) {
                    int dataInt = this.main.getConfig().getInt("gui.levelone.data");
                    LevelOnedata = (byte) dataInt;
                } else if (this.main.getConfig().getInt("gui.leveltwo.data") != 0) {
                    int dataInt = this.main.getConfig().getInt("gui.leveltwo.data");
                    LevelTwodata = (byte) dataInt;
                } else if (this.main.getConfig().getInt("gui.levelthree.data") != 0) {
                    int dataInt;
                    dataInt = this.main.getConfig().getInt("gui.levelthree.data");
                    LevelThreedata = (byte) dataInt;
                } else if (this.main.getConfig().getInt("gui.quit.data") != 0) {
                    int dataInt = this.main.getConfig().getInt("gui.quit.data");
                    Quitdata = (byte) dataInt;
                } else if (this.main.getConfig().getInt("gui.other.data") != 0) {
                    int dataInt = this.main.getConfig().getInt("gui.other.data");
                    Otherdata = (byte) dataInt;
                }
                boolean enchone = false;
                boolean enchtwo = false;
                boolean enchthree = false;
                boolean noench = false;
                if (player.hasPermission("*")) {
                    lorelevelthree = getLore("gui.levelthree.unlocked-lore");
                    enchthree = true;
                } else if (player.hasPermission(this.main.getConfig().getString("gui.levelone.permission-to-get-unlocked"))) {
                    lorelevelone = getLore("gui.levelone.unlocked-lore");
                    enchone = true;
                } else if (player.hasPermission(this.main.getConfig().getString("gui.leveltwo.permission-to-get-unlocked"))) {
                    loreleveltwo = getLore("gui.leveltwo.unlocked-lore");
                    enchtwo = true;
                } else if (player.hasPermission(this.main.getConfig().getString("gui.levelthree.permission-to-get-unlocked"))) {
                    lorelevelthree = getLore("gui.levelthree.unlocked-lore");
                    enchthree = true;
                }
                String[] slots = this.main.getConfig().getString("gui.other.slots").split(",");
                byte b;
                int i;
                String[] arrayOfString1;
                for (i = (arrayOfString1 = slots).length, b = 0; b < i; ) {
                    String oneslot = arrayOfString1[b];
                    int theSlot = Integer.parseInt(oneslot);
                    inv.setItem(theSlot, createMyItem(matOther, this.main.getConfig().getString("gui.other.title").replace("&", "§"), loreother, 1, Otherdata, noench));
                    b++;
                }
                inv.setItem(this.main.getConfig().getInt("gui.levelone.slot"), createMyItem(matLevelOne, this.main.getConfig().getString("gui.levelone.title").replace("&", "§"), lorelevelone, this.main.getConfig().getInt("gui.levelone.quantity"), LevelOnedata, enchone));
                inv.setItem(this.main.getConfig().getInt("gui.leveltwo.slot"), createMyItem(matLevelTwo, this.main.getConfig().getString("gui.leveltwo.title").replace("&", "§"), loreleveltwo, this.main.getConfig().getInt("gui.leveltwo.quantity"), LevelTwodata, enchtwo));
                inv.setItem(this.main.getConfig().getInt("gui.levelthree.slot"), createMyItem(matLevelThree, this.main.getConfig().getString("gui.levelthree.title").replace("&", "§"), lorelevelthree, this.main.getConfig().getInt("gui.levelthree.quantity"), LevelThreedata, enchthree));
                inv.setItem(this.main.getConfig().getInt("gui.quit.slot"), createMyItem(matQuit, this.main.getConfig().getString("gui.quit.title").replace("&", "§"), lorequit, 1, Quitdata, noench));
                player.openInventory(inv);
            } else {
                player.sendMessage(this.main.getConfig().getString("main-config.no-perm-message").replace("&", "§"));
            }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack current = event.getCurrentItem();
        Material levelonematerial = Material.matchMaterial(this.main.getConfig().getString("gui.levelone.material"));
        Material leveltwomaterial = Material.matchMaterial(this.main.getConfig().getString("gui.leveltwo.material"));
        Material levelthreematerial = Material.matchMaterial(this.main.getConfig().getString("gui.levelthree.material"));
        Material quitmaterial = Material.matchMaterial(this.main.getConfig().getString("gui.quit.material"));
        if (current == null)
            return;
        if (inv.getTitle().equals(this.main.getConfig().getString("gui.gui-name").replace("&", "§"))) {
            event.setCancelled(true);
            if (current.getType() == levelonematerial && current.getItemMeta().getDisplayName().equals(this.main.getConfig().getString("gui.levelone.title").replace("&", "§"))) {
                player.closeInventory();
                if (current.hasItemMeta() && current.getItemMeta().hasDisplayName() &&
                        AneziaAddons.getInstance().setupEconomy() && AneziaAddons.getInstance().setupPermissions())
                    if (player.hasPermission(this.main.getConfig().getString("gui.levelone.permission-to-access"))) {
                        if (!player.hasPermission(this.main.getConfig().getString("gui.levelone.permission-to-get-unlocked")) || player.hasPermission("*")) {
                            if (!player.hasPermission(this.main.getConfig().getString("gui.leveltwo.permission-to-get-unlocked")) && !player.hasPermission(this.main.getConfig().getString("gui.levelthree.permission-to-get-unlocked"))) {
                                if (AneziaAddons.economy.getBalance(player) >= this.main.getConfig().getInt("gui.levelone.price-to-upgrade")) {
                                    AneziaAddons.economy.withdrawPlayer(player, this.main.getConfig().getInt("gui.levelone.price-to-upgrade"));
                                    AneziaAddons.permission.playerAdd(player, this.main.getConfig().getString("gui.levelone.permission-to-get-unlocked"));
                                    player.sendMessage("§c" + this.main.getConfig().getInt("gui.levelone.price-to-upgrade") + "$ §7has been taken from your account !");
                                    player.sendMessage("§bHarvestHoe §ahas successfully upgraded to level 1 !");
                                    if (this.main.getConfig().getBoolean("gui.levelone.levelup-sound.enabled"))
                                        PlaySound(player, this.main.getConfig().getString("gui.levelone.levelup-sound.sound"), (float) this.main.getConfig().getDouble("gui.levelone.levelup-sound.volume"), (float) this.main.getConfig().getDouble("gui.levelone.levelup-sound.pitch"));
                                } else {
                                    player.sendMessage("§cYou don't have enought money for that (" + AneziaAddons.economy.getBalance(player) + "$ / " + this.main.getConfig().getInt("gui.levelone.price-to-upgrade") + "$");
                                }
                            } else {
                                player.sendMessage("§cYou already have a better level !");
                            }
                        } else {
                            player.sendMessage("§cLevel 1 Already unlocked");
                        }
                    } else {
                        player.sendMessage("§cInssuficient permission");
                    }
                event.setCancelled(true);
                return;
            }
            if (current.getType() == leveltwomaterial && current.getItemMeta().getDisplayName().equals(this.main.getConfig().getString("gui.leveltwo.title").replace("&", "§"))) {
                player.closeInventory();
                if (current.hasItemMeta() && current.getItemMeta().hasDisplayName() &&
                        AneziaAddons.getInstance().setupEconomy())
                    if (player.hasPermission(this.main.getConfig().getString("gui.leveltwo.permission-to-access"))) {
                        if (!player.hasPermission(this.main.getConfig().getString("gui.leveltwo.permission-to-get-unlocked")) || player.hasPermission("*")) {
                            if (!player.hasPermission(this.main.getConfig().getString("gui.levelthree.permission-to-get-unlocked"))) {
                                if (AneziaAddons.economy.getBalance(player) >= this.main.getConfig().getInt("gui.leveltwo.price-to-upgrade")) {
                                    AneziaAddons.economy.withdrawPlayer(player, this.main.getConfig().getInt("gui.leveltwo.price-to-upgrade"));
                                    AneziaAddons.permission.playerAdd(player, this.main.getConfig().getString("gui.leveltwo.permission-to-get-unlocked"));
                                    player.sendMessage("§c" + this.main.getConfig().getInt("gui.leveltwo.price-to-upgrade") + "$ §7has been taken from your account !");
                                    player.sendMessage("§bHarvestHoe §ahas successfully upgraded to level 2 !");
                                    AneziaAddons.permission.playerRemove(player.getWorld().toString(), player, this.main.getConfig().getString("gui.levelone.permission-to-get-unlocked"));
                                    if (this.main.getConfig().getBoolean("gui.leveltwo.levelup-sound.enabled"))
                                        PlaySound(player, this.main.getConfig().getString("gui.leveltwo.levelup-sound.sound"), (float) this.main.getConfig().getDouble("gui.leveltwo.levelup-sound.volume"), (float) this.main.getConfig().getDouble("gui.leveltwo.levelup-sound.pitch"));
                                } else {
                                    player.sendMessage("§cYou don't have enought money for that (" + AneziaAddons.economy.getBalance(player) + "$ / " + this.main.getConfig().getInt("gui.leveltwo.price-to-upgrade") + "$");
                                }
                            } else {
                                player.sendMessage("§cYou already have a better level");
                            }
                        } else {
                            player.sendMessage("§cLevel 2 Already unlocked");
                        }
                    } else {
                        player.sendMessage("§cInssuficient permission");
                    }
                event.setCancelled(true);
                return;
            }
            if (current.getType() == levelthreematerial && current.getItemMeta().getDisplayName().equals(this.main.getConfig().getString("gui.levelthree.title").replace("&", "§"))) {
                player.closeInventory();
                if (current.hasItemMeta() && current.getItemMeta().hasDisplayName() &&
                        AneziaAddons.getInstance().setupEconomy())
                    if (player.hasPermission(this.main.getConfig().getString("gui.levelthree.permission-to-access"))) {
                        if (!player.hasPermission(this.main.getConfig().getString("gui.levelthree.permission-to-get-unlocked")) || player.hasPermission("*")) {
                            if (!player.hasPermission(this.main.getConfig().getString("gui.levelone.permission-to-get-unlocked"))) {
                                if (AneziaAddons.economy.getBalance(player) >= this.main.getConfig().getInt("gui.levelthree.price-to-upgrade")) {
                                    AneziaAddons.economy.withdrawPlayer(player, this.main.getConfig().getInt("gui.levelthree.price-to-upgrade"));
                                    AneziaAddons.permission.playerAdd(player, this.main.getConfig().getString("gui.levelthree.permission-to-get-unlocked"));
                                    player.sendMessage("§c" + this.main.getConfig().getInt("gui.levelthree.price-to-upgrade") + "$ §7has been taken from your account !");
                                    player.sendMessage("§bHarvestHoe §ahas successfully upgraded to level 3 !");
                                    AneziaAddons.permission.playerRemove(player.getWorld().toString(), player, this.main.getConfig().getString("gui.leveltwo.permission-to-get-unlocked"));
                                    if (this.main.getConfig().getBoolean("gui.levelthree.levelup-sound.enabled"))
                                        PlaySound(player, this.main.getConfig().getString("gui.levelthree.levelup-sound.sound"), (float) this.main.getConfig().getDouble("gui.levelthree.levelup-sound.volume"), (float) this.main.getConfig().getDouble("gui.levelthree.levelup-sound.pitch"));
                                } else {
                                    player.sendMessage("§cYou don't have enought money for that (" + AneziaAddons.economy.getBalance(player) + "$ / " + this.main.getConfig().getInt("gui.levelthree.price-to-upgrade") + "$");
                                }
                            } else {
                                player.sendMessage("§cUnlock level 2 first !");
                            }
                        } else {
                            player.sendMessage("§cLevel 3 Already unlocked");
                        }
                    } else {
                        player.sendMessage("§cInssuficient permission");
                    }
                event.setCancelled(true);
                return;
            }
            if (current.getType() == quitmaterial && current.getItemMeta().getDisplayName().equals(this.main.getConfig().getString("gui.quit.title").replace("&", "§"))) {
                player.closeInventory();
            } else {
                event.setCancelled(true);
            }
        }
    }

    public ItemStack createMyItem(Material material, String customName, ArrayList<String> lore, Integer quantity, Byte data, Boolean ench) {
        ItemStack it = new ItemStack(material, quantity, data);
        ItemMeta itM = it.getItemMeta();
        if (customName != null)
            itM.setDisplayName(customName);
        if (lore != null)
            itM.setLore(lore);
        if (ench) {
            itM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itM.addEnchant(Enchantment.SILK_TOUCH, 1, true);
        }
        it.setItemMeta(itM);
        return it;
    }

    public void PlaySound(Player player, String sound, Float Volume, Float Pitch) {
        Sound thesound;
        thesound = Sound.valueOf(sound);
        player.playSound(player.getLocation(), thesound, Volume, Pitch);
    }

    public ArrayList<String> getLore(String s) {
        ArrayList<String> lore = (ArrayList<String>) this.main.getConfig().getList(s);
        for (int i = 0; i < lore.size(); i = i + 1) {
            String str = lore.get(i);
            lore.set(i, str.replace("&", "§"));
        }
        return lore;
    }

    private void checkForMoney(String s, Player player, BlockBreakEvent event) {
        if (player.hasPermission(this.main.getConfig().getString("gui.levelone.permission-to-get-unlocked")) && this.main.getConfig().getBoolean("gui.levelone." + s + ".enabled")) {
            AneziaAddons.economy.depositPlayer(player, this.main.getConfig().getDouble("gui.levelone." + s + ".sell-price"));
            event.setCancelled(true);
        } else if (player.hasPermission(this.main.getConfig().getString("gui.leveltwo.permission-to-get-unlocked")) && this.main.getConfig().getBoolean("gui.leveltwo." + s + ".enabled")) {
            AneziaAddons.economy.depositPlayer(player, this.main.getConfig().getDouble("gui.leveltwo." + s + ".sell-price"));
            event.setCancelled(true);
        } else if (player.hasPermission(this.main.getConfig().getString("gui.levelthree.permission-to-get-unlocked")) && this.main.getConfig().getBoolean("gui.levelthree." + s + ".enabled")) {
            AneziaAddons.economy.depositPlayer(player, this.main.getConfig().getDouble("gui.levelthree." + s + ".sell-price"));
            event.setCancelled(true);
        }
    }

    private double checkForSellPrice(String s, Player player, Integer amount) {
        double totalEarned = 0.0D;
        if (player.hasPermission("*")) {
            totalEarned = amount * this.main.getConfig().getDouble("gui.levelthree." + s + ".sell-price");
        } else if (player.hasPermission(this.main.getConfig().getString("gui.levelone.permission-to-get-unlocked"))) {
            totalEarned = amount * this.main.getConfig().getDouble("gui.levelone." + s + ".sell-price");
        } else if (player.hasPermission(this.main.getConfig().getString("gui.leveltwo.permission-to-get-unlocked"))) {
            totalEarned = amount * this.main.getConfig().getDouble("gui.leveltwo." + s + ".sell-price");
        } else if (player.hasPermission(this.main.getConfig().getString("gui.levelthree.permission-to-get-unlocked"))) {
            totalEarned = amount * this.main.getConfig().getDouble("gui.levelthree." + s + ".sell-price");
        }
        return totalEarned;
    }

    public void sendActionBar(Player player, String message) {
        CraftPlayer p = (CraftPlayer) player;
        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);
        (p.getHandle()).playerConnection.sendPacket(ppoc);
    }
}