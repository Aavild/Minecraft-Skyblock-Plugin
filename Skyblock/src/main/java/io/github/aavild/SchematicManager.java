package io.github.aavild;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SchematicManager{
    SkyblockMain main;
    Block[][][] CreateSchematic (Location loc1, Location loc2, String name, Location player)
    {
        int xDif = Math.abs(loc1.getBlockX() - loc2.getBlockX());
        int yDif = Math.abs(loc1.getBlockY() - loc2.getBlockY());
        int zDif = Math.abs(loc1.getBlockZ() - loc2.getBlockZ());

        Block[][][] island = new Block[xDif + 1][yDif + 1][zDif + 1];

        int xMin = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int yMin = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int zMin = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        World world = loc1.getWorld();

        for (int i = 0; i < xDif + 1; i++)
        {
            for (int i2 = 0; i2 < yDif + 1; i2++)
            {
                for (int i3 = 0; i3 < zDif + 1; i3++)
                {
                    Location blockLoc = new Location(world,xMin + i, yMin + i2, zMin + i3);
                    island[i][i2][i3] = blockLoc.getBlock();
                }
            }
        }

        Schematic schematic = new Schematic(island, player);
        main.SaveSchematic(schematic, name);
        return island;
    }
}
