package io.github.aavild;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Island implements Serializable {
    UUID owner;
    List<UUID> members = new ArrayList<UUID>();
    List<UUID> Bans = new ArrayList<UUID>();

    //Location islandHome;
    private double[] islandHomeCoords = new double[3];
    private float[] islandHomeDirect = new float[2];

    //Location islandLocation;
    private double[] islandLocationCoords = new double[3];

    String IslandName;
    private Biome biome;
    private int level = 0;
    boolean locked = false;

    public Island(Biome biome, Player player, Location islandHome, Location location)
    {
        this.biome = biome;
        owner = player.getUniqueId();
        members.add(player.getUniqueId());
        setHomeLocation(islandHome);

        islandLocationCoords[0] = location.getBlockX();
        islandLocationCoords[1] = location.getBlockY();
        islandLocationCoords[2] = location.getBlockZ();
    }
    public void UpdateLevel(int level)
    {
        this.level = level;
    }
    public int GetLevel()
    {
        return level;
    }
    public Location getHomeLocation(World world)
    {
        return new Location(world, islandHomeCoords[0], islandHomeCoords[1], islandHomeCoords[2], islandHomeDirect[0], islandHomeDirect[1]);
    }
    public void setHomeLocation(Location loc)
    {
        islandHomeCoords[0] = loc.getX();
        islandHomeCoords[1] = loc.getY();
        islandHomeCoords[2] = loc.getZ();
        islandHomeDirect[0] = loc.getYaw();
        islandHomeDirect[1] = loc.getPitch();
    }
    public Location getIslandLocation(World world)
    {
        return new Location(world, islandLocationCoords[0], islandLocationCoords[1], islandLocationCoords[2]);
    }
    public Biome GetBiome()
    {
        return biome;
    }
    public void SetBiome(Biome biome)
    {
        this.biome = biome;
    }
}
