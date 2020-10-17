package fr.volax.anezia.timers;

import fr.volax.anezia.commands.HLRCommandMain;
import fr.volax.anezia.utils.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Timer {
    public static int taskid;

    public void main(final Player player) {
        taskid = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("HLR"), () -> {
            HLRCommandMain.start.put(player, false);
            player.sendMessage(ConfigHandler.prefix + ConfigHandler.msgMap.get("Timer.CanUseConvertCmd"));
        }, ConfigHandler.time);
    }
}