package fr.volax.anezia.hooks;

import java.util.EnumMap;
import java.util.Map;

import fr.volax.anezia.AneziaAddons;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

public class HookUtils {
    private Map<HookType, Object> enabledHooks = new EnumMap<>(HookType.class);

    private AneziaAddons main;

    public HookUtils(AneziaAddons main) {
        this.main = main;
        PluginManager pm = main.getServer().getPluginManager();
        if (pm.getPlugin("Factions") != null) {
            main.getLogger().info("Hooked into FactionsUUID/SavageFactions");
            this.enabledHooks.put(HookType.FACTIONSUUID, new FactionsUUIDHook(main));
        }
        if (pm.getPlugin("WorldGuard") != null) {
            String pluginVersion = main.getServer().getPluginManager().getPlugin("WorldGuard").getDescription().getVersion();
            if (pluginVersion.startsWith("6") && pm.getPlugin("WorldEdit") != null) {
                this.enabledHooks.put(HookType.WORLDGUARD, new WorldGuard_6());
                main.getLogger().info("Hooked into WorldGuard 6");
            }
        }
    }

    public boolean hasFaction(Player p) {
        if (this.main.getConfigValues().factionsHookEnabled() && this.enabledHooks.containsKey(HookType.FACTIONSUUID)) {
            FactionsUUIDHook factionsUUIDHook = (FactionsUUIDHook) this.enabledHooks.get(HookType.FACTIONSUUID);
            return factionsUUIDHook.hasFaction(p);
        }
        return true;
    }

    public boolean isWilderness(Location loc) {
        if (this.main.getConfigValues().factionsHookEnabled() && this.enabledHooks.containsKey(HookType.FACTIONSUUID)) {
            FactionsUUIDHook factionsUUIDHook = (FactionsUUIDHook) this.enabledHooks.get(HookType.FACTIONSUUID);
            return factionsUUIDHook.isWilderness(loc);
        }
        return false;
    }

    public boolean compareLocToPlayer(Location loc, Player p) {
        boolean canBuild = true;
        if (this.main.getConfigValues().factionsHookEnabled() && this.enabledHooks.containsKey(HookType.FACTIONSUUID)) {
            FactionsUUIDHook factionsUUIDHook = (FactionsUUIDHook) this.enabledHooks.get(HookType.FACTIONSUUID);
            if (!factionsUUIDHook.compareLocPlayerFaction(loc, p))
                canBuild = false;
        }
        if (this.main.getConfigValues().worldguardHookEnabled() && this.enabledHooks.containsKey(HookType.WORLDGUARD)) {
            WorldGuardHook worldGuardHook = (WorldGuardHook) this.enabledHooks.get(HookType.WORLDGUARD);
            if (!worldGuardHook.checkLocationBreakFlag(loc.getChunk(), p))
                canBuild = false;
        }
        return canBuild;
    }

    public boolean checkRole(Player p) {
        if (this.main.getConfigValues().factionsHookEnabled() && this.enabledHooks.containsKey(HookType.FACTIONSUUID)) {
            FactionsUUIDHook factionsUUIDHook = (FactionsUUIDHook) this.enabledHooks.get(HookType.FACTIONSUUID);
            return factionsUUIDHook.checkRole(p, this.main.getConfigValues().getMinimumRole());
        }
        return true;
    }

    public enum HookType {
        FACTIONSUUID, WORLDGUARD;
    }
}