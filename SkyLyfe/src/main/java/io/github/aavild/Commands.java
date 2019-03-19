package io.github.aavild;

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
                    if (label.equalsIgnoreCase("island"))
                    {
                        //should return list of commands
                    }
                    else
                    {
                        //should teleport players to their skyblock
                    }
                }
                if (args.length == 1)
                {
                    //diferent commands here
                }
            }
            else
            {
                if (args.length == 0)
                {
                    //should return list of commands
                }
                return false;
            }
        }
        return false;
    }

}
