package io.github.aavild;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;

public class ChatEventHandler implements Listener {
    List<Player> players = new ArrayList<Player>();
    List<Player> adminAbusers = new ArrayList<Player>();
    Map<Player, String> prefixMap = new HashMap<Player, String>();
    Map<UUID, Boolean> OGplayers = new HashMap<UUID, Boolean>();


    IslandManager islandManager;
    SkyLyfeMain main;
    @EventHandler(priority = EventPriority.LOWEST)
    public void AsyncPlayerChatEvent(AsyncPlayerChatEvent event)
    {
        /*if (!event.isAsynchronous())
            return;*/
        Player player = event.getPlayer();
        if (!players.contains(player)) //normal typing
        {
            Island island = islandManager.GetIsland(player);
            int level;
            if (island == null)
                level = 0;
            else
                level = ((int)Math.sqrt(island.value)) / 5;
            StringBuilder s = new StringBuilder();
            //player.sendMessage("awdwad");
            if (prefixMap.containsKey(player))
            {
                //player.sendMessage("Success");
                s.append(prefixMap.get(player).replace("€", "" +level));
                //player.sendMessage(s.toString());
            }
            else
            {
                player.sendMessage("error");
                return;
            }
            if (OGplayers.containsKey(player.getUniqueId()) && OGplayers.get(player.getUniqueId()))
            {
                s.replace(s.indexOf("$"), s.lastIndexOf("$") + 1, s.substring(s.indexOf("$") + 1, s.lastIndexOf("$")));
            }
            else
            {
                s.replace(s.indexOf("$"), s.lastIndexOf("$") + 1, "");
            }
            event.setFormat(ChatColor.translateAlternateColorCodes('&', s.toString()));
            return;
        }
        event.setCancelled(true);
        for (UUID member : islandManager.GetIsland(player).members)
        {
            Player recipent = main.getServer().getPlayer(member);
            if (recipent != null)
            {
                //recipent.sendMessage(ChatColor.GREEN + "[Teamchat] " + ChatColor.RED + player.getName() + ChatColor.YELLOW + ": " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', event.getMessage()));
                String s = event.getFormat();
                s = s.replaceFirst("%s", ChatColor.RED + player.getDisplayName()).replaceFirst("%s", ChatColor.WHITE + event.getMessage()).replaceFirst("%1\\$s", ChatColor.RED + player.getDisplayName()).replaceFirst("%2\\$s", ChatColor.WHITE + event.getMessage());

                recipent.sendMessage(ChatColor.GREEN + "[Teamchat] " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', s));
            }
        }
        for (Player admin : adminAbusers)
        {
            String s = event.getFormat();
            s = s.replaceFirst("%s", ChatColor.RED + player.getDisplayName()).replaceFirst("%s", ChatColor.WHITE + event.getMessage());

            admin.sendMessage(ChatColor.GREEN + "[TeamchatSpy] " + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', s));
        }

    }
}
