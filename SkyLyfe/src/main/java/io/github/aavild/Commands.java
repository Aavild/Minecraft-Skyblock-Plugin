package io.github.aavild;

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
    List<Integer> islandsizes;
    List<Integer> islandprices;
    boolean enabledDifferentSizes = false;
    String[] cmds = new String[]
            {
                    //Commands
                    "is",
                    "isadmin"
            };
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase(cmds[0]))
        {
            if (sender instanceof Player)
            {
                Player player = (Player) sender;
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
                        if (args.length == 1 || !(args[1].equals("2") || args[1].equals("3") || args[1].equals("4")))
                        {
                            sender.sendMessage(ChatColor.LIGHT_PURPLE + "--------------Commands [Page 1/4]--------------\n" + ChatColor.YELLOW +
                                    "/is gui " + ChatColor.GREEN + "- Opens island GUI\n" + ChatColor.YELLOW +
                                    "/is create " + ChatColor.GREEN + "- Creates an island\n" + ChatColor.YELLOW +
                                    "/is delete " + ChatColor.GREEN + "- Deletes an island \n" + ChatColor.YELLOW +
                                    "/is biome " + ChatColor.GREEN + "- Opens the biome GUI\n" + ChatColor.YELLOW +
                                    "/is biome [Biome] " + ChatColor.GREEN + "- Changes biome to the specified biome\n" + ChatColor.YELLOW +
                                    "/is level " + ChatColor.GREEN + "- Tells what level your island is\n" + ChatColor.YELLOW +
                                    "/is top " + ChatColor.GREEN + "- Lists the best islands of the server\n" + ChatColor.YELLOW +
                                    "/is home " + ChatColor.GREEN + "- Teleports you to your islands home\n" +
                                    ChatColor.LIGHT_PURPLE + "-------Type /is help 2 to view next page----------");
                        }
                        else if (args[1].equalsIgnoreCase("2"))
                        {
                            sender.sendMessage(ChatColor.LIGHT_PURPLE + "--------------Commands [Page 2/4]--------------\n" + ChatColor.YELLOW +
                                    "/is sethome " + ChatColor.GREEN + "- Sets your islands home\n" + ChatColor.YELLOW +
                                    "/is name [Name] " + ChatColor.GREEN + "- Sets your islands name\n" + ChatColor.YELLOW +
                                    "/is value " + ChatColor.GREEN + "- Tells you what value the blocks in your hand has\n" + ChatColor.YELLOW +
                                    "/is team " + ChatColor.GREEN + "- Opens the team GUI\n" + ChatColor.YELLOW +
                                    "/is leave " + ChatColor.GREEN + "- Leaves the island you're part of\n" + ChatColor.YELLOW +
                                    "/is makeleader [Player] " + ChatColor.GREEN + "- Makes another player the owner\n" + ChatColor.YELLOW +
                                    "/is teamchat " + ChatColor.GREEN + "- Toggles Teamchat (NOT ADDED YET)\n" + ChatColor.YELLOW +
                                    "/is suspend [Player] " + ChatColor.GREEN + "- Temporarily teleports a player away\n" +
                                    ChatColor.LIGHT_PURPLE + "-------Type /is help 3 to view next page----------");
                        }
                        else if (args[1].equalsIgnoreCase("3"))
                        {
                            sender.sendMessage(ChatColor.LIGHT_PURPLE + "--------------Commands [Page 3/4]--------------\n" + ChatColor.YELLOW +
                                    "/is ban [Player] " + ChatColor.GREEN + "- Bans a player from your island\n" + ChatColor.YELLOW +
                                    "/is unban [Player] " + ChatColor.GREEN + "- Unbans a player from your island\n" + ChatColor.YELLOW +
                                    "/is invite [Player] " + ChatColor.GREEN + "- Invites a player to be part of your island\n" + ChatColor.YELLOW +
                                    "/is kick [Player] " + ChatColor.GREEN + "- Kicks a player from your island\n" + ChatColor.YELLOW +
                                    "/is listcoops " + ChatColor.GREEN + "- Lists all members of your island\n" + ChatColor.YELLOW +
                                    "/is lock " + ChatColor.GREEN + "- Locks your island making others unable to visit\n" + ChatColor.YELLOW +
                                    "/is unlock " + ChatColor.GREEN + "- Unlocks your island\n" + ChatColor.YELLOW +
                                    "/is resetname " + ChatColor.GREEN + "- Resets the island name to standard\n" +
                                    ChatColor.LIGHT_PURPLE + "-------Type /is help 4 to view next page----------");

                        }
                        else
                        {
                            sender.sendMessage(ChatColor.LIGHT_PURPLE + "--------------Commands [Page 4/4]--------------\n" + ChatColor.YELLOW +
                                    "/is settings " + ChatColor.GREEN + "- Opens the island settings GUI (NOT ADDED YET)\n" + ChatColor.YELLOW +
                                    "/is accept " + ChatColor.GREEN + "- Accepts the coop invite\n" + ChatColor.YELLOW +
                                    "/is reject " + ChatColor.GREEN + "- Rejects the coop invite\n" + ChatColor.YELLOW +
                                    "/is challenge(s) " + ChatColor.GREEN + "- Shows the Challenges GUI (NOT ADDED YET)\n" + ChatColor.YELLOW +
                                    "/is rank " + ChatColor.GREEN + "- Shows rank and what it costs for the next rank\n" + ChatColor.YELLOW +
                                    "/is rankup " + ChatColor.GREEN + "- Ranks you up, more info in /is rank\n" + ChatColor.YELLOW +
                                    "/is coop " + ChatColor.GREEN + "- allows a player to build till server restart\n" + ChatColor.YELLOW +
                                    "/is uncoop " + ChatColor.GREEN + "- Disallows a player to build");
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
                        guiManager.NewInventory(player, Inventype.Island);
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
                        islandManager.DeleteIsland(player);
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
                            guiManager.NewInventory(player, Inventype.Biome);
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
                        float level = islandManager.GetIslandLevel(player);
                        if(level == -1)
                            return true;
                        sender.sendMessage(ChatColor.AQUA + "Island level: " + ChatColor.BLUE + level);
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
                        List<String> islandTop = islandManager.IslandTop(player);
                        sender.sendMessage(ChatColor.DARK_GRAY + "---------- " + ChatColor.BLUE + "Island top" + ChatColor.DARK_GRAY + " ----------");
                        for (String s : islandTop)
                        {
                            sender.sendMessage(ChatColor.YELLOW + s);
                        }
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
                        int value = islandManager.GetValue(player);
                        if (value == -1)
                        {
                            sender.sendMessage(ChatColor.GREEN + "This has no value");
                        }
                        else
                        {
                            sender.sendMessage(ChatColor.GREEN + "The value of the item in your hand is " + ChatColor.YELLOW + value);
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
                        guiManager.NewInventory(player, Inventype.Team);
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
                if (args[0].equalsIgnoreCase("teamchat"))
                {
                    if(sender.hasPermission("skylyfe.is.teamchat"))
                    {
                        sender.sendMessage("havent been implemented yet");
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
                                islandManager.Suspend(player, target);
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
                        Player cooped = main.getServer().getPlayer(args[1]);
                        if (cooped != null)
                        {
                            islandManager.UnBan(player, cooped.getUniqueId());
                        }
                        else
                        {
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
                if (args[0].equalsIgnoreCase("invite"))
                {
                    if(sender.hasPermission("skylyfe.is.invite"))
                    {
                        //adds another player to the players island team
                        Player cooped = main.getServer().getPlayer(args[1]);
                        if (cooped != null)
                        {
                            islandManager.InvitePlayer(player, cooped);
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
                            Player target = main.getServer().getPlayer(args[1]);
                            if (target != null)
                            {
                                islandManager.Kick(player, target);
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
                        sender.sendMessage(ChatColor.YELLOW + "Current rank: " + ChatColor.BLUE + (rank + 1) + ChatColor.YELLOW + " with " + ChatColor.BLUE + islandsizes.get(rank) + ChatColor.YELLOW + " in size");
                        if (rank != islandsizes.size())
                                sender.sendMessage(ChatColor.YELLOW + "Next rank cost: " + ChatColor.BLUE + islandprices.get(rank) + ChatColor.YELLOW + " with " + ChatColor.BLUE + islandsizes.get(rank + 1) + ChatColor.YELLOW + " in size");
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
                if (args.length == 0)
                {
                    //Shows list of admin commands
                    sender.sendMessage(ChatColor.GOLD + "------Commands------\n" + ChatColor.YELLOW + "/isadmin remove [Player] (Not added yet)\n/isadmin wand\n/isadmin schematic [Name]");
                    return true;
                }
                if (args[0].equalsIgnoreCase("remove"))
                {
                    if (sender.hasPermission("skylyfe.admin.remove"))
                    {
                        sender.sendMessage("not implemented yet");
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
                sender.sendMessage(ChatColor.YELLOW + "Couldn't find the command, type /isadmin for help");
                return true;
            }
            sender.sendMessage("Not usable in console yet");
            return true;
        }
        return false;
    }

}
