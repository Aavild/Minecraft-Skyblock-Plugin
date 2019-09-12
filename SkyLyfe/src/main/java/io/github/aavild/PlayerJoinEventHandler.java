package io.github.aavild;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class PlayerJoinEventHandler implements Listener {
    SkyLyfeMain main;
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
        skullMeta.setOwningPlayer(event.getPlayer());
        item.setItemMeta(skullMeta);
        PlayerHeadMeta meta = new PlayerHeadMeta(item, event.getPlayer().getUniqueId());
        if (main == null)
            event.getPlayer().sendMessage("1");
        if (main.SkullList == null)
            event.getPlayer().sendMessage("2");
        if (!main.SkullList.contains(meta))
            main.SkullList.add(meta);

    }
}
