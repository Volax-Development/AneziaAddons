package fr.volax.anezia.hooks;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;

public interface WorldGuardHook {
    boolean checkLocationBreakFlag(Chunk paramChunk, Player paramPlayer);
}