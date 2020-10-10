package fr.volax.anezia.events;

import fr.volax.anezia.AneziaAddons;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class FinderUseEvents implements Listener {
    private AneziaAddons l;

    public FinderUseEvents(AneziaAddons l) {
        this.l = l;
    }

    @EventHandler
    public void useEvent(PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName() ||
                item.getType() != this.l.getItem().getType() ||
                item.getData().getData() != this.l.getItem().getData().getData() ||
                !item.getItemMeta().getDisplayName().equals(this.l.getItem().getItemMeta().getDisplayName()))
            return;
        Player player = e.getPlayer();
        int amount = (player.getLocation().getChunk().getTileEntities()).length;
        if (amount > this.l.getMaxAmount())
            amount = this.l.getMaxAmount();
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        NBTTagCompound itemCompound = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
        int i = itemCompound.getInt("wacfinder-durability") - 1;
        player.sendMessage(this.l.getUseMessage().replaceFirst("%AMOUNT%", String.valueOf(amount)).replaceFirst("%DURABILITY%", String.valueOf(i)));
        if (i == 0) {
            player.setItemInHand(new ItemStack(Material.AIR));
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 100.0F, 100.0F);
            return;
        }
        itemCompound.set("wacfinder-durability", (NBTBase) new NBTTagInt(i));
        nmsItem.setTag(itemCompound);
        player.setItemInHand(CraftItemStack.asBukkitCopy(nmsItem));
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 100.0F, i);
    }
}