package fr.volax.anezia.events;

import java.util.Random;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import fr.volax.anezia.AneziaAddons;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.Bukkit.getServer;

public class HarvestHoeListeners implements Listener {
    private final AneziaAddons main;


    public HarvestHoeListeners(AneziaAddons harvestHoe) {
        this.main = harvestHoe;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void RightClickWheat(PlayerInteractEvent event){
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getPlayer().getItemInHand().getType() == null) return;
            if(event.getPlayer().getItemInHand().getType() != Material.DIAMOND_HOE) return;
            if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;
            if(!event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("§c§l✸ §6§lHoue De Farm §c§l✸")) return;
                if (event.getClickedBlock().getType() == Material.CROPS && event.getClickedBlock().getData() == 7){
                    ItemStack bonemeal = new ItemStack(Material.INK_SACK, 1, (short) 15);
                    if (main.getConfig().getBoolean("enabled-crops.wheat")){
                        if (main.getConfig().getBoolean("hooksa.WorldGuard") && getServer().getPluginManager().getPlugin("WorldGuard") != null) {
                            if (WorldGuardPlugin.inst().canBuild(event.getPlayer(), event.getClickedBlock().getLocation().getBlock())){
                                if (!main.getConfig().getBoolean("allow-bonemeal-harvest") && event.getPlayer().getItemInHand().isSimilar(bonemeal)){
                                    return;
                                } else {
                                    event.getClickedBlock().setType(Material.AIR);
                                    Player p = event.getPlayer();
                                    EntityPlayer ep = ((CraftPlayer)p).getHandle();
                                    PacketPlayOutAnimation packet = new PacketPlayOutAnimation(ep, 0);
                                    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
                                    if (main.getConfig().getBoolean("auto-replant")){
                                        int upper = 2;
                                        Random random = new Random();
                                        event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.SEEDS, random.nextInt(upper)));
                                        event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.WHEAT, 1));
                                        event.getClickedBlock().setTypeId(59);
                                    } else {
                                        int upper = 4;
                                        Random random = new Random();
                                        event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.SEEDS, random.nextInt(upper)));
                                        event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.WHEAT, 1));
                                    }
                                }
                            }
                        } else {
                            if (!main.getConfig().getBoolean("allow-bonemeal-harvest") && event.getPlayer().getItemInHand().isSimilar(bonemeal)){
                                return;
                            } else {
                                event.getClickedBlock().setType(Material.AIR);
                                Player p = event.getPlayer();
                                EntityPlayer ep = ((CraftPlayer)p).getHandle();
                                PacketPlayOutAnimation packet = new PacketPlayOutAnimation(ep, 0);
                                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
                                if (main.getConfig().getBoolean("auto-replant")){
                                    int upper = 2;
                                    Random random = new Random();
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.SEEDS, random.nextInt(upper)));
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.WHEAT, 1));
                                    event.getClickedBlock().setTypeId(59);
                                } else {
                                    int upper = 4;
                                    Random random = new Random();
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.SEEDS, random.nextInt(upper)));
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.WHEAT, 1));
                                }
                            }
                        }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void RightClickPotato(PlayerInteractEvent event){
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getPlayer().getItemInHand().getType() == null) return;
            if(event.getPlayer().getItemInHand().getType() != Material.DIAMOND_HOE) return;
            if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;
            if(!event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("§c§l✸ §6§lHoue De Farm §c§l✸")) return;
                if (event.getClickedBlock().getType() == Material.POTATO && event.getClickedBlock().getData() == 7){
                    ItemStack bonemeal = new ItemStack(Material.INK_SACK, 1, (short) 15);
                    if (main.getConfig().getBoolean("enabled-crops.potato")){
                        if (main.getConfig().getBoolean("hooksa.WorldGuard") && getServer().getPluginManager().getPlugin("WorldGuard") != null) {
                            if (WorldGuardPlugin.inst().canBuild(event.getPlayer(), event.getClickedBlock().getLocation().getBlock())){
                                if (!main.getConfig().getBoolean("allow-bonemeal-harvest") && event.getPlayer().getItemInHand().isSimilar(bonemeal)){
                                    return;
                                } else {
                                    event.getClickedBlock().setType(Material.AIR);
                                    Player p = event.getPlayer();
                                    EntityPlayer ep = ((CraftPlayer)p).getHandle();
                                    PacketPlayOutAnimation packet = new PacketPlayOutAnimation(ep, 0);
                                    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
                                    if (main.getConfig().getBoolean("auto-replant")){
                                        int upper = 3;
                                        Random random = new Random();
                                        event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.POTATO_ITEM, random.nextInt(upper)));
                                        event.getClickedBlock().setType(Material.POTATO);
                                    } else {
                                        int upper = 4;
                                        Random random = new Random();
                                        event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.POTATO_ITEM, random.nextInt(upper)));
                                    }
                                }
                            }
                        } else {
                            if (!main.getConfig().getBoolean("allow-bonemeal-harvest") && event.getPlayer().getItemInHand().isSimilar(bonemeal)){
                                return;
                            } else {
                                event.getClickedBlock().setType(Material.AIR);
                                Player p = event.getPlayer();
                                EntityPlayer ep = ((CraftPlayer)p).getHandle();
                                PacketPlayOutAnimation packet = new PacketPlayOutAnimation(ep, 0);
                                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
                                if (main.getConfig().getBoolean("auto-replant")){
                                    int upper = 3;
                                    Random random = new Random();
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.POTATO_ITEM, random.nextInt(upper)));
                                    event.getClickedBlock().setType(Material.POTATO);
                                } else {
                                    int upper = 4;
                                    Random random = new Random();
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.POTATO_ITEM, random.nextInt(upper)));
                                }
                            }
                            }
                        }
                    }
            }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void RightClickCarrot(PlayerInteractEvent event){
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getPlayer().getItemInHand().getType() == null) return;
            if(event.getPlayer().getItemInHand().getType() != Material.DIAMOND_HOE) return;
            if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;
            if(!event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("§c§l✸ §6§lHoue De Farm §c§l✸")) return;
                if (event.getClickedBlock().getType() == Material.CARROT && event.getClickedBlock().getData() == 7){
                    ItemStack bonemeal = new ItemStack(Material.INK_SACK, 1, (short) 15);
                    if (main.getConfig().getBoolean("enabled-crops.carrot")){
                        if (main.getConfig().getBoolean("hooksa.WorldGuard") && getServer().getPluginManager().getPlugin("WorldGuard") != null) {
                            if (WorldGuardPlugin.inst().canBuild(event.getPlayer(), event.getClickedBlock().getLocation().getBlock())){
                                if (!main.getConfig().getBoolean("allow-bonemeal-harvest") && event.getPlayer().getItemInHand().isSimilar(bonemeal)){
                                    return;
                                } else {
                                    event.getClickedBlock().setType(Material.AIR);
                                    Player p = event.getPlayer();
                                    EntityPlayer ep = ((CraftPlayer)p).getHandle();
                                    PacketPlayOutAnimation packet = new PacketPlayOutAnimation(ep, 0);
                                    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
                                    if (main.getConfig().getBoolean("auto-replant")){
                                        int upper = 3;
                                        Random random = new Random();
                                        event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.CARROT_ITEM, random.nextInt(upper)));
                                        event.getClickedBlock().setType(Material.CARROT);
                                    } else {
                                        int upper = 4;
                                        Random random = new Random();
                                        event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.CARROT_ITEM, random.nextInt(upper)));
                                    }
                                }
                            }
                        } else {
                            if (!main.getConfig().getBoolean("allow-bonemeal-harvest") && event.getPlayer().getItemInHand().isSimilar(bonemeal)){
                                return;
                            } else {
                                event.getClickedBlock().setType(Material.AIR);
                                Player p = event.getPlayer();
                                EntityPlayer ep = ((CraftPlayer)p).getHandle();
                                PacketPlayOutAnimation packet = new PacketPlayOutAnimation(ep, 0);
                                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
                                if (main.getConfig().getBoolean("auto-replant")){
                                    int upper = 3;
                                    Random random = new Random();
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.CARROT_ITEM, random.nextInt(upper)));
                                    event.getClickedBlock().setType(Material.CARROT);
                                } else {
                                    int upper = 4;
                                    Random random = new Random();
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.CARROT_ITEM, random.nextInt(upper)));
                                }
                            }
                        }
                    }
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void RightClickNetherWart(PlayerInteractEvent event){
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getPlayer().getItemInHand().getType() == null) return;
            if(event.getPlayer().getItemInHand().getType() != Material.DIAMOND_HOE) return;
            if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;
            if(!event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("§c§l✸ §6§lHoue De Farm §c§l✸")) return;
                if (event.getClickedBlock().getType() == Material.NETHER_WARTS && event.getClickedBlock().getData() == 3){
                    if (main.getConfig().getBoolean("enabled-crops.nether_wart")){
                        if (main.getConfig().getBoolean("hooksa.WorldGuard") && getServer().getPluginManager().getPlugin("WorldGuard") != null) {
                            if (WorldGuardPlugin.inst().canBuild(event.getPlayer(), event.getClickedBlock().getLocation().getBlock())){
                                event.getClickedBlock().setType(Material.AIR);
                                Player p = event.getPlayer();
                                EntityPlayer ep = ((CraftPlayer)p).getHandle();
                                PacketPlayOutAnimation packet = new PacketPlayOutAnimation(ep, 0);
                                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
                                if (main.getConfig().getBoolean("auto-replant")){
                                    int upper = 3;
                                    Random random = new Random();
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.NETHER_STALK, 1));
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.NETHER_STALK, random.nextInt(upper)));
                                    event.getClickedBlock().setType(Material.NETHER_WARTS);
                                } else {
                                    int upper = 4;
                                    Random random = new Random();
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.NETHER_STALK, 1));
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.NETHER_STALK, random.nextInt(upper)));
                                }
                            }
                        } else {
                            event.getClickedBlock().setType(Material.AIR);
                            Player p = event.getPlayer();
                            EntityPlayer ep = ((CraftPlayer)p).getHandle();
                            PacketPlayOutAnimation packet = new PacketPlayOutAnimation(ep, 0);
                            ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
                            if (main.getConfig().getBoolean("auto-replant")){
                                int upper = 3;
                                Random random = new Random();
                                event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.NETHER_STALK, 1));
                                event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.NETHER_STALK, random.nextInt(upper)));
                                event.getClickedBlock().setType(Material.NETHER_WARTS);
                            } else {
                                int upper = 4;
                                Random random = new Random();
                                event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.NETHER_STALK, 1));
                                event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.NETHER_STALK, random.nextInt(upper)));
                            }
                        }
                    }
                }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void RightClickCocoa(PlayerInteractEvent event){
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getPlayer().getItemInHand().getType() == null) return;
            if(event.getPlayer().getItemInHand().getType() != Material.DIAMOND_HOE) return;
            if(event.getPlayer().getItemInHand().getItemMeta().getDisplayName() == null) return;
            if(!event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals("§c§l✸ §6§lHoue De Farm §c§l✸")) return;
                if (event.getClickedBlock().getType() == Material.COCOA && event.getClickedBlock().getData() == 8 || event.getClickedBlock().getType() == Material.COCOA && event.getClickedBlock().getData() == 9 || event.getClickedBlock().getType() == Material.COCOA &&event.getClickedBlock().getData() == 10 || event.getClickedBlock().getType() == Material.COCOA && event.getClickedBlock().getData() == 11){
                    ItemStack bonemeal = new ItemStack(Material.INK_SACK, 1, (short) 15);
                    byte blockData = event.getClickedBlock().getData();
                    if (main.getConfig().getBoolean("enabled-crops.cocoa")){
                        if (main.getConfig().getBoolean("hooksa.WorldGuard") && getServer().getPluginManager().getPlugin("WorldGuard") != null) {
                            if (WorldGuardPlugin.inst().canBuild(event.getPlayer(), event.getClickedBlock().getLocation().getBlock())){
                                if (!main.getConfig().getBoolean("allow-bonemeal-harvest") && event.getPlayer().getItemInHand().isSimilar(bonemeal)){
                                    return;
                                } else {
                                    event.getClickedBlock().setType(Material.AIR);
                                    Player p = event.getPlayer();
                                    EntityPlayer ep = ((CraftPlayer)p).getHandle();
                                    PacketPlayOutAnimation packet = new PacketPlayOutAnimation(ep, 0);
                                    ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
                                    if (main.getConfig().getBoolean("auto-replant")){
                                        int upper = 2;
                                        Random random = new Random();
                                        event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.INK_SACK, 1, (short) 3));
                                        event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.INK_SACK, random.nextInt(upper), (short)3));
                                        event.getClickedBlock().setTypeIdAndData(127,(byte) ((int) blockData -8), true);
                                    } else {
                                        int upper = 2;
                                        Random random = new Random();
                                        event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.INK_SACK, 1, (short) 3));
                                        event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.INK_SACK, random.nextInt(upper), (short)3));
                                    }
                                }
                            }
                        } else {
                            if (!main.getConfig().getBoolean("allow-bonemeal-harvest") && event.getPlayer().getItemInHand().isSimilar(bonemeal)){
                                return;
                            } else {
                                event.getClickedBlock().setType(Material.AIR);
                                Player p = event.getPlayer();
                                EntityPlayer ep = ((CraftPlayer)p).getHandle();
                                PacketPlayOutAnimation packet = new PacketPlayOutAnimation(ep, 0);
                                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
                                if (main.getConfig().getBoolean("auto-replant")){
                                    int upper = 2;
                                    Random random = new Random();
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.INK_SACK, 1, (short) 3));
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.INK_SACK, random.nextInt(upper), (short)3));
                                    event.getClickedBlock().setTypeIdAndData(127,(byte) (Integer.valueOf(blockData)-8), true);
                                } else {
                                    int upper = 2;
                                    Random random = new Random();
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.INK_SACK, 1, (short) 3));
                                    event.getClickedBlock().getLocation().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(Material.INK_SACK, random.nextInt(upper), (short)3));
                                }
                            }
                        }
                    }
            }
        }
    }
}