package fr.volax.anezia.hooks;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class WorldGuard_6 implements WorldGuardHook {
  public boolean checkLocationBreakFlag(Chunk chunk, Player p) {
    WorldGuardPlugin worldGuardPlugin = (WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
    RegionContainer container = worldGuardPlugin.getRegionContainer();
    Block[] cornerBlocks = { chunk.getBlock(0, chunk.getWorld().getMaxHeight() / 2, 0), chunk.getBlock(0, chunk.getWorld().getMaxHeight() / 2, 15), chunk.getBlock(15, chunk.getWorld().getMaxHeight() / 2, 0), chunk.getBlock(15, chunk.getWorld().getMaxHeight() / 2, 15) };
    for (Block currentBlock : cornerBlocks) {
      RegionQuery query = container.createQuery();
      ApplicableRegionSet set = query.getApplicableRegions(currentBlock.getLocation());
      LocalPlayer localPlayer = worldGuardPlugin.wrapPlayer(p);
      if (set.queryState((RegionAssociable)localPlayer, new StateFlag[] { DefaultFlag.BLOCK_BREAK }) == StateFlag.State.DENY)
        return false; 
    } 
    return true;
  }
}


/* Location:              C:\Users\flo31\Desktop\ChunkBuster.jar!\codes\biscuit\chunkbuster\hooks\WorldGuard_6.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */