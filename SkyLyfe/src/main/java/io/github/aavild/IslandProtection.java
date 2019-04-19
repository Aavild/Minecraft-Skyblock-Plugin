package io.github.aavild;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class IslandProtection implements Listener {
    World skyworld;
    IslandManager islandManager;
    int islandsize = 129;
    @EventHandler
    public void OnInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        Location playerloc = player.getLocation();
        if (playerloc.getWorld() != skyworld)
            return;
        /*if (player.hasPermission("buildanywhere"))
            return;*/
        Action action = event.getAction();
        /*if (action.equals(Action.PHYSICAL))
            return;*/
        Location loc;
        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_BLOCK))
        {
            if (action.equals(Action.RIGHT_CLICK_BLOCK))
                loc = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation();
            else
                loc = event.getClickedBlock().getLocation();
        }
        else
        {
            loc = playerloc;
        }
        Location islandLocation = islandManager.GetPlayerIslandLocation(player);
        if (islandLocation == null)
        {
            event.setCancelled(true);
            return;
        }
        int xDistance = Math.abs(islandLocation.getBlockX() - loc.getBlockX());
        int zDistance = Math.abs(islandLocation.getBlockZ() - loc.getBlockZ());
        int xMaxDistance = islandsize / 2;
        int zMaxDistance = islandsize / 2;
        if (islandsize % 2 == 0)
        {
            if (islandLocation.getBlockX() - loc.getBlockX() < 0)
            {
                xMaxDistance -= 1;
            }
            if (islandLocation.getBlockZ() - loc.getBlockZ() < 0)
            {
                zMaxDistance -= 1;
            }
        }
        if (xDistance <= xMaxDistance && zDistance <= zMaxDistance)
            return;
        event.setCancelled(true);
    }
}
