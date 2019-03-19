package io.github.aavild;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    String[] cmds =new String[]
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
                    //Island GUI Panel.
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
                        //Delete island
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
                        //Open island team GUI
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
                        //invites another player to the players island
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
                if (args[0].equalsIgnoreCase("coop"))
                {
                    if(sender.hasPermission("skylyfe.is.coop"))
                    {
                        //adds another player to the players island team
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
        if (cmd.getName().equalsIgnoreCase(cmds[0]))
        {
            if (args.length == 0)
            {
                //Shows list of admin commands
            }
            if (args[0].equalsIgnoreCase("remove"))
            {
                if (sender.hasPermission("skylyfe.admin.remove"))
                {

                }
            }
        }
            return false;
    }

}
