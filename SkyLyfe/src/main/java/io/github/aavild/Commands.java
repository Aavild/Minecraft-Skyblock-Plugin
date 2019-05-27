package io.github.aavild;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Commands implements CommandExecutor {
    IslandManager islandManager;
    World skyworld;
    GUIManager guiManager;
    SkyLyfeMain main;
    WandCreator wandCreator;
    WandHandler wandHandler;
    SchematicManager schematicManager;
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
                    guiManager.NewInventory(player, Inventype.Island);
                    return true;
                }
                if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))
                {
                    if(sender.hasPermission("skylyfe.is.help"))
                    {
                        //should return list of commands
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
                        if (args.length < 2)
                        {
                            sender.sendMessage(ChatColor.GOLD + "Usage: /is name [Name]");
                            return true;
                        }
                        islandManager.SetBiome(player, args[1]);
                        //Open island Biome GUI.
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
                        if (args.length == 2)
                        {
                            islandManager.SetIslandName(player, args[1]);
                        }
                        else if (args.length == 1)
                        {
                            sender.sendMessage(ChatColor.GOLD + "Usage: /is name [Name]");
                        }
                        else
                        {
                            sender.sendMessage(ChatColor.GOLD + "Too many arguments. Usage: /is name [Name]");
                        }

                        //Sets the players home
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
                        //Check the value of what the player has in his hand
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
                if (args[0].equalsIgnoreCase("kick"))
                {
                    if(sender.hasPermission("skylyfe.is.kick"))
                    {
                        //kicks another player from his island
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
                        //unbans another player from the players island
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "You don't have access to that command");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("coop") || args[0].equalsIgnoreCase("invite"))
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
                    return true;
                }
                if (args[0].equalsIgnoreCase("uncoop"))
                {
                    if(sender.hasPermission("skylyfe.is.uncoop"))
                    {
                        if (args.length == 1)
                        {
                            sender.sendMessage(ChatColor.YELLOW + "Usage: /is uncoop [member]");
                        }
                        else
                        {
                            Player target = main.getServer().getPlayer(args[1]);
                            if (target == null)
                            {
                                sender.sendMessage(ChatColor.YELLOW + args[1] + ChatColor.RED + " doesn't exist");
                                return true;
                            }
                            islandManager.RemoveCoop(player, target.getUniqueId());
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
                if (args[0].equalsIgnoreCase("lock"))
                {
                    if(sender.hasPermission("skylyfe.is.lock"))
                    {
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
                        islandManager.AcceptCoop(player);
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
                        islandManager.RejectCoop(player);
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
                        //Shows island challenges GUI
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
                if (args.length == 0)
                {
                    sender.sendMessage(ChatColor.RED + "Not usable in console");
                    //should return list of commands
                }
                else
                {
                    sender.sendMessage(ChatColor.RED + "Not usable in console");
                }
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
                    return true;
                }
                if (args[0].equalsIgnoreCase("remove"))
                {
                    if (sender.hasPermission("skylyfe.admin.remove"))
                    {

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
            }
            sender.sendMessage("Not for console yet");
            return true;
        }
        return false;
    }

}
