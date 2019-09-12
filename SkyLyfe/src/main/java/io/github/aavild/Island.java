package io.github.aavild;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Island implements Serializable {
    UUID owner;
    List<UUID> members = new ArrayList<UUID>();
    List<UUID> Bans = new ArrayList<UUID>();

    //islandHome
    private double[] islandHomeCoords = new double[3];
    private float[] islandHomeDirect = new float[2];

    //islandLocation
    private double[] islandLocationCoords = new double[3];

    //Island Display Name
    String IslandName = null;
    //Island value for comparison with other islands, level is calculated from this too
    int value = 0;
    //locked is whether other players than the owner and members can enter the island
    boolean locked = false;
    //Settings is minor changes for what people can do on the island
    boolean[] settings = new boolean[54];
    //islandsize from 0 - infinity determining how big an area a players island is
    int islandsize = 0;

    Island(Player player, Location islandHome, Location location)
    {
        owner = player.getUniqueId();
        members.add(player.getUniqueId());
        setHomeLocation(islandHome);

        islandLocationCoords[0] = location.getBlockX();
        islandLocationCoords[1] = location.getBlockY();
        islandLocationCoords[2] = location.getBlockZ();
    }
    Location getHomeLocation(World world)
    {
        return new Location(world, islandHomeCoords[0], islandHomeCoords[1], islandHomeCoords[2], islandHomeDirect[0], islandHomeDirect[1]);
    }
    void setHomeLocation(Location loc)
    {
        islandHomeCoords[0] = loc.getX();
        islandHomeCoords[1] = loc.getY();
        islandHomeCoords[2] = loc.getZ();
        islandHomeDirect[0] = loc.getYaw();
        islandHomeDirect[1] = loc.getPitch();
    }
    Location getIslandLocation(World world)
    {
        return new Location(world, islandLocationCoords[0], islandLocationCoords[1], islandLocationCoords[2]);
    }
}
