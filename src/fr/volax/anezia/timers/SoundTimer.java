package fr.volax.anezia.timers;

import fr.volax.anezia.AneziaAddons;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SoundTimer extends BukkitRunnable {
    private AneziaAddons main;
    private Player p;
    private int count;

    public SoundTimer(AneziaAddons main, Player p, int count) {
        this.main = main;
        this.p = p;
        this.count = count;
    }

    public void run() {
        if (this.p != null)
            this.p.playSound(this.p.getLocation(), this.main.getConfigValues().getWarmupSoundString(), this.main.getConfigValues().getWarmupSoundVolume(), this.main.getConfigValues().getWarmupSoundPitch());
        this.count--;
        if (this.count <= 0)
            cancel();
    }
}