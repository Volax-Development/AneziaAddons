package fr.volax.anezia.timers;

import java.util.UUID;

import fr.volax.anezia.AneziaAddons;
import fr.volax.anezia.utils.ConfigValues;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class MessageTimer extends BukkitRunnable {
    private int seconds;
    private UUID uuid;
    private AneziaAddons main;

    public MessageTimer(int seconds, UUID uuid, AneziaAddons main) {
        this.seconds = seconds;
        this.uuid = uuid;
        this.main = main;
    }

    public void run() {
        if (this.seconds <= 0) {
            cancel();
            return;
        }
        this.main.getUtils().sendMessage((CommandSender) this.main.getServer().getPlayer(this.uuid), ConfigValues.Message.CLEARING_IN_SECONDS, new Object[]{Integer.valueOf(this.seconds)});
        if (!this.main.getConfigValues().sendWarmupEverySecond()) {
            cancel();
            return;
        }
        this.seconds--;
    }
}