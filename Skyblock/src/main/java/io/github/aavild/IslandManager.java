package io.github.aavild;

import me.blackvein.quests.Quest;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
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
    List<Player> deleters = new ArrayList<Player>();
    private IslandComperator islandComperator = new IslandComperator();
    IslandProtection islandProtection;
    ChatEventHandler chatEventHandler;

    //Materials and their assigned values for calculation of island value
    List<Material> materials;
    List<Integer> values;
    List<Integer> moneyValues;

    IslandPositionManager islandPositionManager;
    Schematic schematic;
    SkyblockMain main;
    int IslandSize = 129;
    boolean enabledDifferentSizes = false;

    List<Integer> islandprices;

    //Creation / deletion
    void CreateIsland(Player player)
    {
        for (Island island : islands)
        {
            if (island == null)
                continue;
            if (island.members.contains(player.getUniqueId()))
            {
                player.sendMessage("You already have an island");
                return;
            }
        }
        int islandNumber = islands.size();
        List<Integer> ids = new ArrayList<Integer>();
        for(Island island : islands)
        {
            if (island == null)
                continue;
            ids.add(island.id);
        }
        Collections.sort(ids);
        for (int i = 0; i < ids.size(); i++)
        {
            if (i != ids.get(i))
            {
                islandNumber = i;
                break;
            }
        }
        Location loc = islandPositionManager.location(islandNumber);
        Island island = new Island(player, loc, islandNumber);
        //player.sendMessage("Island loc: " + loc);
        islands.add(island);

        Location home = schematic.CreateIsland(skyworld, loc, main);
        island.setHomeLocation(home);
        //main.SaveIslands();
        player.sendMessage(ChatColor.GREEN + "Created island");
        TeleportPlayerHome(player);
        if (main.quests != null) //If it could find the quests plugin
        {
            Quest quest = main.quests.getQuest("CreateIslandQuest"); //Get the quest instance
            quest.completeQuest(main.quests.getQuester(player.getUniqueId())); //Complete the quest
        }
    }
    void DeleteIsland(Player sender)
    {
        Island remove = GetIsland(sender);
        if (!deleters.contains(sender))
            return;
        deleters.remove(sender);
        islands.remove(remove);
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
        //sender.getInventory().clear();
        sender.teleport(standardWorld.getSpawnLocation());
        main.SaveIslands();
        sender.sendMessage(ChatColor.GOLD + "Succesfully deleted your island");
    }
    void ForceRemoveIsland(Player sender, OfflinePlayer target)
    {
        Island remove = null;
        for(Island island : islands)
        {
            if (island == null)
                continue;
            if (island.owner == target.getUniqueId())
                remove = island;
        }
        if (remove == null)
        {
            sender.sendMessage(ChatColor.RED + "Couldn't find the player");
            return;
        }
        islands.remove(remove);
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
            sender.sendMessage(ChatColor.YELLOW + "You have left " + ChatColor.RED + main.getServer().getOfflinePlayer(island.owner).getName() + ChatColor.YELLOW + "'s island");
            if (!sender.performCommand("spawn"))
                sender.setHealth(0);
            Player owner = Bukkit.getServer().getPlayer(island.owner);
            if (owner != null)
                owner.sendMessage(ChatColor.RED + sender.getName() + ChatColor.YELLOW + " left your island");
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
                sender.sendMessage(ChatColor.GREEN + "Set home");
            }
            else
                sender.sendMessage(ChatColor.RED + "You can only set home within your own island");
        }
        else
            sender.sendMessage(ChatColor.RED + "Only the owner can set home");

    }
    void FixHome(Player sender)
    {
        Island island = GetIsland(sender);
        if (island == null)
        {
            sender.sendMessage(ChatColor.YELLOW + "You do not own an island");
            return;
        }
        island.setHomeLocation(schematic.RecoverHome(island.getIslandLocation(skyworld), islandProtection, island.islandsize));
    }

    //Utils
    void SetIslandName (Player sender, String name)
    {
        Island island = GetIsland(sender);
        if (!IsOwner(island, sender))
            return;
        if (name == null)
        {
            sender.sendMessage(ChatColor.YELLOW + "Your islands name have been reset");
            island.IslandName = name;
            main.SaveIslands();
            return;
        }
        name = ChatColor.translateAlternateColorCodes('&', name);
        island.IslandName = name;
        sender.sendMessage(ChatColor.YELLOW + "Sat the island name to: " + ChatColor.WHITE + name);
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

        sender.sendMessage(ChatColor.YELLOW + "Members from your island:");
        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        int u = 1;
        for (UUID uuid : island.members)
        {
            if (uuid == null)
                continue;
            String s = ChatColor.RED + main.getServer().getOfflinePlayer(uuid).getName() + ChatColor.YELLOW + ", ";
            stringBuilder.append(s);
            if ((i / 3) == u)
            {
                if (i == island.members.size())
                {
                    stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
                    sender.sendMessage(stringBuilder.toString());
                    return;
                }
                sender.sendMessage(stringBuilder.toString());
                stringBuilder = new StringBuilder();
                u++;
            }
            i++;
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        sender.sendMessage(stringBuilder.toString());


        /*StringBuilder s = new StringBuilder();
        for (UUID uuid : island.members)
        {
            s.append(Bukkit.getOfflinePlayer(uuid).getName() + ChatColor.GREEN + ", ");
        }
        s.deleteCharAt(s.lastIndexOf(","));
        sender.sendMessage(ChatColor.GREEN + "Players: "+ ChatColor.BLUE + s);*/
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
            sender.sendMessage(ChatColor.YELLOW + "Changed Biome to " + ChatColor.GREEN + biomeString + ChatColor.YELLOW + "\nRelog to see changes");
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
            sender.sendMessage(ChatColor.YELLOW + "Made " + ChatColor.RED + main.getServer().getOfflinePlayer(uuid).getName() + ChatColor.YELLOW + " island owner");
            Player newOwner = Bukkit.getServer().getPlayer(island.owner);
            if (newOwner != null)
                newOwner.sendMessage(ChatColor.RED + sender.getName() + ChatColor.YELLOW + " made you island owner");
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
        if (island.locked)
        {
            sender.sendMessage(ChatColor.YELLOW + "Your island is already locked");
            return;
        }
        island.locked = true;
        for (Player player : main.getServer().getOnlinePlayers())
        {
            if (island.members.contains(player.getUniqueId()))
                continue;
            boolean cooped = false;
            for (IslandCoop coop : coops)
            {
                if (coop.island.equals(island))
                    if (coop.cooped.equals(player.getUniqueId()))
                    {
                        cooped = true;
                        break;
                    }
            }
            if (cooped)
                continue;
            if (islandProtection.WithinIsland(player.getLocation(), island.getIslandLocation(skyworld), island.islandsize))
            {
                if (player.hasPermission("skyblock.admin.bypass.lock"))
                {
                    player.sendMessage(ChatColor.RED + sender.getName() + ChatColor.YELLOW + " tried to suspend you from his island with /is lock");
                    sender.sendMessage(ChatColor.YELLOW + "You cannot suspend " + ChatColor.RED + player.getName() + ChatColor.YELLOW + " from your island");
                    continue;
                }
                player.teleport(standardWorld.getSpawnLocation());
                sender.sendMessage(ChatColor.YELLOW + "Suspended " + ChatColor.RED + player.getName() + ChatColor.YELLOW + " from your island");
                player.sendMessage(ChatColor.GOLD + "You got suspended from " + sender.getName() + "s island");
            }
        }
        sender.sendMessage(ChatColor.GOLD + "Locked your island");
    }
    void UnlockIsland(Player sender)
    {
        Island island = GetIsland(sender);
        if (!IsOwner(island, sender))
            return;
        if (!island.locked)
        {
            sender.sendMessage(ChatColor.YELLOW + "Your island is already unlocked");
            return;
        }
        island.locked = false;
        sender.sendMessage(ChatColor.YELLOW + "Unlocked your island");
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
        {
            sender.sendMessage(ChatColor.RED + "You're not the owner of your island");
            return;
        }
        int cost = islandprices.get(island.islandsize);
        if (cost > main.economy.getBalance(sender))
        {
            sender.sendMessage(ChatColor.YELLOW + "You don't have enough money to rank up your island");
            return;
        }
        main.economy.withdrawPlayer(sender, cost);
        island.islandsize += 1;
        sender.sendMessage(ChatColor.YELLOW + "Increased your island size from " + ChatColor.GREEN + sizes.get(island.islandsize - 1) + "x" + sizes.get(island.islandsize - 1) +
                ChatColor.YELLOW + " to " + ChatColor.GREEN + sizes.get(island.islandsize) + "x" + sizes.get(island.islandsize));
        if (island.islandsize + 1 != sizes.size())
            sender.sendMessage(ChatColor.YELLOW + "Next rank cost: " + ChatColor.GREEN + islandprices.get(island.islandsize) +
                    ChatColor.YELLOW + " with " + ChatColor.GREEN + sizes.get(island.islandsize + 1) + "x" + sizes.get(island.islandsize + 1) + ChatColor.YELLOW + " in size");
        else
            sender.sendMessage(ChatColor.YELLOW + "Your island is now maximum size");
    }
    void UpdateIslandsValue(Island island)
    {
        Location loc = island.getIslandLocation(skyworld);
        int[] value = new int[2];
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
                    {
                        value[0] += values.get(materials.indexOf(block.getType()));
                        value[1] += moneyValues.get(materials.indexOf(block.getType()));
                    }
                }
            }
        }
        island.value = value[0];
        island.moneyValue = value[1];
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
            sender.sendMessage(ChatColor.RED + invited.getName() + ChatColor.YELLOW + " already have an island");
            return;
        }
        sender.sendMessage(ChatColor.YELLOW + "Invited " + ChatColor.RED + invited.getName() + ChatColor.YELLOW + " to your island");
        TextComponent text = new TextComponent("");
        invited.sendMessage(ChatColor.YELLOW + "You have been invited to join " + ChatColor.RED + sender.getName() + ChatColor.YELLOW + "'s island");




        TextComponent message = new TextComponent( "Type " );
        message.setColor( net.md_5.bungee.api.ChatColor.YELLOW );

        TextComponent command = new TextComponent( "/is accept" );
        command.setColor( net.md_5.bungee.api.ChatColor.GREEN );
        command.setUnderlined( true );
        command.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/is accept" ) );
        ComponentBuilder hoverText = new ComponentBuilder( "Click to accept the invite" );
        hoverText.color(net.md_5.bungee.api.ChatColor.GREEN);
        command.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, hoverText.create() ) );
        message.addExtra( command );

        TextComponent message2 = new TextComponent( " to accept the invite or type " );
        message2.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
        message.addExtra( message2);

        TextComponent command2 = new TextComponent( "/is reject" );
        command2.setColor( net.md_5.bungee.api.ChatColor.RED );
        command2.setUnderlined( true );
        command2.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/is reject" ) );
        ComponentBuilder hoverText2 = new ComponentBuilder( "Click to reject the invite" );
        hoverText2.color(net.md_5.bungee.api.ChatColor.RED);
        command2.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, hoverText2.create() ) );
        message.addExtra( command2 );

        TextComponent message3 = new TextComponent( " to accept the invite or type " );
        message3.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
        message.addExtra( message3);

        invited.spigot().sendMessage( message );






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
            sender.sendMessage(ChatColor.YELLOW + "You don't have any incoming invites");
            return;
        }
        invite.island.members.add(sender.getUniqueId());
        sender.sendMessage(ChatColor.YELLOW + "Joined " + ChatColor.RED + main.getServer().getOfflinePlayer(invite.island.owner).getName() + ChatColor.YELLOW +  "'s island");
        sender.teleport(invite.island.getHomeLocation(skyworld));
        Player owner = main.getServer().getPlayer(invite.island.owner);
        if (owner != null)
            owner.sendMessage(ChatColor.RED + sender.getName() + ChatColor.YELLOW + " joined your island");
        invites.remove(invite);
    }
    void RejectInvite(Player sender)
    {
        IslandInvite invite = GetInvite(sender);
        if (invite == null)
        {
            sender.sendMessage(ChatColor.YELLOW + "You don't have any incoming invites");
            return;
        }
        invites.remove(invite);
        sender.sendMessage(ChatColor.YELLOW + "You have rejected the invite to join " + ChatColor.RED + main.getServer().getOfflinePlayer(invite.island.owner).getName() + ChatColor.YELLOW + "'s island");
        Player owner = Bukkit.getServer().getPlayer(invite.island.owner);
        if (owner != null)
            owner.sendMessage(ChatColor.RED + sender.getName() + ChatColor.YELLOW + " rejected your invite to join your island");
    }
    void Kick(Player sender, String target)
    {
        Island island = GetIsland(sender);
        if (island == null)
        {
            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
            return;
        }
        if (island.owner.equals(sender.getUniqueId()))
        {
            OfflinePlayer player = null;
            for (UUID uuid : island.members)
            {
                OfflinePlayer offlinePlayer = main.getServer().getOfflinePlayer(uuid);
                if (offlinePlayer.getName().equalsIgnoreCase(target))
                {
                    player = offlinePlayer;
                    break;
                }
            }
            if (player == null)
            {
                sender.sendMessage(ChatColor.RED + "Couldn't find the player");
                return;
            }
            if (sender.getUniqueId().equals(player.getUniqueId()))
            {
                sender.sendMessage(ChatColor.RED + "Quit trying to kick yourself");
                return;
            }
            sender.sendMessage(ChatColor.YELLOW + "Kicked " + ChatColor.RED + player.getName() + ChatColor.YELLOW + " from your island");
            island.members.remove(player.getUniqueId());
            Player removed = Bukkit.getServer().getPlayer(player.getUniqueId());
            if (removed != null)
                removed.sendMessage(ChatColor.YELLOW + "You have been kicked from " + ChatColor.RED + sender.getName() + ChatColor.YELLOW + "'s island");
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
        if (island.members.contains(cooped.getUniqueId()))
        {
            sender.sendMessage(ChatColor.RED + cooped.getName() + ChatColor.YELLOW + " is a part of your island");
            return;
        }
        UUID coopedID = cooped.getUniqueId();
        for (IslandCoop coop : coops)
        {
            if (coop.cooped.equals(coopedID) && coop.island.equals(island))
            {
                sender.sendMessage(ChatColor.RED + cooped.getName() + ChatColor.YELLOW + " is already cooped to your island");
                return;
            }
        }
        coops.add(new IslandCoop(island, coopedID));
        sender.sendMessage(ChatColor.RED + cooped.getName() + ChatColor.YELLOW + " has been cooped to your island");
        cooped.sendMessage(ChatColor.YELLOW + "You have been cooped to " + ChatColor.RED + sender.getName() + ChatColor.YELLOW + "'s island");
    }
    void RemoveCoop(Player sender, Player cooped)
    {
        Island island = GetIsland(sender);
        if (!IsOwner(island, sender))
            return;
        IslandCoop islandCoop = null;
        for (IslandCoop coop : coops)
        {
            if (coop.cooped.equals(cooped.getUniqueId()) && coop.island.equals(island))
            {
                sender.sendMessage( ChatColor.YELLOW + "You have uncooped " + ChatColor.RED + cooped.getName() + " from your island");
                cooped.sendMessage(ChatColor.YELLOW + "You have been uncooped from " + ChatColor.RED + sender.getName() + ChatColor.YELLOW + "'s island");
                islandCoop = coop;
            }
        }
        if (islandCoop != null)
            coops.remove(islandCoop);
        else
            sender.sendMessage(ChatColor.RED + "That Player is not cooped to your island");
    }
    void ListCoops(Player sender)
    {
        Island island = GetIsland(sender);
        if (island == null)
        {
            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
            return;
        }
        sender.sendMessage(ChatColor.YELLOW + "Players cooped for your island:");
        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        int u = 1;
        int a = 0;
        for (IslandCoop islandCoop : coops)
        {
            a++;
            if (islandCoop == null)
                continue;
            if (!islandCoop.island.equals(island))
                continue;
            String s = ChatColor.RED + main.getServer().getOfflinePlayer(islandCoop.cooped).getName() + ChatColor.YELLOW + ", ";
            stringBuilder.append(s);
            if ((i / 3) == u)
            {
                if (a == coops.size())
                {
                    stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
                    sender.sendMessage(stringBuilder.toString());
                    return;
                }
                sender.sendMessage(stringBuilder.toString());
                stringBuilder = new StringBuilder();
                u++;
            }
            i++;
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        sender.sendMessage(stringBuilder.toString());
    }
    void RemoveAllCoops(Player player)
    {
        Island island = GetIsland(player);
        if (island == null)
            return;
        if (island.owner != player.getUniqueId())
            return;
        List<IslandCoop> remove = new ArrayList<IslandCoop>();
        for (IslandCoop coop : coops)
        {
            if (coop == null)
                continue;
            if (!coop.island.equals(island))
                continue;
            remove.add((coop));
            Player cooped = main.getServer().getPlayer(coop.cooped);
            if (cooped == null)
                continue;
            cooped.sendMessage(ChatColor.YELLOW + "You have been uncooped from " + ChatColor.RED + player.getName() + ChatColor.YELLOW + "'s island");
        }
        for (IslandCoop removeCoop : remove)
            coops.remove(removeCoop);
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
            if (island.members.contains(uuid))
            {
                sender.sendMessage(ChatColor.YELLOW + "You can't ban " + ChatColor.RED + main.getServer().getOfflinePlayer(uuid).getName() + ChatColor.YELLOW + " from your island");
            }
            island.Bans.add(uuid);
            sender.sendMessage(ChatColor.YELLOW + "Banned " + ChatColor.RED + main.getServer().getOfflinePlayer(uuid).getName() + ChatColor.YELLOW + " from your island");
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
                sender.sendMessage(ChatColor.YELLOW + "Unbanned " + ChatColor.RED + main.getServer().getOfflinePlayer(uuid).getName() + ChatColor.YELLOW +  " from your island");
            }
            else
                sender.sendMessage(ChatColor.RED + main.getServer().getOfflinePlayer(uuid).getName() + ChatColor.YELLOW + " is not banned from your island");
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
                if (target.hasPermission("skyblock.is.suspend.ignore"))
                {
                    target.sendMessage(ChatColor.RED + sender.getName() + ChatColor.YELLOW + " tried to suspend you from his island");
                    sender.sendMessage(ChatColor.YELLOW + "You cannot suspend " + ChatColor.RED + target.getName() + ChatColor.YELLOW + " from your island");
                    return;
                }
                target.teleport(standardWorld.getSpawnLocation());
                sender.sendMessage(ChatColor.YELLOW + "Suspended " + ChatColor.RED + target.getName() + ChatColor.YELLOW + " from your island");
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
    void ApplyForDelete(Player sender)
    {
        Island island = GetIsland(sender);
        if (island == null)
        {
            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
            return;
        }

        if (island.owner.equals(sender.getUniqueId()))
        {
            deleters.add(sender);


            TextComponent message = new TextComponent( "Please type " );
            message.setColor( net.md_5.bungee.api.ChatColor.RED );

            TextComponent command = new TextComponent( "/is confirm" );
            command.setColor( net.md_5.bungee.api.ChatColor.DARK_RED );
            command.setUnderlined( true );
            command.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND, "/is confirm" ) );
            ComponentBuilder hoverText = new ComponentBuilder( "Click me to confirm!" );
            hoverText.color(net.md_5.bungee.api.ChatColor.BLUE);
            command.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, hoverText.create() ) );
            message.addExtra( command );

            TextComponent message2 = new TextComponent( " to confirm deletion of your island" );
            message2.setColor(net.md_5.bungee.api.ChatColor.RED);
            message.addExtra( message2);

            sender.spigot().sendMessage( message );



        }
        else
            sender.sendMessage(ChatColor.RED + "You're not the owner of your island");
    }
    void GetBanList(Player sender)
    {
        Island island = GetIsland(sender);
        if (island == null)
        {
            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
            return;
        }
        sender.sendMessage(ChatColor.YELLOW + "Players banned from your island:");
        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
        int u = 1;
        for (UUID uuid : island.Bans)
        {
            if (uuid == null)
                continue;
            String s = ChatColor.RED + main.getServer().getOfflinePlayer(uuid).getName() + ChatColor.YELLOW + ", ";
            stringBuilder.append(s);
            if ((i / 3) == u)
            {
                if (i == island.Bans.size())
                {
                    stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
                    sender.sendMessage(stringBuilder.toString());
                    return;
                }
                sender.sendMessage(stringBuilder.toString());
                stringBuilder = new StringBuilder();
                u++;
            }
            i++;
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        sender.sendMessage(stringBuilder.toString());
    }
    void TeamChat(Player sender)
    {
        if (GetIsland(sender) == null)
        {
            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
            return;
        }
        if (chatEventHandler.players.contains(sender))
        {
            chatEventHandler.players.remove(chatEventHandler.players.indexOf(sender));
            sender.sendMessage(ChatColor.YELLOW + "Disabled teamchat");
        }
        else
        {
            chatEventHandler.players.add(sender);
            sender.sendMessage(ChatColor.YELLOW + "Enabled teamchat");
        }
    }
    void SpyTeamChat(Player sender)
    {
        if (chatEventHandler.adminAbusers.contains(sender))
        {
            chatEventHandler.adminAbusers.remove(chatEventHandler.adminAbusers.indexOf(sender));
            sender.sendMessage(ChatColor.YELLOW + "Disabled spying teamchat");
        }
        else
        {
            chatEventHandler.adminAbusers.add(sender);
            sender.sendMessage(ChatColor.YELLOW + "Enabled spying teamchat");
        }
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
    int GetIslandLevel(Player sender)
    {
        Island island = GetIsland(sender);
        if (island == null)
        {
            return -1;
        }
        UpdateIslandsValue(island);
        return ((int)Math.sqrt(island.value)) / 5;
    }
    int GetIslandValue(Player sender)
    {
        Island island = GetIsland(sender);
        if (island == null)
        {
            return -1;
        }
        UpdateIslandsValue(island);
        return island.value;
    }
    int[] GetValue(Player sender)
    {
        int[] itemValues = new int[2];
        Material material = sender.getInventory().getItemInMainHand().getType();
        if (materials.contains(material))
        {
            itemValues[0] = values.get(materials.indexOf(material));
            itemValues[1] = moneyValues.get(materials.indexOf(material));
        }
        else
        {
            itemValues[0] = -1;
            itemValues[1] = -1;
        }
        return itemValues;
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
            if (island == null)
                continue;
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
            if (s.size() == 10)
                break;
            if (island == null)
                continue;
            if (island.IslandName == null)
                s.add(ChatColor.GREEN +"" + Bukkit.getServer().getOfflinePlayer(island.owner).getName() + ": " + ChatColor.LIGHT_PURPLE + ((int)Math.sqrt(island.value)) / 5);
            else
                s.add(ChatColor.GREEN +"" + island.IslandName + ChatColor.YELLOW + " - Lvl: " + ChatColor.GREEN + ((int)Math.sqrt(island.value)) / 5 + ChatColor.YELLOW + ", Value: " + ChatColor.GREEN + island.value);
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
        List<Island> remove = new ArrayList<Island>();
        for (Island island : sortedislands)
        {
            if (island == null)
                remove.add(island);
        }
        sortedislands.removeAll(remove);
        Collections.sort(sortedislands, islandComperator);
        Collections.reverse(sortedislands);
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
