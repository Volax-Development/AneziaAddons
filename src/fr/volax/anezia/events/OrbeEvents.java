package fr.volax.anezia.events;

import java.util.HashMap;
import java.util.Map;

import fr.volax.anezia.AneziaAddons;
import fr.volax.anezia.utils.OrbeType;
import fr.volax.anezia.utils.OrbeVerifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OrbeEvents implements Listener {
    private final AneziaAddons plugin;

    private final Map<String, Boolean> fire;
    private final Map<String, Boolean> speed;
    private final Map<String, Boolean> force;

    public OrbeEvents(AneziaAddons main) {
        this.fire = new HashMap<>();
        this.speed = new HashMap<>();
        this.force = new HashMap<>();
        this.plugin = main;
    }

    @EventHandler
    public void fallEvent(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && e.getCause() == EntityDamageEvent.DamageCause.FALL && (new OrbeVerifier((Player) e.getEntity(), OrbeType.FALL)).b())
            e.setCancelled(true);
    }

    @EventHandler
    public void click(final InventoryClickEvent e) {
        Bukkit.getScheduler().runTaskLater((Plugin) this.plugin, new Runnable() {
            public void run() {
                Player player = (Player) e.getWhoClicked();
                boolean fires = (new OrbeVerifier(player, OrbeType.FIRE)).b();
                if (OrbeEvents.this.fire.containsKey(player.getName()) && OrbeEvents.this.fire.get(player.getName()) && !fires) {
                    player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
                } else if (fires) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2147483647, 0));
                }
                OrbeEvents.this.fire.put(player.getName(), fires);
                boolean speeds = (new OrbeVerifier(player, OrbeType.SPEED)).b();
                if (OrbeEvents.this.speed.containsKey(player.getName()) && OrbeEvents.this.speed.get(player.getName()) && !speeds) {
                    player.removePotionEffect(PotionEffectType.SPEED);
                } else if (speeds) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 1));
                }
                OrbeEvents.this.speed.put(player.getName(), speeds);
                boolean forces = (new OrbeVerifier(player, OrbeType.FORCE)).b();
                if (OrbeEvents.this.force.containsKey(player.getName()) && OrbeEvents.this.force.get(player.getName()) && !forces) {
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                } else if (forces) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, 0));
                }
                OrbeEvents.this.force.put(player.getName(), forces);
            }
        }, 5L);
    }

    @EventHandler
    public void changeslot(PlayerItemHeldEvent e) {
        boolean fires = (new OrbeVerifier(e.getPlayer(), OrbeType.FIRE)).b();
        if (this.fire.containsKey(e.getPlayer().getName()) && this.fire.get(e.getPlayer().getName()) && !fires) {
            e.getPlayer().removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        } else if (fires) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 2147483647, 0));
        }
        this.fire.put(e.getPlayer().getName(), fires);
        boolean speeds = (new OrbeVerifier(e.getPlayer(), OrbeType.SPEED)).b();
        if (this.speed.containsKey(e.getPlayer().getName()) && this.speed.get(e.getPlayer().getName()) && !speeds) {
            e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
        } else if (speeds) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2147483647, 1));
        }
        this.speed.put(e.getPlayer().getName(), speeds);
        boolean forces = (new OrbeVerifier(e.getPlayer(), OrbeType.FORCE)).b();
        if (this.force.containsKey(e.getPlayer().getName()) && this.force.get(e.getPlayer().getName()) && !forces) {
            e.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        } else if (forces) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, 0));
        }
        this.force.put(e.getPlayer().getName(), forces);
    }
}