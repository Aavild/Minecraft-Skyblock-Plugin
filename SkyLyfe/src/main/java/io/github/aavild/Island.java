package io.github.aavild;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Island {
    public Island(Biome biome, Player player)
    {
        this.biome = biome;
        owner = player;
        members.add(player);
    }
    Player owner;
    List<Player> members = new ArrayList<Player>();
    Location islandHome;
    Location islandLocation;
    String IslandName;
    private Biome biome;
    private float level = 0;
    public float GetIslandLevel()
    {
        //update level
        return level;
    }
}
