package fr.volax.anezia.events;

import fr.volax.anezia.utils.DynamiteItem;
import fr.volax.anezia.utils.ValidItem;
import fr.volax.anezia.utils.WorldGuardVerifier;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class DynamiteEvents implements Listener {
    @EventHandler
    public void interactEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!(new ValidItem(e.getItem())).b())
            return;
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && (
                new WorldGuardVerifier(player)).b() && e.getItem().getType() == (new DynamiteItem()).type() &&
                e.getItem().getItemMeta().getDisplayName().equals((new DynamiteItem()).name())) {
            e.setCancelled(true);
            if (e.getItem().getAmount() == 1) {
                player.setItemInHand(new ItemStack(Material.AIR));
            } else {
                ItemStack t = e.getItem();
                t.setAmount(e.getItem().getAmount() - 1);
                player.setItemInHand(t);
            }
            TNTPrimed tnt = e.getPlayer().getWorld().spawn(e.getPlayer().getLocation(), TNTPrimed.class);
            tnt.setFuseTicks(50);
            tnt.setVelocity(e.getPlayer().getLocation().getDirection().multiply(0.85D));
        }
    }
}