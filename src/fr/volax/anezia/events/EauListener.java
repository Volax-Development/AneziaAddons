package fr.volax.anezia.events;

import java.util.HashMap;

import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EauListener implements Listener {
    private final HashMap<Player, Boolean> playersCreepers = new HashMap<Player, Boolean>();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() == null || e.getItem().getType() == Material.AIR)
            return;
        if (e.getItem().getType() == Material.MONSTER_EGG && e.getItem().getItemMeta().getDisplayName().equals("§c§l✸ §6§lCreeper d'Eau §c§l✸")) {
            Player player = e.getPlayer();
            this.playersCreepers.put(player, true);
        }
    }

    @EventHandler
    public void onCreatureSpawn(EntityTargetEvent e) {
        if (e.getEntity() instanceof Creeper &&
                e.getTarget() instanceof Player &&
                this.playersCreepers.containsKey(e.getTarget())) {
            Creeper creeper = (Creeper) e.getEntity();
            creeper.setPowered(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Creeper &&
                entity.getName().equals("§c§l✸ §6§lCreeper d'Eau §c§l✸") &&
                e.getCause() == EntityDamageEvent.DamageCause.DROWNING)
            e.setCancelled(true);
    }

    @EventHandler
    public void onExplodeWater(EntityExplodeEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Creeper &&
                entity.getName().equals("§c§l✸ §6§lCreeper d'Eau §c§l✸")) {
            explodeWater(entity.getLocation());
            customizeCreeper((Creeper) entity);
        }
    }

    private void explodeWater(Location location) {
        int r = 2;
        for (int x = r * -1; x <= r; x++) {
            for (int y = r * -1; y <= r; y++) {
                for (int z = r * -1; z <= r; z++) {
                    Block block = location.getWorld().getBlockAt(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
                    if (block.getType().equals(Material.WATER) || block.getType().equals(Material.STATIONARY_WATER))
                        block.setType(Material.AIR);
                }
            }
        }
    }

    private void customizeCreeper(Creeper creeper) {
        net.minecraft.server.v1_8_R3.Entity nms = ((CraftEntity) creeper).getHandle();
        NBTTagCompound nbttag = new NBTTagCompound();
        nms.c(nbttag);
        nbttag.setInt("ExplosionPower", 1);
        EntityLiving livingcreeper = (EntityLiving) nms;
        livingcreeper.a(nbttag);
    }
}