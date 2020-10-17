package fr.volax.anezia.events;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fr.volax.anezia.AneziaAddons;
import fr.volax.anezia.utils.ConfigHandler;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CHlistener implements Listener {
    private AneziaAddons plugin;

    public static HashMap<Map.Entry<UUID, String>, List<Location>> blockInfo = new HashMap<>();

    private static HashMap<Location, ItemStack> dropCheck = new HashMap<>();

    public CHlistener(AneziaAddons instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack Mblock;
        Player player = event.getPlayer();
        String world = event.getBlock().getWorld().getName();
        boolean old = false, inMain = false, inOff = false;
        if (Integer.parseInt(AneziaAddons.ver.split("\\.")[1].trim()) >= 9) {
            Mblock = player.getInventory().getItemInHand();
        } else {
            Mblock = player.getItemInHand();
            old = true;
        }
        if (Mblock.hasItemMeta() &&
                Mblock.getItemMeta().hasDisplayName() && Mblock.getItemMeta().hasLore() && Mblock.getType() == Material.HOPPER &&
                Mblock.getItemMeta().getDisplayName().equals(AneziaAddons.CHname) && Mblock.getItemMeta().getLore().equals(AneziaAddons.hopperlore))
            inMain = true;
        if (inMain)
            if (this.plugin.isEnabledIn(world)) {
                if (old) {
                    File DataFile = new File(this.plugin.getDataFolder() + File.separator +
                            "Data.yml");
                    YamlConfiguration DFile = YamlConfiguration.loadConfiguration(DataFile);
                    double x = event.getBlock().getX();
                    double y = event.getBlock().getY();
                    double z = event.getBlock().getZ();
                    String coord = x + "," + y + "," + z;
                    Location loc = event.getBlock().getLocation();
                    Map.Entry<UUID, String> entry = new AbstractMap.SimpleEntry<>(event.getBlock().getWorld().getUID(), event.getBlock().getLocation().getChunk().toString());
                    if (!blockInfo.containsKey(entry) || ConfigHandler.chunkHopperLimit == -1 || blockInfo.get(entry).size() < ConfigHandler.chunkHopperLimit || player.hasPermission("HLR.nochunklimit")) {
                        if (!blockInfo.containsKey(entry)) {
                            List<Location> list = new ArrayList<>();
                            list.add(loc);
                            blockInfo.put(entry, list);
                        } else {
                            blockInfo.get(entry).add(loc);
                        }
                        List<String> coordlist = DFile.getStringList(world);
                        coordlist.add(coord);
                        DFile.set(world, coordlist);
                        try {
                            DFile.save(this.plugin.getDataFolder() + File.separator +
                                    "Data.yml");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        player.sendMessage(ConfigHandler.prefix + ConfigHandler.msgMap.get("Listener.PlacedHopper"));
                    } else {
                        event.setCancelled(true);
                        player.sendMessage(ConfigHandler.prefix + ConfigHandler.msgMap.get("Listener.HopperLimitReached"));
                    }
                }
            } else {
                player.sendMessage(ConfigHandler.prefix + ConfigHandler.msgMap.get("Listener.NotEnabledInWorld"));
                event.setCancelled(true);
            }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        String world = event.getBlock().getWorld().getName();
        if (this.plugin.isEnabledIn(world)) {
            File DataFile = new File(this.plugin.getDataFolder() + File.separator +
                    "Data.yml");
            YamlConfiguration DFile = YamlConfiguration.loadConfiguration(DataFile);
            double x = event.getBlock().getX();
            double y = event.getBlock().getY();
            double z = event.getBlock().getZ();
            String coord = x + "," + y + "," + z;
            if (DFile.getStringList(world).contains(coord)) {
                boolean skip = false;
                Map.Entry<UUID, String> entry = new AbstractMap.SimpleEntry<>(event.getBlock().getWorld().getUID(), event.getBlock().getLocation().getChunk().toString());
                try {
                    ((List) blockInfo.get(entry)).remove(event.getBlock().getLocation());
                } catch (NullPointerException e) {
                    skip = true;
                }
                if (!skip) {
                    List<String> coordlist = DFile.getStringList(world);
                    coordlist.remove(coord);
                    DFile.set(world, coordlist);
                    ItemStack drop = new ItemStack(Material.HOPPER, 1);
                    event.getBlock().breakNaturally(new ItemStack(Material.AIR));
                    ItemMeta meta = drop.getItemMeta();
                    if (ConfigHandler.retainTweak) {
                        meta.setDisplayName(AneziaAddons.CHname);
                        meta.setLore(AneziaAddons.hopperlore);
                        drop.setItemMeta(meta);
                    }
                    event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
                    player.sendMessage(String.valueOf(ConfigHandler.prefix) + (String) ConfigHandler.msgMap.get("Listener.DestroyedHopper"));
                    try {
                        DFile.save(this.plugin.getDataFolder() + File.separator +
                                "Data.yml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent event) {
        if (!ConfigHandler.spawnerAllow && !ConfigHandler.spawnerWhitelist.contains(event.getEntityType())) {
            Location loc = event.getLocation();
            if (!checkCHPresence(loc))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        if (!ConfigHandler.waterAllow && ConfigHandler.waterBlacklist.contains(event.getToBlock().getType())) {
            Location loc = event.getToBlock().getLocation();
            if (!checkCHPresence(loc))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (!ConfigHandler.pistonAllow) {
            String blockStr = event.getBlocks().toString();
            int index = StringUtils.indexOfAny(blockStr, ConfigHandler.strPistonBlacklist.<String>toArray(new String[ConfigHandler.strPistonBlacklist.size()]));
            if (index != -1) {
                int i = StringUtils.countMatches(blockStr.substring(0, index), "type=");
                Location loc = ((Block) event.getBlocks().get(i - 1)).getLocation();
                if (!checkCHPresence(loc))
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        if (!ConfigHandler.cactusAllow) {
            BlockState state = event.getNewState();
            if (state.getType() == Material.CACTUS) {
                Block block = state.getBlock();
                if ((block.getRelative(BlockFace.NORTH).getType() != Material.AIR || block.getRelative(BlockFace.SOUTH).getType() != Material.AIR ||
                        block.getRelative(BlockFace.EAST).getType() != Material.AIR || block.getRelative(BlockFace.WEST).getType() != Material.AIR) &&
                        !checkCHPresence(state.getLocation()))
                    event.setCancelled(true);
            }
        }
    }

    private boolean checkCHPresence(Location loc) {
        Map.Entry<UUID, String> entry = new AbstractMap.SimpleEntry<>(loc.getWorld().getUID(), loc.getChunk().toString());
        if (!blockInfo.containsKey(entry) || ((List) blockInfo.get(entry)).isEmpty())
            return false;
        return true;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        if (!ConfigHandler.greedy &&
                this.plugin.isEnabledIn(event.getItemDrop().getLocation().getWorld().getName()))
            dropCheck.put(event.getItemDrop().getLocation(), event.getItemDrop().getItemStack());
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (!ConfigHandler.greedy &&
                !dropCheck.isEmpty() && dropCheck.containsKey(event.getEntity().getLocation()) && ((ItemStack) dropCheck.get(event.getEntity().getLocation())).equals(event.getEntity().getItemStack())) {
            dropCheck.remove(event.getEntity().getLocation());
            return;
        }
        ItemStack item = event.getEntity().getItemStack();
        ItemStack sitem = item.clone();
        sitem.setItemMeta(Bukkit.getItemFactory().getItemMeta(sitem.getType()));
        sitem.setAmount(1);
        World world = event.getEntity().getWorld();
        String worldname = world.getName();
        if (ConfigHandler.itemList.contains(sitem) && this.plugin.isEnabledIn(worldname)) {
            World w = event.getEntity().getWorld();
            Chunk c = event.getEntity().getLocation().getChunk();
            Map.Entry<UUID, String> entry = new AbstractMap.SimpleEntry<>(w.getUID(), c.toString());
            boolean notinChunk = false;
            List<Location> loc = null;
            loc = blockInfo.get(entry);
            try {
                loc.size();
            } catch (NullPointerException e) {
                notinChunk = true;
            }
            if (!notinChunk)
                for (int i = 0; i < loc.size(); i++) {
                    Hopper hopper = null;
                    boolean retry = false;
                    try {
                        hopper = (Hopper) ((Location) loc.get(i)).getBlock().getState();
                    } catch (ClassCastException e) {
                        File DataFile = new File(this.plugin.getDataFolder() + File.separator +
                                "Data.yml");
                        YamlConfiguration DFile = YamlConfiguration.loadConfiguration(DataFile);
                        String coord = String.valueOf(((Location) loc.get(i)).getBlockX()) + ".0," + ((Location) loc.get(i)).getBlockY() + ".0," + ((Location) loc.get(i)).getBlockZ() + ".0";
                        ((List) blockInfo.get(entry)).remove(loc.get(i));
                        List<String> tmp = DFile.getStringList(worldname);
                        tmp.remove(coord);
                        DFile.set(worldname, tmp);
                        try {
                            DFile.save(this.plugin.getDataFolder() + File.separator +
                                    "Data.yml");
                        } catch (IOException e1) {
                            e.printStackTrace();
                        }
                        retry = true;
                    }
                    if (!retry) {
                        Inventory hopperInv = hopper.getInventory();
                        if (hopperInv.firstEmpty() != -1) {
                            hopperInv.addItem(item);
                            event.getEntity().remove();
                            break;
                        }
                        HashMap<Integer, ItemStack> left = hopperInv.addItem(item);
                        if (!left.isEmpty()) {
                            int a = item.getAmount() - (Integer) left.keySet().toArray()[0];
                            ItemStack i2 = item.clone();
                            i2.setAmount(a);
                            hopperInv.removeItem(i2);
                        } else {
                            event.getEntity().remove();
                        }
                    }
                }
        }
    }
}