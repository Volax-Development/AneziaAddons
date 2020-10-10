package fr.volax.anezia.utils;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WorldGuardVerifier {
    private boolean b;

    public WorldGuardVerifier(Player player) {
        WorldGuardPlugin worldGuard = getWorldGuard();
        Location location = player.getLocation();
        RegionManager regionManager = worldGuard.getRegionManager(location.getWorld());
        ApplicableRegionSet regionsAtLocation = regionManager.getApplicableRegions(location);
        for (ProtectedRegion region : regionsAtLocation) {
            if (region.getFlag((Flag) DefaultFlag.TNT) != null && (region.getFlag((Flag) DefaultFlag.TNT)).toString().equals("DENY")) {
                this.b = false;
                return;
            }
        }
        this.b = true;
    }

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin))
            return null;
        return (WorldGuardPlugin) plugin;
    }

    public boolean b() {
        return this.b;
    }
}