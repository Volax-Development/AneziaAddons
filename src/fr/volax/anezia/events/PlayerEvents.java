package fr.volax.anezia.events;

import java.util.HashMap;
import java.util.Map;

import fr.volax.anezia.AneziaAddons;
import fr.volax.anezia.nbt.NBTItem;
import fr.volax.anezia.timers.MessageTimer;
import fr.volax.anezia.timers.SoundTimer;
import fr.volax.anezia.utils.ConfigValues;
import fr.volax.anezia.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class PlayerEvents implements Listener {
    private AneziaAddons main;
    private Map<OfflinePlayer, Location> chunkBusterLocations = new HashMap<>();
    private Map<OfflinePlayer, Long> playerCooldowns = new HashMap<>();
    private Map<OfflinePlayer, Long> noFallDamage = new HashMap<>();

    public PlayerEvents(AneziaAddons main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChunkBusterPlace(BlockPlaceEvent e) {
        NBTItem nbtItem = new NBTItem(e.getItemInHand());
        if (e.getItemInHand().getType().equals(this.main.getConfigValues().getChunkBusterMaterial()) && nbtItem.hasKey("chunkbuster.radius").booleanValue()) {
            e.setCancelled(true);
            Player p = e.getPlayer();
            if (p.hasPermission("chunkbuster.use")) {
                if (this.main.getHookUtils().hasFaction(p)) {
                    if (this.main.getHookUtils().checkRole(p)) {
                        if (this.main.getHookUtils().compareLocToPlayer(e.getBlock().getLocation(), p)) {
                            if (this.main.getConfigValues().getCooldown() > 0 &&
                                    this.playerCooldowns.containsKey(p) &&
                                    System.currentTimeMillis() < ((Long) this.playerCooldowns.get(p)).longValue()) {
                                long longDifference = ((Long) this.playerCooldowns.get(e.getPlayer())).longValue() - System.currentTimeMillis();
                                int secondsDifference = (int) (longDifference / 1000L);
                                int seconds = secondsDifference % 60;
                                int minutes = secondsDifference / 60;
                                this.main.getUtils().sendMessage((CommandSender) p, ConfigValues.Message.COOLDOWN, new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)});
                                return;
                            }
                            this.chunkBusterLocations.put(p, e.getBlock().getLocation());
                            if (!p.getOpenInventory().getTitle().contains(this.main.getConfigValues().getGUITitle())) {
                                Inventory confirmInv = Bukkit.createInventory(null, 9 * this.main.getConfigValues().getGUIRows(), this.main.getConfigValues().getGUITitle());
                                ItemStack acceptItem = this.main.getConfigValues().getConfirmBlockItemStack();
                                if (!acceptItem.getType().equals(Material.AIR)) {
                                    ItemMeta acceptItemMeta = acceptItem.getItemMeta();
                                    acceptItemMeta.setDisplayName(this.main.getConfigValues().getConfirmName());
                                    acceptItemMeta.setLore(this.main.getConfigValues().getConfirmLore());
                                    acceptItem.setItemMeta(acceptItemMeta);
                                }
                                ItemStack cancelItem = this.main.getConfigValues().getCancelBlockItemStack();
                                if (!cancelItem.getType().equals(Material.AIR)) {
                                    ItemMeta cancelItemMeta = cancelItem.getItemMeta();
                                    cancelItemMeta.setDisplayName(this.main.getConfigValues().getCancelName());
                                    cancelItemMeta.setLore(this.main.getConfigValues().getCancelLore());
                                    cancelItem.setItemMeta(cancelItemMeta);
                                }
                                ItemStack fillItem = this.main.getConfigValues().getFillItemStack();
                                if (!fillItem.getType().equals(Material.AIR)) {
                                    ItemMeta fillItemMeta = fillItem.getItemMeta();
                                    fillItemMeta.setDisplayName(this.main.getConfigValues().getFillName());
                                    fillItemMeta.setLore(this.main.getConfigValues().getFillLore());
                                    fillItem.setItemMeta(fillItemMeta);
                                }
                                int slotCounter = 1;
                                for (int i = 0; i < 9 * this.main.getConfigValues().getGUIRows(); i++) {
                                    if (slotCounter < 5) {
                                        confirmInv.setItem(i, acceptItem);
                                    } else if (slotCounter > 5) {
                                        confirmInv.setItem(i, cancelItem);
                                    } else {
                                        confirmInv.setItem(i, fillItem);
                                    }
                                    if (slotCounter >= 9) {
                                        slotCounter = 1;
                                    } else {
                                        slotCounter++;
                                    }
                                }
                                p.openInventory(confirmInv);
                            }
                        } else {
                            this.main.getUtils().sendMessage((CommandSender) p, ConfigValues.Message.CANNOT_PLACE, new Object[0]);
                        }
                    } else {
                        this.main.getUtils().sendMessage((CommandSender) p, ConfigValues.Message.NOT_MINIMUM_ROLE, new Object[0]);
                    }
                } else {
                    this.main.getUtils().sendMessage((CommandSender) p, ConfigValues.Message.NO_FACTION, new Object[0]);
                }
            } else {
                this.main.getUtils().sendMessage((CommandSender) p, ConfigValues.Message.NO_PERMISSION_PLACE, new Object[0]);
            }
        }
    }

    @EventHandler
    public void onConfirmClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player && e.getClickedInventory() != null && e.getView().getTitle() != null && e
                .getView().getTitle().equals(this.main.getConfigValues().getGUITitle())) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            Location chunkBusterLocation = this.chunkBusterLocations.get(p);
            if (chunkBusterLocation != null) {
                if (this.main.getHookUtils().hasFaction(p)) {
                    if (this.main.getHookUtils().checkRole(p)) {
                        if (this.main.getHookUtils().compareLocToPlayer(chunkBusterLocation, p) || this.main.getHookUtils().isWilderness(chunkBusterLocation)) {
                            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains(this.main.getConfigValues().getConfirmName())) {
                                int itemSlot = -1;
                                NBTItem nbtItem = new NBTItem(p.getItemInHand());
                                if (p.getItemInHand() != null && p.getItemInHand().getType().equals(this.main.getConfigValues().getChunkBusterMaterial()) && nbtItem.hasKey("chunkbuster.radius").booleanValue()) {
                                    itemSlot = p.getInventory().getHeldItemSlot();
                                } else {
                                    for (int i = 0; i <= 40; i++) {
                                        ItemStack currentItem = p.getInventory().getItem(i);
                                        nbtItem = new NBTItem(currentItem);
                                        if (currentItem != null && currentItem.getType().equals(this.main.getConfigValues().getChunkBusterMaterial()) && nbtItem.hasKey("chunkbuster.radius").booleanValue()) {
                                            itemSlot = i;
                                            break;
                                        }
                                    }
                                    if (itemSlot == -1) {
                                        this.chunkBusterLocations.remove(p);
                                        p.closeInventory();
                                        if (this.main.getConfigValues().cancelSoundEnabled())
                                            p.playSound(p.getLocation(), this.main.getConfigValues().getCancelSoundString(), this.main.getConfigValues().getCancelSoundVolume(), this.main.getConfigValues().getCancelSoundPitch());
                                        this.main.getUtils().sendMessage((CommandSender) p, ConfigValues.Message.NO_ITEM, new Object[0]);
                                        return;
                                    }
                                }
                                ItemStack checkItem = p.getInventory().getItem(itemSlot);
                                nbtItem = new NBTItem(checkItem);
                                int chunkBusterDiameter = nbtItem.getInteger("chunkbuster.radius").intValue();
                                this.playerCooldowns.put(p, Long.valueOf(System.currentTimeMillis() + (1000 * this.main.getConfigValues().getCooldown())));
                                this.chunkBusterLocations.remove(p);
                                p.closeInventory();
                                if (this.main.getConfigValues().confirmSoundEnabled())
                                    p.playSound(p.getLocation(), this.main.getConfigValues().getConfirmSoundString(), this.main.getConfigValues().getConfirmSoundVolume(), this.main.getConfigValues().getConfirmSoundPitch());
                                if (p.getGameMode().equals(GameMode.SURVIVAL) || p.getGameMode().equals(GameMode.ADVENTURE))
                                    if (checkItem.getAmount() <= 1) {
                                        p.getInventory().setItem(itemSlot, null);
                                    } else {
                                        checkItem.setAmount(checkItem.getAmount() - 1);
                                        p.getInventory().setItem(itemSlot, checkItem);
                                    }
                                if (this.main.getConfigValues().getChunkBusterWarmup() > 0) {
                                    int seconds = this.main.getConfigValues().getChunkBusterWarmup();
                                    (new MessageTimer(seconds, p.getUniqueId(), this.main)).runTaskTimer((Plugin) this.main, 0L, 20L);
                                    if (this.main.getConfigValues().warmupSoundEnabled())
                                        (new SoundTimer(this.main, p, (int) (seconds / this.main.getConfigValues().getWarmupSoundInterval()))).runTaskTimer((Plugin) this.main, 0L, 20L * this.main.getConfigValues().getWarmupSoundInterval());
                                    Bukkit.getScheduler().runTaskLater((Plugin) this.main, () -> {
                                        if (this.main.getConfigValues().clearingSoundEnabled())
                                            p.playSound(p.getLocation(), this.main.getConfigValues().getClearingSoundString(), this.main.getConfigValues().getClearingSoundVolume(), this.main.getConfigValues().getClearingSoundPitch());
                                        this.main.getUtils().clearChunks(chunkBusterDiameter, chunkBusterLocation, p);
                                        if (this.main.getConfigValues().getNoFallMillis() > 0)
                                            this.noFallDamage.put(p, System.currentTimeMillis() + this.main.getConfigValues().getNoFallMillis());
                                    }, 20L * seconds);
                                } else {
                                    if (this.main.getConfigValues().clearingSoundEnabled())
                                        p.playSound(p.getLocation(), this.main.getConfigValues().getClearingSoundString(), this.main.getConfigValues().getClearingSoundVolume(), this.main.getConfigValues().getClearingSoundPitch());
                                    this.main.getUtils().clearChunks(chunkBusterDiameter, chunkBusterLocation, p);
                                    if (this.main.getConfigValues().getNoFallMillis() > 0)
                                        this.noFallDamage.put(p, Long.valueOf(System.currentTimeMillis() + this.main.getConfigValues().getNoFallMillis()));
                                }
                            } else if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().getDisplayName().contains(this.main.getConfigValues().getCancelName())) {
                                this.chunkBusterLocations.remove(p);
                                p.closeInventory();
                                if (this.main.getConfigValues().cancelSoundEnabled())
                                    p.playSound(p.getLocation(), this.main.getConfigValues().getCancelSoundString(), this.main.getConfigValues().getCancelSoundVolume(), this.main.getConfigValues().getCancelSoundPitch());
                                this.main.getUtils().sendMessage((CommandSender) p, ConfigValues.Message.GUI_CANCEL, new Object[0]);
                            }
                        } else {
                            this.main.getUtils().sendMessage((CommandSender) p, ConfigValues.Message.CANNOT_PLACE, new Object[0]);
                        }
                    } else {
                        this.main.getUtils().sendMessage((CommandSender) p, ConfigValues.Message.NOT_MINIMUM_ROLE, new Object[0]);
                    }
                } else {
                    this.main.getUtils().sendMessage((CommandSender) p, ConfigValues.Message.NO_FACTION, new Object[0]);
                }
            } else {
                p.sendMessage(Utils.color("&cError, please re-place your chunk buster."));
            }
        }
    }

    @EventHandler
    public void onGUIClose(InventoryCloseEvent e) {
        if (e.getView().getTitle().contains(this.main.getConfigValues().getGUITitle()) && e.getPlayer() instanceof Player)
            this.chunkBusterLocations.remove(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL && e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (this.noFallDamage.containsKey(p))
                if (((Long) this.noFallDamage.get(p)).longValue() >= System.currentTimeMillis()) {
                    e.setCancelled(true);
                } else {
                    this.noFallDamage.remove(p);
                }
        }
    }
}