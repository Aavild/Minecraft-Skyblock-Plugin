package io.github.aavild;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import sun.management.Sensor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IslandManager {
    World skyworld;
    List<Island> islands;
    List<IslandCoopInvite> invites = new ArrayList<IslandCoopInvite>();
    Biome standardBiome = Biome.FOREST;
    IslandPositionManager islandPositionManager;
    Schematic schematic;
    SkyLyfeMain main;
    int IslandSize = 129;
    public void CreateIsland(Player sender)
    {
        int islandNumber = islands.size();
        for (Island island : islands)
        {
            if (island == null)
            {
                islandNumber = islands.indexOf(null);
                continue;
            }
            if (island.members.contains(sender.getUniqueId()))
            {
                sender.sendMessage("You already have an island");
                return;
            }
        }
        Location loc = islandPositionManager.location(islandNumber, sender);
        loc.setY(loc.getBlockY() - 1);
        Island island = new Island(standardBiome, sender, loc);
        loc.setY(loc.getBlockY() + 1);
        Material[][][] skyblock;
        skyblock = schematic.skyblocks().clone();
        int[] size = {skyblock.length, skyblock[0].length, skyblock[0][0].length};
        for (int i = 0; i < skyblock.length; i++)
        {
            for (int i2 = 0; i2 < skyblock[i].length; i2++)
            {
                for (int i3 = 0; i3 < skyblock[i][i2].length; i3++)
                {
                    Block block = new Location(skyworld, i - size[0] / 2 + loc.getBlockX(), i2 - size[1] / 2 + loc.getBlockY(), i3 - size[2] / 2 + loc.getBlockZ()).getBlock();
                    block.setType(skyblock[i][i2][i3]);
                    block.setBiome(standardBiome);
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
        sender.teleport(island.getHomeLocation(skyworld));

        if (islands.contains(null))
            islands.set(islands.indexOf(null), island);
        else
            islands.add(island);

        main.Save();
        sender.sendMessage(ChatColor.GREEN + "Created island");
    }
    public void DeleteIsland(Player sender)
    {
        Island remove = null;
        for (Island island : islands)
        {
            if (island == null)
                continue;
            if (island.owner.equals(sender.getUniqueId()))
            {
                remove = island;
                islands.set(islands.indexOf(island), null);
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "You're not the owner of your island");
                return;
            }
        }
        if (remove != null)
        {
            islands.remove(remove);
            Location loc = remove.getIslandLocation(skyworld);
            for (int i = 0; i < IslandSize + IslandSize % 2; i++)
            {
                for (int i2 = 0; i2 < 257; i2++)
                {
                    for (int i3 = 0; i3 < IslandSize + IslandSize % 2; i3++)
                    {
                        Block block = new Location(skyworld, i - IslandSize / 2 - IslandSize % 2 + loc.getBlockX(), i2, i3 - IslandSize / 2 - IslandSize % 2 + loc.getBlockZ()).getBlock();
                        if (block.getType().equals(Material.CHEST))
                        {
                            BlockState bs = block.getState();
                            ((Chest) bs).getInventory().clear();
                        }
                        block.setType(Material.AIR);
                    }
                }
            }
            sender.getInventory().clear();
            sender.setHealth(0);
        }
        else
        {
            sender.sendMessage(ChatColor.YELLOW + "You're not a member of an island");
        }
        main.Save();
    }
    public float GetIslandLevel(Player sender)
    {
        for (Island island : islands)
        {
            if (island == null)
                continue;
            if (island.members.contains(sender.getUniqueId()))
            {
                Location loc = island.getIslandLocation(skyworld);
                float level = 0;
                for (int i = 0; i < IslandSize; i++)
                {
                    for (int i2 = 0; i2 < 257; i2++)
                    {
                        for (int i3 = 0; i3 < IslandSize; i3++)
                        {
                            Block block = new Location(skyworld, i - IslandSize / 2 + loc.getBlockX(), i2, i3 - IslandSize / 2 + loc.getBlockZ()).getBlock();
                            if (!block.getType().equals(Material.AIR))
                                level +=1;
                        }
                    }
                }
                island.UpdateLevel(level);
                return level;
            }
        }
        sender.sendMessage(ChatColor.RED + "You're not a member of an island");
        return -1;
    }
    public void TeleportPlayerHome(Player sender)
    {
        for (Island island : islands)
        {
            if (island == null)
                continue;
            if (island.members.contains(sender.getUniqueId()))
            {
                sender.teleport(island.getHomeLocation(skyworld));
                return;
            }
        }
        sender.sendMessage(ChatColor.RED + "You're not a member of an island");
    }
    public void SetHome(Player sender)
    {
        for (Island island : islands)
        {
            if (island == null)
                continue;
            if (island.members.contains(sender.getUniqueId()))
            {
                if (island.owner.equals(sender.getUniqueId()))
                {
                    island.setHomeLocation(sender.getLocation());
                    main.Save();
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
            if (island == null)
                continue;
            if (island.members.contains(sender.getUniqueId()))
            {
                if (island.owner.equals(sender.getUniqueId()))
                {
                    island.IslandName = name;
                    main.Save();
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
    public Location GetPlayerIslandLocation(Player player)
    {
        for (Island island : islands)
        {
            if (island == null)
                continue;
            if (island.members.contains(player.getUniqueId()))
                return island.getIslandLocation(skyworld);
        }
        return null;
    }
    void CoopPlayer(Player sender, Player cooped)
    {
        for (Island island : islands)
        {
            if (island == null)
                continue;
            if (island.members.contains(sender.getUniqueId()))
            {
                if (island.owner.equals(sender.getUniqueId()))
                {
                    for (Island island1 : islands)
                    {
                        if (island1.members.contains(cooped.getUniqueId()))
                        {
                            sender.sendMessage(ChatColor.RED + "That player is already a member of an island");
                            return;
                        }
                        sender.sendMessage(ChatColor.BLUE + cooped.getName() + ChatColor.GREEN + " has been invited to join your island");
                        cooped.sendMessage(ChatColor.GREEN + "You have been invited to join " + ChatColor.BLUE + sender.getName() + "'" + ChatColor.GREEN + " island");
                        for (IslandCoopInvite invite : invites)
                        {
                            if (invite.player.equals(cooped))
                            {
                                invite.island = island;
                                return;
                            }
                        }
                        invites.add(new IslandCoopInvite(cooped, island));
                    }
                    return;
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "You're not the owner of your island");
                    return;
                }

            }
        }
        sender.sendMessage(ChatColor.RED + "You're not a member of an island");
    }
    void AcceptCoop(Player sender)
    {
        IslandCoopInvite remove = null;
        for (IslandCoopInvite invite : invites)
        {
            if (invite == null)
                continue;
            if (invite.player.equals(sender))
            {
                invite.island.members.add(sender.getUniqueId());
                sender.sendMessage(ChatColor.GREEN + "Accepted the invite\n" + ChatColor.YELLOW + "Teleporting...");
                sender.teleport(invite.island.getHomeLocation(skyworld));
                Player owner = Bukkit.getServer().getPlayer(invite.island.owner);
                if (owner != null)
                    owner.sendMessage(ChatColor.BLUE + sender.getName() + ChatColor.YELLOW + " accepted your invite");
                remove = invite;
            }
        }
        if (remove != null)
        {
            invites.remove(remove);
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "You don't have an incoming invites");
        }
    }
    void RejectCoop(Player sender)
    {
        IslandCoopInvite remove = null;
        for (IslandCoopInvite invite : invites)
        {
            if (invite == null)
                continue;
            if (invite.player.equals(sender))
            {
                remove = invite;
                Player owner = Bukkit.getServer().getPlayer(invite.island.owner);
                if (owner != null)
                    owner.sendMessage(ChatColor.BLUE + sender.getName() + ChatColor.YELLOW + " declined your invite");
            }
        }
        if (remove != null)
        {
            invites.remove(remove);
            sender.sendMessage(ChatColor.YELLOW + "Rejected the invite...");
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "You don't have an incoming invites");
        }
    }
    void ListMembers(Player sender)
    {
        for (Island island : islands)
        {
            if (island == null)
                continue;
            if (island.members.contains(sender.getUniqueId()))
            {
                StringBuilder s = new StringBuilder();
                for (UUID uuid : island.members)
                {
                    s.append(ChatColor.BLUE + Bukkit.getOfflinePlayer(uuid).getName() + ChatColor.GREEN + ", ");
                }
                s.deleteCharAt(s.lastIndexOf(","));
                sender.sendMessage(ChatColor.GREEN + "Players: " + s);
                return;
            }
        }
        sender.sendMessage(ChatColor.RED + "You're not a member of an island");
    }
    void Leave(Player sender)
    {
        for (Island island : islands)
        {
            if (island == null)
                continue;
            if (island.members.contains(sender.getUniqueId()))
            {
                if (island.owner.equals(sender.getUniqueId()))
                {
                    sender.sendMessage(ChatColor.YELLOW + "You're the owner of your island, please use /is delete if you want to delete your island");
                }
                else
                {
                    island.members.remove(sender.getUniqueId());
                    sender.getInventory().clear();
                    sender.setHealth(0);
                    sender.sendMessage(ChatColor.RED + "You have left your island");
                    Player owner = Bukkit.getServer().getPlayer(island.owner);
                    if (owner != null)
                        owner.sendMessage(ChatColor.BLUE + sender.getName() + ChatColor.YELLOW + " declined your invite");
                }
                return;
            }
        }
        sender.sendMessage(ChatColor.RED + "You're not a member of an island");
    }
    void SetBiome(Player sender, String biomeString)
    {
        Location loc = null;
        Island island2 = null;
        for (Island island : islands)
        {
            if (island == null)
                continue;
            if (island.members.contains(sender.getUniqueId()))
            {
                if (island.owner.equals(sender.getUniqueId()))
                {
                    island2 = island;
                    loc = island.getIslandLocation(skyworld);
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "You're not the owner of your island");
                }
            }
        }
        if (loc == null)
        {
            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
            return;
        }
        Biome biome;
        try
        {
            biome = Biome.valueOf(biomeString.toUpperCase());
            sender.sendMessage(ChatColor.GREEN + "Changing Biome to: " + ChatColor.BLUE + biomeString);
        }
        catch (IllegalArgumentException e)
        {
            sender.sendMessage(ChatColor.YELLOW + biomeString + ChatColor.RED + " is not a biome");
            return;
        }
        island2.SetBiome(biome);
        for (int i = 0; i < IslandSize + IslandSize % 2; i++)
        {
            for (int i2 = 0; i2 < 257; i2++)
            {
                for (int i3 = 0; i3 < IslandSize + IslandSize % 2; i3++)
                {
                    Block block = new Location(skyworld, i - IslandSize / 2 - IslandSize % 2 + loc.getBlockX(), i2, i3 - IslandSize / 2 - IslandSize % 2 + loc.getBlockZ()).getBlock();
                    block.setBiome(biome);
                }
            }
        }
    }
    List<String> IslandTop(Player sender)
    {
        List<String> s = new ArrayList<String>();
        for (Island island : islands)
        {
            if (island == null)
                continue;
            island.GetLevel();
            s.add(ChatColor.GREEN +"" + Bukkit.getServer().getOfflinePlayer(island.owner).getName() + ": " + ChatColor.LIGHT_PURPLE + island.GetLevel());
        }
        if (s.size() == 1 && s.get(0) == null)
        {
            sender.sendMessage(ChatColor.YELLOW + "There's currently no islands");
            return null;
        }
        return s;
    }
    List<String> GetMembers(Player sender)
    {
        List<String> members = new ArrayList<String>();
        for (Island island : islands)
        {
            if (island.members.contains(sender.getUniqueId()))
            {
                for (UUID uuid : island.members)
                {
                    members.add(ChatColor.LIGHT_PURPLE + main.getServer().getOfflinePlayer(uuid).getName());
                }
            }
        }
        return members;
    }
}
