package io.github.aavild;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WandHandler implements Listener {
    ItemStack wand;
    List<Location[]> markedLocs = new ArrayList<Location[]>();
    List<Player> players = new ArrayList<Player>();
    public WandHandler(WandCreator wandCreator)
    {
        wand = wandCreator.CreateWand();
    }
    @EventHandler
    public void OnInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.equals(wand))
            return;
        Action action = event.getAction();
        if (!(action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_BLOCK)))
            return;


        event.setCancelled(true);
        Location block = event.getClickedBlock().getLocation();
        if (action.equals(Action.LEFT_CLICK_BLOCK))
        {
            int playerindex = GetPlayersIndex(player);
            if (playerindex == -1)
            {
                players.add(player);
                Location[] loc = new Location[2];
                loc[0] = block;
                markedLocs.add(loc);
                player.sendMessage(ChatColor.GOLD + "Marked Block");
                return;
            }
            Location[] loc = markedLocs.get(playerindex);
            if (loc[0] != null)
                if (loc[0].equals(block))
                    return;
            loc[0] = block;
            player.sendMessage(ChatColor.GOLD + "Marked Block");
        }
        else
        {
            int playerindex = GetPlayersIndex(player);
            if (playerindex == -1)
            {
                players.add(player);
                Location[] loc = new Location[2];
                loc[1] = event.getClickedBlock().getLocation();
                markedLocs.add(loc);
                player.sendMessage(ChatColor.YELLOW + "Marked Block");
                return;
            }
            Location[] loc = markedLocs.get(playerindex);
            if (loc[1] != null)
                if (loc[1].equals(block))
                    return;
            loc[1] = block;
            player.sendMessage(ChatColor.YELLOW + "Marked Block");
        }
    }
    int GetPlayersIndex(Player player)
    {
        if (players.contains(player))
            return players.indexOf(player);
        return -1;
    }
    Location[] GetLocation(Player player)
    {
        int index = GetPlayersIndex(player);
        if (index == -1)
            return null;
        return markedLocs.get(index);
    }
}
