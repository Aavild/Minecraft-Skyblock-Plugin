package io.github.aavild;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerJoinEventHandler implements Listener {
    SkyLyfeMain main;
    IslandProtection islandProtection;
    IslandManager islandManager;
    ChatEventHandler chatEventHandler;
    private SkullMeta skullMeta;
    private ItemStack item;
    public PlayerJoinEventHandler()
    {
        item = new ItemStack(Material.PLAYER_HEAD);
        skullMeta = (SkullMeta) item.getItemMeta();
    }
    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        skullMeta.setOwningPlayer(player);
        item.setItemMeta(skullMeta);
        PlayerHeadMeta meta = new PlayerHeadMeta(item, player.getUniqueId());
        if (!main.SkullList.contains(meta))
            main.SkullList.add(meta);
         if (islandProtection.HandleTheMovement(player.getLocation(), player, player.getLocation()))
         {
             player.performCommand("spawn");
         }
        for (Prefix prefix : main.prefixes)
            if (player.hasPermission(prefix.PermissionNode))
                chatEventHandler.prefixMap.put(player, prefix.Name);
    }
    @EventHandler
    public void OnPlayerLeave(PlayerQuitEvent event)
    {
        islandManager.RemoveAllCoops(event.getPlayer());
    }
}
