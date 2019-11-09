package io.github.aavild;

import me.blackvein.quests.Quest;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class IslandManager {
    World skyworld;
    World standardWorld;
    List<Island> islands;
    List<IslandInvite> invites = new ArrayList<IslandInvite>();
    List<IslandCoop> coops = new ArrayList<IslandCoop>();
    List<Integer> sizes;
    private IslandComperator islandComperator = new IslandComperator();
    IslandProtection islandProtection;

    //Materials and their assigned values for calculation of island value
    List<Material> materials;
    List<Integer> values;

    IslandPositionManager islandPositionManager;
    Schematic schematic;
    SkyLyfeMain main;
    int IslandSize = 129;
    boolean enabledDifferentSizes = false;

    List<Integer> islandprices;

    //Creation / deletion
    void CreateIsland(Player sender)
    {
        /*if (enabledDifferentSizes)
        {
            if (islandprices.get(0) > 0 && main.economy != null)
            {
                if(!(main.economy.getBalance(sender) > islandprices.get(0)))
                {
                    sender.sendMessage(ChatColor.RED + "You don't have enough to buy an island\n" + ChatColor.YELLOW + "It costs " + islandprices.get(0) + " to buy an island");
                    return;
                }
                main.economy.withdrawPlayer(sender, islandprices.get(0));
            }
        }*/
        int islandNumber = islands.size();
        for (Island island : islands)
        {
            if (island == null)
            {
                islandNumber = islands.indexOf(null);
                continue;
            }
            /*if (island.members.contains(sender.getUniqueId()))
            {
                sender.sendMessage("You already have an island");
                return;
            }*/
        }
        Location loc = islandPositionManager.location(islandNumber);
        Location home = schematic.CreateIsland(skyworld, loc, main);
        Island island = new Island(sender, home, loc);
        if (islands.contains(null))
            islands.set(islands.indexOf(null), island);
        else
            islands.add(island);

        main.SaveIslands();
        sender.sendMessage(ChatColor.GREEN + "Created island");
        TeleportPlayerHome(sender);
        if (main.quests != null) //If it could find the quests plugin
        {
            Quest quest = main.quests.getQuest("CreateIslandQuest"); //Get the quest instance
            quest.completeQuest(main.quests.getQuester(sender.getUniqueId())); //Complete the quest
        }
    }
    void DeleteIsland(Player sender)
    {
        Island remove = GetIsland(sender);
        if (!IsOwner(remove, sender))
            return;

        islands.set(islands.indexOf(remove), null);
        Location loc = remove.getIslandLocation(skyworld);
        for (int i = 0; i < IslandSize + IslandSize % 2; i++)
        {
            for (int i2 = 0; i2 < 256; i2++)
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
        sender.teleport(standardWorld.getSpawnLocation());
        main.SaveIslands();
    }
    void Leave(Player sender)
    {
        Island island = GetIsland(sender);
        if (island == null)
        {
            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
            return;
        }
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
                owner.sendMessage(ChatColor.BLUE + sender.getName() + ChatColor.YELLOW + " left your island");
        }
    }

    //Teleportation
    void TeleportPlayerHome(Player sender)
    {
        Island island = GetIsland(sender);
        if (island == null)
        {
            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
            return;
        }
        //sender.sendMessage("" + island.getIslandLocation(skyworld));
        sender.teleport(island.getHomeLocation(skyworld));
    }
    void SetHome(Player sender)
    {
        Island island = GetIsland(sender);
        if (IsOwner(island, sender))
        {
            if (islandProtection.WithinIsland(sender.getLocation(), island.getIslandLocation(skyworld), island.islandsize))
            {
                island.setHomeLocation(sender.getLocation());
                main.SaveIslands();
                sender.sendMessage(ChatColor.GREEN + "Sat home");
            }
            else
                sender.sendMessage(ChatColor.RED + "You can only set home within your own island");
        }
        else
            sender.sendMessage(ChatColor.RED + "Only the owner can set home");

    }

    //Utils
    void SetIslandName (Player sender, String name)
    {
        Island island = GetIsland(sender);
        if (!IsOwner(island, sender))
            return;
        island.IslandName = name;
        if (name == null)
            sender.sendMessage(ChatColor.GOLD + "Resat your islands name");
        else
            sender.sendMessage(ChatColor.GOLD + "Set the island name to " + name);
        main.SaveIslands();
    }
    void ListMembers(Player sender)
    {
        Island island = GetIsland(sender);
        if (island == null)
        {
            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
            return;
        }
        StringBuilder s = new StringBuilder();
        for (UUID uuid : island.members)
        {
            s.append(Bukkit.getOfflinePlayer(uuid).getName() + ChatColor.GREEN + ", ");
        }
        s.deleteCharAt(s.lastIndexOf(","));
        sender.sendMessage(ChatColor.GREEN + "Players: "+ ChatColor.BLUE + s);
    }
    void SetBiome(Player sender, String biomeString)
    {
        Island island = GetIsland(sender);
        if (!IsOwner(island, sender))
            return;
        Location loc = island.getIslandLocation(skyworld);
        Biome biome;
        try
        {
            biome = Biome.valueOf(biomeString.toUpperCase());
            sender.sendMessage(ChatColor.GREEN + "Changed Biome to " + ChatColor.BLUE + biomeString + ChatColor.YELLOW + "\n Relog to see changes");
        }
        catch (IllegalArgumentException e)
        {
            sender.sendMessage(ChatColor.YELLOW + biomeString + ChatColor.RED + " is not a biome" + ChatColor.YELLOW +
                    "\n please refer to minecraft wiki for exact names and use " + ChatColor.GREEN + "_ " + ChatColor.YELLOW + "instead of spaces in the name");
            return;
        }
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
    void MakeLeader(Player sender, UUID uuid)
    {
        Island island = GetIsland(sender);
        if (!IsOwner(island, sender))
            return;
        if (island.members.contains(uuid))
        {
            island.owner = uuid;
            sender.sendMessage(ChatColor.GREEN + "Made " + main.getServer().getOfflinePlayer(uuid).getName() + " owner");
            Player newOwner = Bukkit.getServer().getPlayer(island.owner);
            if (newOwner != null)
                newOwner.sendMessage(ChatColor.BLUE + sender.getName() + ChatColor.YELLOW + " have made you owner");
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "That player isn't a part of your island");
        }
    }
    void LockIsland(Player sender)
    {
        Island island = GetIsland(sender);
        if (!IsOwner(island, sender))
            return;
        island.locked = true;
        sender.sendMessage(ChatColor.GOLD + "Locked your island");
    }
    void UnlockIsland(Player sender)
    {
        Island island = GetIsland(sender);
        if (!IsOwner(island, sender))
            return;
        island.locked = false;
        sender.sendMessage(ChatColor.GOLD + "Unlocked your island");
    }
    void RankUpIsland(Player sender)
    {
        if (!enabledDifferentSizes)
        {
            sender.sendMessage(ChatColor.RED + "Different sizes is currently disabled");
            return;
        }
        Island island = GetIsland(sender);
        if (!IsOwner(island, sender))
            return;
        int cost = islandprices.get(island.islandsize + 1);
        if (cost > main.economy.getBalance(sender))
        {
            sender.sendMessage(ChatColor.YELLOW + "You don't have enough money to rank up your island");
            return;
        }
        sender.sendMessage(ChatColor.BLUE + "Increased your island size");
        main.economy.withdrawPlayer(sender, cost);
        island.islandsize += 1;
    }
    void UpdateIslandsValue(Island island)
    {
        Location loc = island.getIslandLocation(skyworld);
        int value = 0;
        int size;
        if (enabledDifferentSizes)
        {
            size = sizes.get(island.islandsize);
        }
        else
            size = IslandSize;
        for (int i = 0; i < size; i++)
        {
            for (int i2 = 0; i2 < 256; i2++)
            {
                for (int i3 = 0; i3 < size; i3++)
                {
                    Block block = new Location(skyworld, i - IslandSize / 2 + loc.getBlockX(), i2, i3 - IslandSize / 2 + loc.getBlockZ()).getBlock();
                    if (materials.contains(block.getType()))
                        value += values.get(materials.indexOf(block.getType()));
                }
            }
        }
        island.value = value;
    }



    //IslandCoop / Bans
    void InvitePlayer(Player sender, Player invited)
    {
        Island island = GetIsland(sender);
        if (!IsOwner(island, sender))
            return;
        Island island1 = GetIsland(invited);
        if (island1 != null)
        {
            sender.sendMessage(ChatColor.RED + "That player is already a member of an island");
            return;
        }
        sender.sendMessage(ChatColor.BLUE + invited.getName() + ChatColor.GREEN + " has been invited to join your island");
        invited.sendMessage(ChatColor.GREEN + "You have been invited to join " + ChatColor.BLUE + sender.getName() + "'" + ChatColor.GREEN + " island");
        for (IslandInvite invite : invites)
        {
            if (invite.player.equals(invited))
            {
                invite.island = island;
                return;
            }
        }
        invites.add(new IslandInvite(invited, island));
    }
    void AcceptInvite(Player sender)
    {
        IslandInvite invite = GetInvite(sender);
        if (invite == null)
        {
            sender.sendMessage(ChatColor.RED + "You don't have any incoming invites");
            return;
        }
        invite.island.members.add(sender.getUniqueId());
        sender.sendMessage(ChatColor.GREEN + "Accepted the invite");
        sender.teleport(invite.island.getHomeLocation(skyworld));
        Player owner = main.getServer().getPlayer(invite.island.owner);
        if (owner != null)
            owner.sendMessage(ChatColor.BLUE + sender.getName() + ChatColor.YELLOW + " accepted your invite");
        invites.remove(invite);
    }
    void RejectInvite(Player sender)
    {
        IslandInvite invite = GetInvite(sender);
        if (invite == null)
        {
            sender.sendMessage(ChatColor.RED + "You don't have any incoming invites");
            return;
        }
        invites.remove(invite);
        sender.sendMessage(ChatColor.YELLOW + "Rejected the invite...");
        Player owner = Bukkit.getServer().getPlayer(invite.island.owner);
        if (owner != null)
            owner.sendMessage(ChatColor.BLUE + sender.getName() + ChatColor.YELLOW + " declined your invite");
    }
    void Kick(Player sender, Player target)
    {
        Island island = GetIsland(sender);
        if (island == null)
        {
            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
            return;
        }
        if (island.owner.equals(sender.getUniqueId()))
        {
            if (island.members.contains(target.getUniqueId()))
            {
                if (sender.getUniqueId().equals(target.getUniqueId()))
                {
                    sender.sendMessage(ChatColor.RED + "Quit trying to kick yourself, moron");
                    return;
                }
                sender.sendMessage(ChatColor.GREEN + "Removed " + target.getName() + " from your island");
                island.members.remove(target.getUniqueId());
                Player removed = Bukkit.getServer().getPlayer(island.owner);
                if (removed != null)
                    removed.sendMessage(ChatColor.BLUE + sender.getName() + ChatColor.YELLOW + " have kicked you from the island");
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "That player isn't a part of your island");
            }
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "You're not the owner of your island");
        }
    }
    void CoopPlayer(Player sender, Player cooped)
    {
        Island island = GetIsland(sender);
        if (!IsOwner(island, sender))
            return;
        for (IslandCoop coop : coops)
        {
            if (coop.cooped.equals(cooped) && coop.island.equals(island))
            {
                sender.sendMessage("That player is already cooped to your island");
                return;
            }
        }
        coops.add(new IslandCoop(island, cooped));
        sender.sendMessage(ChatColor.BLUE + cooped.getName() + ChatColor.GREEN + " has been cooped to your island");
        cooped.sendMessage(ChatColor.GREEN + "You have been cooped to " + ChatColor.BLUE + sender.getName() + "'" + ChatColor.GREEN + " island");
    }
    void RemoveCoop(Player sender, Player cooped)
    {
        Island island = GetIsland(sender);
        if (!IsOwner(island, sender))
            return;
        IslandCoop islandCoop = null;
        for (IslandCoop coop : coops)
        {
            if (coop.cooped.equals(cooped) && coop.island.equals(island))
            {
                sender.sendMessage(ChatColor.BLUE + cooped.getName() + ChatColor.GREEN + " has been uncooped from your island");
                cooped.sendMessage(ChatColor.GREEN + "You have been uncooped from " + ChatColor.BLUE + sender.getName() + "'s" + ChatColor.GREEN + " island");
                islandCoop = coop;
            }
        }
        if (islandCoop != null)
            coops.remove(islandCoop);
        else
            sender.sendMessage(ChatColor.RED + "That Player is not cooped to your island");
    }
    void Ban(Player sender, UUID uuid)
    {
        Island island = GetIsland(sender);
        if (island == null)
        {
            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
            return;
        }
        if (island.owner.equals(sender.getUniqueId()))
        {
            if (sender.getUniqueId().equals(uuid))
            {
                sender.sendMessage(ChatColor.RED + "You can't ban yourself");
                return;
            }
            island.Bans.add(uuid);
            sender.sendMessage(ChatColor.GOLD + "Banned the player from your island");
        }
        else
            sender.sendMessage(ChatColor.RED + "You're not the owner of your island");
    }
    void UnBan(Player sender, UUID uuid)
    {
        Island island = GetIsland(sender);
        if (island == null)
        {
            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
            return;
        }
        if (island.owner.equals(sender.getUniqueId()))
        {
            if (island.Bans.contains(uuid))
            {
                island.Bans.remove(uuid);
                sender.sendMessage(ChatColor.GREEN + "Unbanned the player from your island");
            }
            else
                sender.sendMessage("Couldn't find that player");
        }
        else
            sender.sendMessage(ChatColor.RED + "You're not the owner of your island");
    }
    void Suspend(Player sender, Player target)
    {
        Island island = GetIsland(sender);
        if (island == null)
        {
            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
            return;
        }
        if (island.owner.equals(sender.getUniqueId()))
        {
            if (islandProtection.WithinIsland(target.getLocation(), island.getIslandLocation(skyworld), island.islandsize))
            {
                target.teleport(standardWorld.getSpawnLocation());
                sender.sendMessage(ChatColor.GREEN + "Suspended the player from your island");
                target.sendMessage(ChatColor.GOLD + "You got suspended from " + sender.getName() + "s island");
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "That player is not on your island");
            }
        }
        else
            sender.sendMessage(ChatColor.RED + "You're not the owner of your island");
    }

    //Non-voids
    boolean HasInvite(Player sender) {return GetInvite(sender) != null; }
    boolean IsOwner(Island island, Player sender)
    {
        if (island == null)
        {
            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
            return false;
        }
        if (!(island.owner.equals(sender.getUniqueId())))
        {
            sender.sendMessage(ChatColor.RED + "You're not the owner of your island");
            return false;
        }
        return true;
    }
    Island GetIsland (Player player)
    {
        for (Island island : islands)
        {
            if (island == null)
                continue;
            if (island.members.contains(player.getUniqueId()))
            {
                return island;
            }
        }
        return null;
    }
    IslandInvite GetInvite(Player player)
    {
        for (IslandInvite invite : invites)
        {
            if (invite == null)
                continue;
            if (invite.player.equals(player))
                return invite;
        }
        return null;
    }
    float GetIslandLevel(Player sender)
    {
        Island island = GetIsland(sender);
        if (island == null)
        {
            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
            return -1;
        }
        UpdateIslandsValue(island);
        return ((int)Math.sqrt(island.value)) / 5;
    }
    int GetValue(Player sender)
    {
        Material material = sender.getInventory().getItemInMainHand().getType();
        if (materials.contains(material))
            return values.get(materials.indexOf(material));
        else
            return -1;
    }
    Location GetPlayerIslandLocation(Player sender)
    {
        Island island = GetIsland(sender);
        if (island == null)
            return null;
        return island.getIslandLocation(skyworld);
    }
    Island GetIslandFromLoc(Location loc)
    {
        for (Island island : islands)
        {
            if (loc.equals(island.getIslandLocation(skyworld)))
                return island;
        }
        return null;
    }
    List<String> IslandTop(Player sender)
    {
        List<Island> sortedislands = GetTopTen();
        List<String> s = new ArrayList<String>();
        for (Island island : sortedislands)
        {
            if (island == null)
                continue;
            s.add(ChatColor.GREEN +"" + Bukkit.getServer().getOfflinePlayer(island.owner).getName() + ": " + ChatColor.LIGHT_PURPLE + ((int)Math.sqrt(island.value)) / 5);
        }
        if (s.size() == 1 && s.get(0) == null)
        {
            sender.sendMessage(ChatColor.YELLOW + "There's currently no islands");
            return null;
        }
        return s;
    }
    List<Island> GetTopTen()
    {
        List<Island> sortedislands = islands;
        Collections.sort(sortedislands, islandComperator);
        if (sortedislands.size() > 10)
            sortedislands = sortedislands.subList(0, 9);
        return sortedislands;
    }
    List<String> GetMembers(Player sender)
    {
        List<String> members = new ArrayList<String>();
        Island island = GetIsland(sender);
        if (island == null)
            return null;
        for (UUID uuid : island.members)
        {
            members.add(ChatColor.LIGHT_PURPLE + main.getServer().getOfflinePlayer(uuid).getName());
        }
        return members;
    }
}
