package fr.volax.anezia.events;

import java.util.regex.Pattern;

import fr.volax.anezia.AneziaAddons;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class OtherEvents implements Listener {
    private AneziaAddons main;

    private int version = -1;

    public OtherEvents(AneziaAddons main) {
        this.main = main;
    }

    @EventHandler
    public void onWaterFlow(BlockFromToEvent e) {
        if (this.version == -1)
            this.version = Integer.parseInt(Bukkit.getBukkitVersion().split(Pattern.quote("-"))[0].split(Pattern.quote("."))[1]);
        if (this.version >= 13) {
            if ((e.getBlock().getType().equals(Material.WATER) || e.getBlock().getType().equals(Material.LAVA)) &&
                    !this.main.getUtils().getWaterChunks().contains(e.getBlock().getChunk()) && this.main.getUtils().getWaterChunks().contains(e.getToBlock().getChunk()))
                e.setCancelled(true);
        } else if ((e.getBlock().getType().equals(Material.WATER) || e.getBlock().getType().equals(Material.valueOf("STATIONARY_WATER")) || e
                .getBlock().getType().equals(Material.LAVA) || e.getBlock().getType().equals(Material.valueOf("STATIONARY_LAVA"))) &&
                !this.main.getUtils().getWaterChunks().contains(e.getBlock().getChunk()) && this.main.getUtils().getWaterChunks().contains(e.getToBlock().getChunk())) {
            e.setCancelled(true);
        }
    }
}