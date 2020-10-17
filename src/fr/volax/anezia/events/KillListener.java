package fr.volax.anezia.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class KillListener implements Listener {
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        System.out.println(event.getEntity().getKiller());
        if(event.getEntity().getKiller() == null){
            event.setDeathMessage("§c"+event.getEntity().getPlayer().getName()+" vient de se faire décapiter");
        }else{
            if(event.getEntity().getKiller().getItemInHand().getType() == Material.AIR) event.setDeathMessage("§c"+event.getEntity().getPlayer().getName()+" vient de se faire décapiter par "+event.getEntity().getKiller().getName()+" avec sa main");
            else{
                if(event.getEntity().getKiller().getItemInHand().getItemMeta().getDisplayName() == null) event.setDeathMessage("§c"+event.getEntity().getPlayer().getName()+" vient de se faire décapiter par "+event.getEntity().getKiller().getName()+" avec "+event.getEntity().getKiller().getItemInHand().getType());
                else event.setDeathMessage("§c"+event.getEntity().getPlayer().getName()+" vient de se faire décapiter par "+event.getEntity().getKiller().getName());
            }
        }
    }
}
