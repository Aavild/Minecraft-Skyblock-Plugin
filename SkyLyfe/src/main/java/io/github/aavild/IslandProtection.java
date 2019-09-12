package io.github.aavild;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;

public class IslandProtection implements Listener {
    SkyLyfeMain main;
    World skyworld;
    IslandManager islandManager;
    int islandsize = 129;
    int islandTotalSize = 134;
    boolean enabledDifferentSizes = false;
    List<Integer> islandSizes;
    @EventHandler
    public void OnInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if (player.getLocation().getWorld() != skyworld)
            return;
        Action action = event.getAction();
        Location loc;
        if (action.equals(Action.RIGHT_CLICK_BLOCK) || action.equals(Action.LEFT_CLICK_BLOCK))
        {
            if (action.equals(Action.RIGHT_CLICK_BLOCK))
                loc = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation();
            else
                loc = event.getClickedBlock().getLocation();
        }
        else
            return;

        Location islandloc = GetIslandLocation(loc);
        Island island = islandManager.GetIslandFromLoc(islandloc);
        if (island == null)
        {
            //player.sendMessage("no island found");
            event.setCancelled(true);
            return;
        }

        //player.sendMessage("" + WithinIsland(loc, islandloc, islandSizes.get(island.islandsize)));
        if (island.members.contains(player.getUniqueId()))
        {
            if (!WithinIsland(loc, islandloc, island.islandsize))
                event.setCancelled(true);
        }
        else
        {
            for (IslandCoop coop : islandManager.coops)
            {
                if (coop.island.equals(island) && coop.cooped.equals(player))
                    if (WithinIsland(loc, islandloc, island.islandsize))
                    {
                        event.setCancelled(false);
                        return;
                    }
            }
            player.sendMessage("not in coop");
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        event.setCancelled(HandleTheMovement(event.getTo(), event.getPlayer(), event.getFrom()));
    }
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        event.setCancelled(HandleTheMovement(event.getTo(), event.getPlayer(), event.getFrom()));
    }
    private boolean HandleTheMovement(Location gotoloc, Player player, Location gofromloc)
    {
        //Island fromIsland;
        Island toIsland;
        if (gotoloc != null)
        {
            if (gotoloc.getWorld().equals(skyworld))
            {
                Location islandloc = GetIslandLocation(gotoloc);
                toIsland = islandManager.GetIslandFromLoc(islandloc);
                if (toIsland != null)
                {
                    if (!WithinIsland(gotoloc,toIsland.getIslandLocation(skyworld), toIsland.islandsize)) //if the player doesnt go within an island
                        toIsland = null;
                    else
                    {
                        if (toIsland.members.contains(player.getUniqueId())) //if the island the player enters is his own
                        {
                            //player.sendMessage(WithinIsland(gofromloc, toIsland.getIslandLocation(skyworld), size)+"");
                            if (!WithinIsland(gofromloc, toIsland.getIslandLocation(skyworld), toIsland.islandsize)) //if where the player came from is outside the island
                            {
                                try
                                {
                                    HotbarMessager.sendHotBarMessage(player, ChatColor.GREEN + "Welcome back");
                                }
                                catch (Exception e)
                                {
                                    main.getServer().getConsoleSender().sendMessage("[SkyL] Something went wrong with sending hotbar message");
                                }
                            }
                        }
                        else //the player enters an island that isnt his
                        {
                            if (toIsland.locked || toIsland.Bans.contains(player.getUniqueId())) //if its locked or the player is banned
                            {
                                try
                                {
                                    HotbarMessager.sendHotBarMessage(player, ChatColor.RED + "You're not allowed to enter this island");
                                }
                                catch (Exception e)
                                {
                                    main.getServer().getConsoleSender().sendMessage("[SkyL] Something went wrong with sending hotbar message");
                                }
                                return true;
                            }
                            else //if its not locked
                            {
                                try
                                {
                                    HotbarMessager.sendHotBarMessage(player, ChatColor.GREEN + "Welcome to " + ChatColor.BLUE + toIsland.IslandName);
                                }
                                catch (Exception e)
                                {
                                    main.getServer().getConsoleSender().sendMessage("[SkyL] Something went wrong with sending hotbar message");
                                }
                            }
                        }
                    }
                }
                if (toIsland == null)
                {
                    Location islandloc2 = GetIslandLocation(gofromloc);
                    Island fromisland = islandManager.GetIslandFromLoc(islandloc2);
                    if (fromisland != null)
                    {
                        if (WithinIsland(gofromloc, fromisland.getIslandLocation(skyworld), fromisland.islandsize)) //if the player comes from within an island
                        {
                            if (fromisland.members.contains(player.getUniqueId()))
                            {
                                try
                                {
                                    HotbarMessager.sendHotBarMessage(player, ChatColor.RED + "You left your island");
                                }
                                catch (Exception e)
                                {
                                    main.getServer().getConsoleSender().sendMessage("[SkyL] Something went wrong with sending hotbar message");
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    boolean WithinIsland(Location loc, Location islandLocation, int islandsize)
    {
        int size;
        if (enabledDifferentSizes)
            size = islandSizes.get(islandsize);
        else
            size = this.islandsize;
        //if location is within islandlocation's x
        if (loc.getBlockX() >= islandLocation.getBlockX() - size / 2 && loc.getBlockX() <= islandLocation.getBlockX() + (size - 1) / 2)
        {
            //if location is within islandlocation's z
            return (loc.getBlockZ() >= islandLocation.getBlockZ() - size / 2 && loc.getBlockZ() <= islandLocation.getBlockZ() + (size - 1) / 2);
        }
        return false;
    }
    private Location GetIslandLocation(Location loc)
    {
        //main.getServer().broadcastMessage("" + CalculateCenterForAxis(loc.getBlockX()) + CalculateCenterForAxis(loc.getBlockZ()));
        return new Location(skyworld, CalculateCenterForAxis(loc.getBlockX()), 64, CalculateCenterForAxis(loc.getBlockZ()));
    }
    private int CalculateCenterForAxis(int i)
    {
        if (i > 0)
        {
            return (i - 1) / islandTotalSize * islandTotalSize + islandTotalSize / 2;
        }
        else if (i < 0)
        {
            return (i + 1) / islandTotalSize * islandTotalSize - islandTotalSize / 2;
        }
        return 0;
    }
}
