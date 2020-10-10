package fr.volax.anezia.timers;

import java.util.LinkedList;

import fr.volax.anezia.AneziaAddons;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RemovalQueue extends BukkitRunnable {
    private AneziaAddons main;
    private LinkedList<Block> blocks = new LinkedList<>();
    private Player p;

    public RemovalQueue(AneziaAddons main, Player p) {
        this.main = main;
        this.p = p;
    }

    public void run() {
        for (int count = 0; count < this.main.getConfigValues().getBlockPerTick() &&
                !this.blocks.isEmpty(); count++) {
            Block b = this.blocks.getFirst();
            if (!b.getType().equals(Material.AIR)) {
                b.setType(Material.AIR);
            } else {
                count--;
            }
            this.blocks.removeFirst();
        }
        if (this.blocks.isEmpty())
            cancel();
    }

    public LinkedList<Block> getBlocks() {
        return this.blocks;
    }
}