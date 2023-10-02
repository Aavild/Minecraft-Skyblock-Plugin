package io.github.aavild;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class GUIEventHandler implements Listener {
    IslandManager islandManager;
    GUIManager guiManager;
    SkyblockMain main;
    @EventHandler
    public void InvenClick(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
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
                if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Create Island"))
                    islandManager.CreateIsland(player);
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Delete Island"))
                    islandManager.ApplyForDelete(player);
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Home"))
                    islandManager.TeleportPlayerHome(player);
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Set Home"))
                    islandManager.SetHome(player);
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Team Management"))
                {
                    guiManager.NewInventory(player, Inventype.Team, null);
                    return;
                }
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Island Top"))
                {
                    guiManager.NewInventory(player, Inventype.IslandTop, null);
                    return;
                }
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Island biome"))
                {
                    guiManager.NewInventory(player, Inventype.Biome, null);
                    return;
                }
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Island Settings"))
                {
                    guiManager.NewInventory(player, Inventype.Settings, null);
                    return;
                }
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Upgrade Island Size"))
                    islandManager.RankUpIsland(player);
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Island Level"))
                {
                    int level = islandManager.GetIslandLevel(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eUpdated island level to: &a" + level));
                }
                player.closeInventory();
                return;
            }
        }
        else if (invView.getTitle().equals(ChatColor.GREEN + Inventype.Team.name()))
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
                    guiManager.NewInventory(player, Inventype.Island, null);
                    return;
                }
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Accept invite"))
                    islandManager.AcceptInvite(player);
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Reject invite"))
                    islandManager.RejectInvite(player);
                else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Members"))
                    return;
                else
                {
                    UUID member = islandManager.GetIsland(player).members.get(event.getSlot());
                    guiManager.NewInventory(player, Inventype.Member, main.getServer().getOfflinePlayer(member));
                    return;
                }

                player.closeInventory();
            }
        }
        else if (invView.getTitle().equals(ChatColor.GREEN + "Member Management"))
        {
            event.setCancelled(true);
            if (item == null)
                return;
            if (!item.hasItemMeta())
                return;
            if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Go back"))
            {
                guiManager.NewInventory(player, Inventype.Team, null);
                return;
            }

            if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Kick player"))
            {
                String description = item.getItemMeta().getLore().get(0);
                description = description.replace("Kicks ", "").replace(" from your island", "");
                islandManager.Kick(player, ChatColor.stripColor(description));
            }
            else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Make island leader"))
            {
                String description = item.getItemMeta().getLore().get(0);
                description = description.replace("Promotes ", "").replace(" to island leader", "");
                Player target = main.getServer().getPlayer(ChatColor.stripColor(description));

                if (target != null)
                    islandManager.MakeLeader(player, target.getUniqueId());
                else
                    player.sendMessage(ChatColor.RED + "Couldn't find the player");
            }

            player.closeInventory();


        }
        else if (invView.getTitle().equals(ChatColor.GREEN + Inventype.Biome.name()))
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
                    guiManager.NewInventory(player, Inventype.Island, null);
                    return;
                }
                String s = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', item.getItemMeta().getDisplayName()));
                s = s.replaceAll(" ", "_");
                player.closeInventory();
                islandManager.SetBiome(player, s);
            }
        }
        else if (invView.getTitle().equals(ChatColor.GREEN + "Island Top"))
        {
            event.setCancelled(true);
        }
        else if (invView.getTitle().equals(ChatColor.GREEN + "Settings"))
        {
            event.setCancelled(true);
            if (item == null)
                return;
            if (!item.hasItemMeta())
                return;
            if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Go back"))
            {
                guiManager.NewInventory(player, Inventype.Island, null);
                return;
            }
            Island island = islandManager.GetIsland(player);
            island.settings[event.getSlot()] = !island.settings[event.getSlot()];
            guiManager.NewInventory(player, Inventype.Settings, null);
        }
        else if (invView.getTitle().equals(ChatColor.GREEN + "Skyblock Menu"))
        {
            event.setCancelled(true);
            if (item == null)
                return;
            if (!item.hasItemMeta())
                return;
            if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Profile"))
                return;
            else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Shop"))
                player.performCommand("shop");
            else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Token Shop"))
                player.performCommand("tokenshop");
            else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Island"))
            {
                guiManager.NewInventory(player, Inventype.Island, null);
                return;
            }
            else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Warps"))
                player.performCommand("warps");
            else if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Achievements"))
                player.performCommand("achievements");
            player.closeInventory();
        }
    }
}
