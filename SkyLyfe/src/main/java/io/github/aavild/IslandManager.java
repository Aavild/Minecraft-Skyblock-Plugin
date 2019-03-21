package io.github.aavild;

import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class IslandManager {
    private List<Island> islands = new ArrayList<Island>();
    Biome standardBiome = Biome.FOREST;
    public void CreateIsland(Player sender)
    {
        Island island = new Island(standardBiome, sender);
        islands.add(island);
    }
    public void DeleteIsland(Player sender)
    {
        List<Island> Remove = new ArrayList<Island>();
        for (Island island : islands)
        {
            if (island.owner.equals(sender))
            {
                Remove.add(island);
            }
        }
        for (Island island : Remove)
        {
            islands.remove(island);
        }
    }
    public float GetIslandLevel(Player sender)
    {
        for (Island island : islands)
        {
            if (island.members.contains(sender))
            {
                return island.GetIslandLevel();
            }
        }
        return -1;
    }
    public void TeleportPlayerHome(Player sender)
    {
        for (Island island : islands)
        {
            if (island.members.contains(sender))
            {
                sender.teleport(island.islandHome);
                return;
            }
        }
        sender.sendMessage(ChatColor.RED + "You're not a member of an island");
    }
    public void SetHome(Player sender)
    {
        for (Island island : islands)
        {
            if (island.members.contains(sender))
            {
                if (island.owner.equals(sender))
                {
                    island.islandHome = sender.getLocation();
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "You're not the owner of your island");
                }
                return;
            }
        }
        sender.sendMessage(ChatColor.RED + "You're not a member of an island");
    }
    public void SetIslandName (Player sender, String name)
    {
        for (Island island : islands)
        {
            if (island.members.contains(sender))
            {
                if (island.owner.equals(sender))
                {
                    island.IslandName = name;
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "You're not the owner of your island");
                }
                return;
            }
        }
        sender.sendMessage(ChatColor.RED + "You're not a member of an island");
    }
}
