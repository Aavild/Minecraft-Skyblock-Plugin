package io.github.aavild;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class IslandManager {
    World skyworld;
    private List<Island> islands = new ArrayList<Island>();
    Biome standardBiome = Biome.FOREST;
    IslandPositionManager islandPositionManager;
    Schematic schematic;
    public void CreateIsland(Player sender)
    {
        for (Island island : islands)
        {
            if (island.members.contains(sender))
            {
                sender.sendMessage("You already have an island");
                return;
            }
        }
        Island island = new Island(standardBiome, sender);
        island.islandLocation = islandPositionManager.location(islands.size(), sender);
        Material[][][] skyblock = null;
        skyblock = schematic.skyblocks().clone();
        int[] size = {skyblock.length, skyblock[0].length, skyblock[0][0].length};
        Location loc = island.islandLocation;
        island.islandHome = loc;
        island.islandHome.setY(loc.getBlockY() - 1);
        for (int i = 0; i < skyblock.length; i++)
        {
            for (int i2 = 0; i2 < skyblock[i].length; i2++)
            {
                for (int i3 = 0; i3 < skyblock[i][i2].length; i3++)
                {
                    Block block = new Location(skyworld, i - size[0] / 2 + loc.getBlockX(), i2 - size[1] / 2 + loc.getBlockY(), i3 - size[2] / 2 + loc.getBlockZ()).getBlock();
                    block.setType(skyblock[i][i2][i3]);
                    if (i == 1 && i2 == 3 && i3 == 0)
                    {
                        BlockData blockData = block.getBlockData();
                        Directional directional = (Directional) blockData;
                        directional.setFacing(BlockFace.SOUTH);
                        block.setBlockData(directional);
                        Chest chest = (Chest)block.getState();
                        Inventory inv = chest.getInventory();
                        ItemStack a = new ItemStack(Material.ICE, 2);
                        ItemStack b = new ItemStack(Material.LAVA_BUCKET, 1);
                        ItemStack c = new ItemStack(Material.PUMPKIN_SEEDS, 1);
                        ItemStack d = new ItemStack(Material.WHEAT_SEEDS, 1);
                        ItemStack e = new ItemStack(Material.MELON_SEEDS, 1);
                        ItemStack f = new ItemStack(Material.BEETROOT_SEEDS, 1);
                        ItemStack g = new ItemStack(Material.CARROT, 1);
                        ItemStack h = new ItemStack(Material.OAK_SAPLING, 1);
                        ItemStack j = new ItemStack(Material.BONE, 3);
                        inv.addItem(a, b, c, d, e, f, g, h, j);
                    }
                }
            }
        }
        sender.teleport(island.islandHome);
        for (Island island1 : islands)
        {
            if (island1 == null)
            {
                islands.set(islands.indexOf(island1), island);
                return;
            }
        }
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
                islands.set(islands.indexOf(island), null);
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
