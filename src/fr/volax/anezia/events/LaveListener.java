package fr.volax.anezia.events;

import java.lang.reflect.Field;
import java.util.HashMap;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreeper;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class LaveListener implements Listener {
    private HashMap<Player, Boolean> playersCreepers = new HashMap<Player, Boolean>();

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getItem() == null || e.getItem().getType() == Material.AIR)
            return;
        if (e.getItem().getType() == Material.MONSTER_EGG && e.getItem().getItemMeta().getDisplayName().equals("§d✿ §6Creeper de §nlave")) {
            Player player = e.getPlayer();
            this.playersCreepers.put(player, Boolean.valueOf(true));
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
                entity.getName().equals("§d✿ §6Creeper de §nlave") && (
                e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.LAVA || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK))
            e.setCancelled(true);
    }

    @EventHandler
    public void onExplodeWater(EntityExplodeEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Creeper &&
                entity.getName().equals("§d✿ §6Creeper de §nlave")) {
            explodeLava(entity.getLocation());
            customizeCreeper((Creeper) entity, 2);
        }
    }

    private void explodeLava(Location location) {
        int r = 2;
        for (int x = r * -1; x <= r; x++) {
            for (int y = r * -1; y <= r; y++) {
                for (int z = r * -1; z <= r; z++) {
                    Block block = location.getWorld().getBlockAt(location.getBlockX() + x, location.getBlockY() + y, location.getBlockZ() + z);
                    if (block.getType().equals(Material.LAVA) || block.getType().equals(Material.STATIONARY_LAVA))
                        block.setType(Material.AIR);
                }
            }
        }
    }

    private void customizeCreeper(Creeper creeper, int radius) {
        EntityCreeper entCreeper = ((CraftCreeper) creeper).getHandle();
        Field radiusF = null;
        try {
            radiusF = EntityCreeper.class.getDeclaredField("explosionRadius");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        radiusF.setAccessible(true);
        try {
            radiusF.setInt(entCreeper, radius);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}