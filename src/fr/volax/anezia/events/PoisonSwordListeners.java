package fr.volax.anezia.events;

import fr.volax.anezia.AneziaAddons;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class PoisonSwordListeners implements Listener {
    private AneziaAddons main;
    
    public PoisonSwordListeners(AneziaAddons aneziaAddons) {
        this.main = aneziaAddons;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        System.out.println();
        if(!(event.getEntity() instanceof Player)) return;
        if(!(event.getDamager() instanceof Player)) return;

        final Player damager = (Player) event.getDamager();
        final Player damaged = (Player) event.getEntity();
        final ItemStack itemStack = damager.getItemInHand();

        if(itemStack.getType() == null) return;
        if(itemStack.getType() == Material.DIAMOND_SWORD && itemStack.getItemMeta().getDisplayName().equals("§c§l✸ §6§lPoison Sword §c§l✸")){
            UUID uuid = damager.getUniqueId();
            if(this.main.poisonSword.containsKey(uuid)){
                int time = (int) ((System.currentTimeMillis() - this.main.poisonSword.get(uuid)) / 1000);
                if(time < 20){
                    damager.sendMessage("§cTu dois encore attendre " + ( 20 - time) + " seconde(s) avant de pouvoir donner poison à quelqu'un !");
                    return;
                }else {
                    damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1, true));
                    damaged.sendMessage("§aTu viens de te faire attaquer par une Poison Sword !");
                    damager.sendMessage("§aTu viens d'attaquer une personne avec ta Poison Sword !");
                    this.main.poisonSword.put(uuid, System.currentTimeMillis());
                }
            }else{
                damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1, true));
                damaged.sendMessage("§aTu viens de te faire attaquer par une Poison Sword !");
                damager.sendMessage("§aTu viens d'attaquer une personne avec ta Poison Sword !");
                this.main.poisonSword.put(uuid, System.currentTimeMillis());
            }
        }
    }
}
