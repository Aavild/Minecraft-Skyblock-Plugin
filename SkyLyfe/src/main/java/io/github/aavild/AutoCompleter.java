package io.github.aavild;

import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AutoCompleter implements TabCompleter {

    private String[] commands = {"help", "gui", "create", "delete", "biome", "level", "top", "home", "sethome", "fixhome", "name",
            "value", "team", "leave", "makeleader", "teamchat", "suspend", "ban", "unban", "invite", "kick", "listcoops",
            "lock", "unlock", "resetname", "settings", "accept", "reject", "challenges", "rank", "rankup", "coop", "uncoop", "tc", "listbans", "listmembers"};

    private String[] adminCommands = {"remove", "wand", "schematic", "spy"};

    private String[] biomes;

    SkyLyfeMain main;
    AutoCompleter()
    {
        biomes = Arrays.toString(Biome.values()).replaceAll("^.|.$", "").split(", ");
    }

    public List<String> onTabComplete (CommandSender sender, Command command, String alias, String[] args){
        final List<String> completions = new ArrayList<String>();
        if (command.getName().equalsIgnoreCase("is"))
        {
            if (args.length == 1)
            {
                //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
                StringUtil.copyPartialMatches(args[0], Arrays.asList(commands), completions);
                //sort the list
                //Collections.sort(completions);
            }
            if (args.length == 2)
            {
                if (args[0].equalsIgnoreCase("biome"))
                {
                    StringUtil.copyPartialMatches(args[1], Arrays.asList(biomes), completions);
                }
                if (args[0].equalsIgnoreCase("makeleader") || args[0].equalsIgnoreCase("suspend") || args[0].equalsIgnoreCase("ban") ||
                args[0].equalsIgnoreCase("unban") || args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("coop"))
                {
                    //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
                    List<String> players = new ArrayList<String>();
                    for (Player player : main.getServer().getOnlinePlayers())
                    {
                        players.add(player.getName());
                    }

                    StringUtil.copyPartialMatches(args[1], players, completions);
                    //sort the list
                    Collections.sort(completions);

                }
            }
        }
        else if (command.getName().equalsIgnoreCase("isadmin"))
        {
            if (args.length == 1)
            {
                //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
                StringUtil.copyPartialMatches(args[0], Arrays.asList(adminCommands), completions);
                //sort the list
                Collections.sort(completions);
            }
        }
        else if (command.getName().equalsIgnoreCase("toggleog"))
        {
            if (args.length == 1 && sender.hasPermission("skylyfe.admin.toggleog"))
            {
                //copy matches of first argument from list (ex: if first arg is 'm' will return just 'minecraft')
                List<String> players = new ArrayList<String>();
                for (Player player : main.getServer().getOnlinePlayers())
                {
                    players.add(player.getName());
                }

                StringUtil.copyPartialMatches(args[0], players, completions);
                //sort the list
                Collections.sort(completions);
            }
        }



        return completions;
    }
}
