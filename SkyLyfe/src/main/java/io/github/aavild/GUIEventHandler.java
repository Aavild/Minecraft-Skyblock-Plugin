package io.github.aavild;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class GUIEventHandler implements Listener {
    IslandManager islandManager;
    GUIManager guiManager;
    @EventHandler
    public void InvenClick(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        ClickType click = event.getClick();
        Inventory inv = event.getClickedInventory();
        InventoryView invView = event.getView();
        ItemStack item = event.getCurrentItem();
        if (inv == null)
            return;
        if (invView.getTitle().equals(ChatColor.GREEN + Inventype.Island.name()))
        {
            event.setCancelled(true);
            if (inv.equals(invView.getTopInventory()))
            {
                if (item == null)
                    return;
                if (!item.hasItemMeta())
                    return;
                if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Create island"))
                    islandManager.CreateIsland(player);
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Delete island"))
                    islandManager.DeleteIsland(player);
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Home"))
                    islandManager.TeleportPlayerHome(player);
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Set home"))
                    islandManager.SetHome(player);
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Team Management"))
                {
                    guiManager.NewInventory(player, Inventype.Team);
                    return;
                }
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Island top"))
                    return;
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Island biome"))
                {
                    guiManager.NewInventory(player, Inventype.Biome);
                    return;
                }
                player.closeInventory();
                return;
            }
        }
        if (invView.getTitle().equals(ChatColor.GREEN + Inventype.Team.name()))
        {
            event.setCancelled(true);
            if (inv.equals(invView.getTopInventory()))
            {
                if (item == null)
                    return;
                if (!item.hasItemMeta())
                    return;
                if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Go back"))
                {
                    guiManager.NewInventory(player, Inventype.Island);
                    return;
                }
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Accept invite"))
                    islandManager.AcceptCoop(player);
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Reject invite"))
                    islandManager.RejectCoop(player);
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Members"))
                    return;

                player.closeInventory();
                return;
            }
        }
    }
}
