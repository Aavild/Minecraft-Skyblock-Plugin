package io.github.aavild;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Commands implements CommandExecutor {
    IslandManager islandManager;
    SkyLyfeMain main;
    World skyworld;
    GUIManager guiManager;
    WandCreator wandCreator;
    WandHandler wandHandler;
    SchematicManager schematicManager;
    ChatEventHandler chatEventHandler;
    List<Integer> islandsizes;
    List<Integer> islandprices;
    boolean enabledDifferentSizes = false;
    String[] cmds = new String[]
            {
                    //Commands
                    "is",
                    "isadmin",
                    "gui",
                    "toggleog"
            };
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase(cmds[0]))
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;
                if (!sender.hasPermission("skylyfe.is"))
                {
                    sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    return true;
                }
                if (args.length == 0)
                {
                    if (islandManager.GetIsland(player) == null)
                    {
                        if(sender.hasPermission("skylyfe.is.create"))
                        {
                            islandManager.CreateIsland(player);
                            //Create island
                        }
                        else
                        {
                            sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                        }
                        return true;
                    }
                    if(sender.hasPermission("skylyfe.is.home"))
                    {
                        //Teleport the player to his island home.
                        islandManager.TeleportPlayerHome(player);
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))
                {
                    if(sender.hasPermission("skylyfe.is.help"))
                    { //if either theres no more than 1 arg or arg 1 is not either 2, 3 or 4
                        if (args.length == 1 || !(args[1].equals("2") || args[1].equals("3") || args[1].equals("4") || args[1].equals("5")))
                        {
                            sender.sendMessage(ChatColor.LIGHT_PURPLE + "--------------Commands [Page 1/5]--------------\n" + ChatColor.YELLOW +
                                    "/is create " + ChatColor.GREEN + "- Creates an island\n" + ChatColor.YELLOW +
                                    "/is delete " + ChatColor.GREEN + "- Deletes an island \n" + ChatColor.YELLOW +
                                    "/is invite " + ChatColor.RED + "Player " + ChatColor.GREEN + "- Invites a player to be part of your island\n" + ChatColor.YELLOW +
                                    "/is accept " + ChatColor.GREEN + "- Accepts the coop invite\n" + ChatColor.YELLOW +
                                    "/is reject " + ChatColor.GREEN + "- Rejects the coop invite\n" + ChatColor.YELLOW +
                                    "/is gui " + ChatColor.GREEN + "- Opens island GUI\n" + ChatColor.YELLOW +
                                    "/is settings " + ChatColor.GREEN + "- Opens the island settings GUI\n" + ChatColor.YELLOW +
                                    "/is challenge(s) " + ChatColor.GREEN + "- Shows the Challenges GUI (NOT ADDED YET)\n" + ChatColor.YELLOW +
                                    ChatColor.LIGHT_PURPLE + "-------Type /is help 2 to view next page----------");
                        }
                        else if (args[1].equalsIgnoreCase("2"))
                        {
                            sender.sendMessage(ChatColor.LIGHT_PURPLE + "--------------Commands [Page 2/5]--------------\n" + ChatColor.YELLOW +
                                    "/is biome " + ChatColor.GREEN + "- Opens the biome GUI\n" + ChatColor.YELLOW +
                                    "/is biome " + ChatColor.RED + "Biome " + ChatColor.GREEN + "- Changes biome to the specified biome\n" + ChatColor.YELLOW +
                                    "/is team " + ChatColor.GREEN + "- Opens the team GUI\n" + ChatColor.YELLOW +
                                    "/is level " + ChatColor.GREEN + "- Updates & Tells what level your island is\n" + ChatColor.YELLOW +
                                    "/is value " + ChatColor.GREEN + "- Tells you what value the blocks in your hand has\n" + ChatColor.YELLOW +
                                    "/is top " + ChatColor.GREEN + "- Lists the best islands of the server\n" + ChatColor.YELLOW +
                                    "/is name " + ChatColor.RED + "Name " + ChatColor.GREEN + "- Sets your islands name\n" + ChatColor.YELLOW +
                                    "/is resetname " + ChatColor.GREEN + "- Resets the island name to standard\n" +
                                    ChatColor.LIGHT_PURPLE + "-------Type /is help 3 to view next page----------");
                        }
                        else if (args[1].equalsIgnoreCase("3"))
                        {
                            sender.sendMessage(ChatColor.LIGHT_PURPLE + "--------------Commands [Page 3/5]--------------\n" + ChatColor.YELLOW +
                                    "/is sethome " + ChatColor.GREEN + "- Sets island home\n" + ChatColor.YELLOW +
                                    "/is home " + ChatColor.GREEN + "- Teleports you to your islands home\n" + ChatColor.YELLOW +
                                    "/is fixhome " + ChatColor.GREEN + "- Fixes your home\n" + ChatColor.YELLOW +
                                    "/is kick " + ChatColor.RED + "Player " + ChatColor.GREEN + "- Kicks a player from your island\n" + ChatColor.YELLOW +
                                    "/is suspend " + ChatColor.RED + "Player " + ChatColor.GREEN + "- Temporarily teleports a player away\n" + ChatColor.YELLOW +
                                    "/is ban " + ChatColor.RED + "Player " + ChatColor.GREEN + "- Bans a player from your island\n" + ChatColor.YELLOW +
                                    "/is unban " + ChatColor.RED + "Player " + ChatColor.GREEN + "- Unbans a player from your island\n" + ChatColor.YELLOW +
                                    "/is banlist " + ChatColor.GREEN + "- Lists all banned players on your island\n" + ChatColor.YELLOW +
                                    ChatColor.LIGHT_PURPLE + "-------Type /is help 4 to view next page----------");

                        }
                        else if (args[1].equalsIgnoreCase("4"))
                        {
                            sender.sendMessage(ChatColor.LIGHT_PURPLE + "--------------Commands [Page 4/5]--------------\n" + ChatColor.YELLOW +
                                    "/is coop " + ChatColor.RED + "Player " + ChatColor.GREEN + "- allows a player to build till server restart\n" + ChatColor.YELLOW +
                                    "/is uncoop " + ChatColor.RED + "Player " + ChatColor.GREEN + "- Disallows a player to build\n" + ChatColor.YELLOW +
                                    "/is listcoops " + ChatColor.GREEN + "- Lists all members of your island\n" + ChatColor.YELLOW +
                                    "/is rank " + ChatColor.GREEN + "- Shows rank and what it costs for the next rank\n" + ChatColor.YELLOW +
                                    "/is rankup " + ChatColor.GREEN + "- Ranks you up, more info in /is rank\n" + ChatColor.YELLOW +
                                    "/is lock " + ChatColor.GREEN + "- Locks your island making others unable to visit\n" + ChatColor.YELLOW +
                                    "/is unlock " + ChatColor.GREEN + "- Unlocks your island\n" + ChatColor.YELLOW +
                                    "/is teamchat " + ChatColor.GREEN + "- Toggles Teamchat\n" + ChatColor.YELLOW +
                                    ChatColor.LIGHT_PURPLE + "-------Type /is help 5 to view next page----------");
                        }
                        else
                        {
                            sender.sendMessage(ChatColor.LIGHT_PURPLE + "--------------Commands [Page 5/5]--------------\n" + ChatColor.YELLOW +
                                    "/is makeleader " + ChatColor.RED +  "Player " + ChatColor.GREEN + "- Makes another player the owner\n" + ChatColor.YELLOW +
                                    "/is leave " + ChatColor.GREEN + "- Leaves the island you're part of\n" + ChatColor.YELLOW +
                                    ChatColor.LIGHT_PURPLE + "-------      This is the last page      ----------");
                        }
                        //Returns list of commands
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("gui"))
                {
                    if(sender.hasPermission("skylyfe.is.gui"))
                    {
                        guiManager.NewInventory(player, Inventype.Island, null);
                        //Create island
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("create"))
                {
                    if(sender.hasPermission("skylyfe.is.create"))
                    {
                        islandManager.CreateIsland(player);
                        //Create island
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("delete"))
                {
                    if(sender.hasPermission("skylyfe.is.delete"))
                    {
                        islandManager.ApplyForDelete(player);
                        //Delete island
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("biome"))
                {
                    if(sender.hasPermission("skylyfe.is.biome"))
                    {
                        if (args.length == 1)
                        {
                            guiManager.NewInventory(player, Inventype.Biome, null);
                        }
                        else
                        {
                            islandManager.SetBiome(player, args[1]);
                        }
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("level"))
                {
                    if(sender.hasPermission("skylyfe.is.level"))
                    {
                        //Update/Check island level.
                        int level = islandManager.GetIslandLevel(player);
                        if(level == -1)
                            return true;
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUpdated island level to: &a" + level));
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("top"))
                {
                    if(sender.hasPermission("skylyfe.is.top"))
                    {
                        /*List<String> islandTop = islandManager.IslandTop(player);
                        sender.sendMessage(ChatColor.DARK_GRAY + "---------- " + ChatColor.BLUE + "Island top" + ChatColor.DARK_GRAY + " ----------");
                        for (String s : islandTop)
                        {
                            sender.sendMessage(ChatColor.YELLOW + s);
                        }*/
                        guiManager.NewInventory(player, Inventype.IslandTop, null);
                        //Open is top GUI
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("home") || args[0].equalsIgnoreCase("go"))
                {
                    if(sender.hasPermission("skylyfe.is.home"))
                    {
                        //Teleport the player to his island home.
                        islandManager.TeleportPlayerHome(player);
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("sethome"))
                {
                    if(sender.hasPermission("skylyfe.is.sethome"))
                    {
                        //Sets the players islands home if he is owner
                        islandManager.SetHome(player);
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("fixhome"))
                {
                    if(sender.hasPermission("skylyfe.is.fixhome"))
                    {
                        //Sets the players islands home if he is owner
                        islandManager.FixHome(player);
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("name"))
                {
                    if(sender.hasPermission("skylyfe.is.name"))
                    {
                        if (args.length == 1)
                            sender.sendMessage(ChatColor.GOLD + "Usage: /is name [Name]");
                        else
                            islandManager.SetIslandName(player, args[1]);
                        //Sets the islands name
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("value"))
                {
                    if(sender.hasPermission("skylyfe.is.value"))
                    {
                        int[] value = islandManager.GetValue(player);
                        if (value[0] == -1)
                        {
                            sender.sendMessage(ChatColor.GREEN + "This has no value");
                        }
                        else
                        {
                            sender.sendMessage(ChatColor.YELLOW + "Your current item have the value of " + ChatColor.GREEN + value[0] + " points" + ChatColor.YELLOW + " and " + ChatColor.GREEN + "$" + value[1]);
                        }
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("team"))
                {
                    if(sender.hasPermission("skylyfe.is.team"))
                    {
                        guiManager.NewInventory(player, Inventype.Team, null);
                        //Open island team GUI
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("leave"))
                {
                    if(sender.hasPermission("skylyfe.is.leave"))
                    {
                        islandManager.Leave(player);
                        //the player leaves his island
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("makeleader"))
                {
                    if(sender.hasPermission("skylyfe.is.makeleader"))
                    {
                        if (args.length == 1)
                            sender.sendMessage(ChatColor.GOLD + "Usage: /is makeleader [Player]");
                        else
                        {
                            Player target = main.getServer().getPlayer(args[1]);
                            if (target == null)
                            {
                                sender.sendMessage(ChatColor.YELLOW + "Couldn't find the player");
                                return true;
                            }
                            islandManager.MakeLeader(player, target.getUniqueId());
                        }
                        //gives another player the players island
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("teamchat") || args[0].equalsIgnoreCase("tc"))
                {
                    if(sender.hasPermission("skylyfe.is.teamchat"))
                    {
                        islandManager.TeamChat(player);
                        //Toggles team chat
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("suspend"))
                {
                    if(sender.hasPermission("skylyfe.is.suspend"))
                    {
                        if (args.length == 1)
                            sender.sendMessage(ChatColor.GOLD + "Usage: /is suspend [Player]");
                        else
                        {
                            Player target = main.getServer().getPlayer(args[1]);
                            if (target == null)
                                sender.sendMessage(ChatColor.RED + "Couldn't find the player");
                            else
                                if (!target.hasPermission("skylyfe.admin"))
                                    islandManager.Suspend(player, target);
                                else
                                {
                                    sender.sendMessage(ChatColor.RED + "You can't kick an admin out of your island");
                                    target.sendMessage(ChatColor.RED + sender.getName() + " tried to kick you off his island");
                                }
                        }
                        //suspends another player from the players island
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("ban"))
                {
                    if(sender.hasPermission("skylyfe.is.ban"))
                    {
                        Player cooped = main.getServer().getPlayer(args[1]);
                        if (cooped != null)
                        {
                            islandManager.Ban(player, cooped.getUniqueId());
                        }
                        else
                        {
                            sender.sendMessage(ChatColor.RED + "Couldn't find the player");
                        }
                        //bans another player from the players island
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("unban"))
                {
                    if(sender.hasPermission("skylyfe.is.unban"))
                    {
                        if (args.length == 1)
                            sender.sendMessage(ChatColor.GOLD + "Usage: /is suspend [Player]");
                        else
                        {
                            Player cooped = main.getServer().getPlayer(args[1]);
                            if (cooped != null)
                                islandManager.UnBan(player, cooped.getUniqueId());
                            else
                                sender.sendMessage(ChatColor.RED + "Couldn't find the player");
                        }
                        //unbans another player from the players island
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("banlist"))
                {
                    if(sender.hasPermission("skylyfe.is.banlist"))
                    {
                        islandManager.GetBanList(player);
                        //get all bans for players island
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("invite"))
                {
                    if(sender.hasPermission("skylyfe.is.invite"))
                    {
                        //adds another player to the players island team
                        if (args.length == 1)
                            sender.sendMessage(ChatColor.GOLD + "Usage: /is invite [Player]");
                        else
                        {
                            Player cooped = main.getServer().getPlayer(args[1]);
                            if (cooped != null)
                                islandManager.InvitePlayer(player, cooped);
                            else
                                sender.sendMessage(ChatColor.RED + "Couldn't find the player");
                        }
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("kick"))
                {
                    if(sender.hasPermission("skylyfe.is.kick"))
                    {
                        if (args.length == 1)
                        {
                            sender.sendMessage(ChatColor.YELLOW + "Usage: /is kick [Player]");
                        }
                        else
                        {
                            islandManager.Kick(player, args[1]);
                        }
                        //removes another player from the players island team
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("listmembers"))
                {
                    if(sender.hasPermission("skylyfe.is.listmembers"))
                    {
                        islandManager.ListMembers(player);
                        //displays a list of players the player has added to his island team
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("coop"))
                {
                    if (args.length == 1)
                    {
                        sender.sendMessage(ChatColor.YELLOW + "Usage: /is coop [Player]");
                    }
                    else
                    {
                        if(sender.hasPermission("skylyfe.is.coop"))
                        {
                            //adds another player to the players island team
                            Player cooped = main.getServer().getPlayer(args[1]);
                            if (cooped != null)
                            {
                                islandManager.CoopPlayer(player, cooped);
                            }
                            else
                            {
                                sender.sendMessage(ChatColor.RED + "Couldn't find the player");
                            }
                        }
                        else
                        {
                            sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                        }
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("uncoop"))
                {
                    if(sender.hasPermission("skylyfe.is.uncoop"))
                    {
                        if (args.length == 1)
                        {
                            sender.sendMessage(ChatColor.YELLOW + "Usage: /is uncoop/kick [Player]");
                        }
                        else
                        {
                            Player target = main.getServer().getPlayer(args[1]);
                            if (target != null)
                            {
                                islandManager.RemoveCoop(player, target);
                            }
                            else
                            {
                                sender.sendMessage(ChatColor.YELLOW + "Couldn't find the player");
                                return true;
                            }
                        }
                        //removes another player from the players island team
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("listcoops"))
                {
                    if(sender.hasPermission("skylyfe.is.listcoops"))
                    {
                        islandManager.ListCoops(player);
                        //returns list of coops
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("lock"))
                {
                    if(sender.hasPermission("skylyfe.is.lock"))
                    {
                        islandManager.LockIsland(player);
                        //locks the players island
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("unlock"))
                {
                    if(sender.hasPermission("skylyfe.is.unlock"))
                    {
                        islandManager.UnlockIsland(player);
                        //unlocks the players island
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("resetname"))
                {
                    if(sender.hasPermission("skylyfe.is.resetname"))
                    {
                        islandManager.SetIslandName(player, null);
                        //resets the players island name
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("settings"))
                {
                    if(sender.hasPermission("skylyfe.is.settings"))
                    {
                        if (islandManager.GetIsland(player) == null)
                        {
                            sender.sendMessage(ChatColor.RED + "You're not a member of an island");
                            return true;
                        }
                        guiManager.NewInventory(player, Inventype.Settings, null);
                        //opens the island settings GUI
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("accept"))
                {
                    if(sender.hasPermission("skylyfe.is.accept"))
                    {
                        islandManager.AcceptInvite(player);
                        //accepts the island team invite
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("reject"))
                {
                    if(sender.hasPermission("skylyfe.is.reject"))
                    {
                        islandManager.RejectInvite(player);
                        //reject the island team invite
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("challenge") || args[0].equalsIgnoreCase("challenges"))
                {
                    if(sender.hasPermission("skylyfe.is.challenges"))
                    {
                        sender.sendMessage("not implemented yet");
                        //Shows island challenges GUI
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("rank"))
                {
                    if (sender.hasPermission("skylyfe.is.rank"))
                    {
                        if (!enabledDifferentSizes)
                        {
                            sender.sendMessage(ChatColor.RED + "Different sizes is currently disabled");
                            return true;
                        }
                        int rank = islandManager.GetIsland(player).islandsize;
                        sender.sendMessage(ChatColor.YELLOW + "Your current island rank is: " + ChatColor.GREEN + (rank + 1) + ChatColor.YELLOW + " with " + ChatColor.GREEN + islandsizes.get(rank) + "x" + islandsizes.get(rank) + ChatColor.YELLOW + " in size");
                        if (rank != islandsizes.size())
                                sender.sendMessage(ChatColor.YELLOW + "Next rank cost: " + ChatColor.GREEN + islandprices.get(rank) +
                                        ChatColor.YELLOW + " with " + ChatColor.GREEN + islandsizes.get(rank + 1) + "x" + islandsizes.get(rank + 1) + ChatColor.YELLOW + " in size");
                        //Should return what rank a player is and how much for ranking up
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("rankup"))
                {
                    if (sender.hasPermission("skylyfe.is.rankup"))
                    {
                        islandManager.RankUpIsland(player);
                        //ranks up a players island if theyre owner
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("confirm"))
                {
                    islandManager.DeleteIsland(player);
                    return true;

                }
                sender.sendMessage(ChatColor.YELLOW + "Couldn't find the command, type /is ? for help");
                return true;
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "Not usable in console");
                return true;
            }
        }
        if (cmd.getName().equalsIgnoreCase(cmds[1]))
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;
                if (!sender.hasPermission("skylyfe.admin"))
                {
                    sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    return true;
                }
                if (args.length == 0)
                {
                    //Shows list of admin commands
                    sender.sendMessage(ChatColor.GOLD + "------Commands------\n" + ChatColor.YELLOW + "/isadmin remove [Player]\n/isadmin wand\n/isadmin schematic [Name]");
                    return true;
                }
                if (args[0].equalsIgnoreCase("remove"))
                {
                    if (sender.hasPermission("skylyfe.admin.remove"))
                    {
                        OfflinePlayer target = null;
                        for (OfflinePlayer offlinePlayer : main.getServer().getOfflinePlayers())
                        {
                            if (offlinePlayer.getName().equalsIgnoreCase(args[1]))
                                target = offlinePlayer;
                        }
                        if (target != null)
                        {
                            islandManager.ForceRemoveIsland(player, target);
                        }
                        else
                        {
                            sender.sendMessage(ChatColor.RED + "Couldn't find the player");
                        }
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("wand"))
                {
                    if(sender.hasPermission("skylyfe.admin.wand"))
                    {
                        ItemStack wand = wandCreator.CreateWand();
                        player.getInventory().addItem(wand);
                        //Gives the player a wand
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("schematic"))
                {
                    if (args.length == 1)
                    {
                        sender.sendMessage(ChatColor.GOLD + "Usage: /isadmin schematic [Name]");
                        return true;
                    }
                    if(sender.hasPermission("skylyfe.admin.schematic"))
                    {
                        Location[] locs = wandHandler.GetLocation(player);
                        if (locs == null)
                        {
                            sender.sendMessage(ChatColor.RED + "You haven't marked any locations yet. Use /isadmin wand to get a wand");
                            return true;
                        }
                        if (locs[0] == null || locs[1] == null)
                        {
                            sender.sendMessage(ChatColor.RED + "You have only marked 1 position");
                            return true;
                        }
                        schematicManager.CreateSchematic(locs[0], locs[1], args[1], player.getLocation());
                        sender.sendMessage(ChatColor.GREEN + "Created Schematic");
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("spy"))
                {
                    if(sender.hasPermission("skylyfe.admin.spy"))
                    {
                        islandManager.SpyTeamChat(player);
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                sender.sendMessage(ChatColor.YELLOW + "Couldn't find the command, type /isadmin for help");
                return true;
            }
            sender.sendMessage("Not usable in console yet");
            return true;
        }
        if (cmd.getName().equalsIgnoreCase(cmds[2]))
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;
                guiManager.NewInventory(player, Inventype.GUI, null);
            }
            return true;
        }
        if (cmd.getName().equalsIgnoreCase(cmds[3]))
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;
                if (player.hasPermission("skylyfe.admin.toggleog") && args.length != 0)
                {
                    Player target = main.getServer().getPlayer(args[0]);
                    if (target != null)
                    {
                        if (chatEventHandler.OGplayers.containsKey(target.getUniqueId()))
                        {
                            chatEventHandler.OGplayers.remove(target.getUniqueId());
                            target.sendMessage(ChatColor.YELLOW + "You've lost OG");
                            player.sendMessage(ChatColor.YELLOW + "Removed " + target.getName() + "'s OG");
                        }
                        else
                        {
                            chatEventHandler.OGplayers.put(target.getUniqueId(), true);
                            target.sendMessage(ChatColor.YELLOW + "You've been given OG\n Use /toggleog to toggle your OG");
                            player.sendMessage(ChatColor.YELLOW + "Given " + target.getName() + " OG");
                        }
                    }
                    else
                        player.sendMessage(ChatColor.RED + "Couldn't find the player");
                }
                else
                {
                    if (chatEventHandler.OGplayers.containsKey(player.getUniqueId()))
                    {
                        player.sendMessage(chatEventHandler.OGplayers.get(player.getUniqueId()) ? ChatColor.GREEN + "Disabled OG" : ChatColor.YELLOW + "Enabled OG");
                        chatEventHandler.OGplayers.put(player.getUniqueId(), !chatEventHandler.OGplayers.get(player.getUniqueId()));
                    }
                    else
                        player.sendMessage(ChatColor.YELLOW + "You're not an OGPlayer");
                }
                return true;
            }
        }
        return false;
    }

}
