package fr.volax.anezia.hooks;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Role;
import fr.volax.anezia.AneziaAddons;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class FactionsUUIDHook {
    private AneziaAddons main;

    FactionsUUIDHook(AneziaAddons main) {
        this.main = main;
    }

    boolean hasFaction(Player p) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(p);
        return fPlayer.hasFaction();
    }

    boolean isWilderness(Location loc) {
        FLocation fLoc = new FLocation(loc);
        Faction fLocFaction = Board.getInstance().getFactionAt(fLoc);
        return fLocFaction.isWilderness();
    }

    boolean compareLocPlayerFaction(Location loc, Player p) {
        Faction locFaction = Board.getInstance().getFactionAt(new FLocation(loc));
        Faction pFaction = FPlayers.getInstance().getByPlayer(p).getFaction();
        return (locFaction.equals(pFaction) || (locFaction.isWilderness() && this.main.getConfigValues().canPlaceInWilderness()));
    }

    boolean checkRole(Player p, String role) {
        Role adminRole, playerRole = FPlayers.getInstance().getByPlayer(p).getRole();
        if (playerRole == null)
            return false;
        try {
            adminRole = Role.valueOf("ADMIN");
        } catch (Exception ex) {
            try {
                adminRole = Role.valueOf("LEADER");
            } catch (Exception ex2) {
                return false;
            }
        }
        Role coLeader = null;
        try {
            coLeader = Role.valueOf("COLEADER");
        } catch (Exception exception) {
        }
        switch (role) {
            case "leader":
            case "admin":
                if (playerRole.equals(adminRole))
                    return true;
                break;
            case "coleader":
            case "co-leader":
                if (playerRole.equals(coLeader) || playerRole.equals(adminRole))
                    return true;
                break;
            case "moderator":
                if (playerRole.equals(Role.MODERATOR) || playerRole.equals(coLeader) || playerRole.equals(adminRole))
                    return true;
                break;
            case "member":
            case "normal":
                if (playerRole.equals(Role.NORMAL) || playerRole.equals(Role.MODERATOR) || playerRole.equals(coLeader) || playerRole.equals(adminRole))
                    return true;
                break;
            case "recruit":
            case "any":
                return true;
        }
        return false;
    }
}