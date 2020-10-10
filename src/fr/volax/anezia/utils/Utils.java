package fr.volax.anezia.utils;


import fr.volax.anezia.AneziaAddons;
import fr.volax.anezia.nbt.NBTItem;
import fr.volax.anezia.timers.RemovalQueue;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Utils {
    private AneziaAddons main;

    private Set<Chunk> waterChunks = new HashSet<>();

    public Utils(AneziaAddons main) {
        this.main = main;
    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    List<String> colorLore(List<String> lore) {
        List<String> newLore = new ArrayList<>();
        for (String loreLine : lore)
            newLore.add(color(loreLine));
        return newLore;
    }

    ItemStack itemFromString(String rawItem) {
        Material material;
        String[] rawSplit;
        if (rawItem.contains(":")) {
            rawSplit = rawItem.split(":");
        } else {
            rawSplit = new String[] { rawItem };
        }
        try {
            material = Material.valueOf(rawSplit[0]);
        } catch (IllegalArgumentException ex) {
            material = Material.DIRT;
        }
        short damage = 0;
        if (rawSplit.length > 1)
            try {
                damage = Short.valueOf(rawSplit[1]).shortValue();
            } catch (IllegalArgumentException illegalArgumentException) {}
        return new ItemStack(material, 1, damage);
    }

    public void sendMessage(CommandSender p, ConfigValues.Message message, Object... params) {
        if (!this.main.getConfigValues().getMessage(message, params).equals(""))
            p.sendMessage(this.main.getConfigValues().getMessage(message, params));
    }

    private void addGlow(ItemStack item) {
        item.addUnsafeEnchantment(Enchantment.LURE, 1);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        item.setItemMeta(meta);
    }

    public void clearChunks(int chunkBusterArea, Location chunkBusterLocation, Player p) {
        Set<Material> ignoredBlocks = this.main.getConfigValues().getIgnoredBlocks();
        if (chunkBusterArea % 2 != 0) {
            RemovalQueue removalQueue = new RemovalQueue(this.main, p);
            WorldBorder border = chunkBusterLocation.getWorld().getWorldBorder();
            int upperBound = (chunkBusterArea - 1) / 2 + 1;
            int lowerBound = (chunkBusterArea - 1) / -2;
            for (int y = this.main.getConfigValues().getMaximumY(p); y >= this.main.getConfigValues().getMinimumY(p); y--) {
                for (int chunkX = lowerBound; chunkX < upperBound; chunkX++) {
                    for (int chunkZ = lowerBound; chunkZ < upperBound; chunkZ++) {
                        Chunk chunk = chunkBusterLocation.getWorld().getChunkAt(chunkBusterLocation.getChunk().getX() + chunkX, chunkBusterLocation.getChunk().getZ() + chunkZ);
                        Location chunkCheckLoc = chunk.getBlock(7, 60, 7).getLocation();
                        if (this.main.getHookUtils().compareLocToPlayer(chunkCheckLoc, p)) {
                            this.waterChunks.add(chunk);
                            for (int x = 0; x < 16; x++) {
                                for (int z = 0; z < 16; z++) {
                                    Block b = chunk.getBlock(x, y, z);
                                    if (!b.getType().equals(Material.AIR) && !ignoredBlocks.contains(b.getType()) && (
                                            !this.main.getConfigValues().worldborderHookEnabled() || insideBorder(b, border)))
                                        removalQueue.getBlocks().add(b);
                                }
                            }
                        }
                    }
                }
            }
            removalQueue.runTaskTimer((Plugin)this.main, 1L, 1L);
            this.main.getUtils().sendMessage((CommandSender)p, ConfigValues.Message.CLEARING_CHUNKS, new Object[0]);
        } else {
            p.sendMessage(color("&cInvalid chunk buster!"));
        }
    }

    private boolean insideBorder(Block block, WorldBorder border) {
        Location blockLocation = block.getLocation().add(0.5D, 0.0D, 0.5D);
        double x = blockLocation.getX();
        double z = blockLocation.getZ();
        double size = border.getSize() / 2.0D;
        Location center = border.getCenter();
        return (x < center.clone().add(size, 0.0D, 0.0D).getX() && z < center.clone().add(0.0D, 0.0D, size).getZ() && x > center.clone().subtract(size, 0.0D, 0.0D).getX() && z > center.clone().subtract(0.0D, 0.0D, size).getZ());
    }


    public Set<Chunk> getWaterChunks() {
        return this.waterChunks;
    }

    public ItemStack getChunkBusterItem(int giveAmount, int chunkArea) {
        ItemStack item = new ItemStack(this.main.getConfigValues().getChunkBusterMaterial(), giveAmount, this.main.getConfigValues().getChunkBusterDamage());
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(this.main.getConfigValues().getChunkBusterName());
        itemMeta.setLore(this.main.getConfigValues().getChunkBusterLore(chunkArea));
        item.setItemMeta(itemMeta);
        if (this.main.getConfigValues().itemShouldGlow())
            addGlow(item);
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setInteger("chunkbuster.radius", Integer.valueOf(chunkArea));
        return nbtItem.getItem();
    }
}