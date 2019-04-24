package io.github.aavild;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {
    SkyLyfeMain main;
    IslandManager islandManager;
    public void NewInventory(Player player, Inventype inventype)
    {
        Inventory i = main.getServer().createInventory(null, 54, ChatColor.GREEN + inventype.name());
        List<String> description = new ArrayList<String>();
        List<Item> items = new ArrayList<Item>();
        if (inventype.equals(Inventype.Island))
        {
            if (player.hasPermission("skylyfe.is.create"))
            {
                Item item = new Item(Material.GRASS_BLOCK,ChatColor.GOLD + "Create island", description, 3, 4);
                description.clear();
                items.add(item);
            }
            if (player.hasPermission("skylyfe.is.delete"))
            {
                Item item = new Item(Material.TNT,ChatColor.GOLD + "Delete island", description, 3, 6);
                description.clear();
                items.add(item);
            }
            if (player.hasPermission("skylyfe.is.home"))
            {
                description.add(ChatColor.YELLOW + "Teleports you home");
                Item item = new Item(Material.RED_BED,ChatColor.GOLD + "Home", description, 6, 5);
                description.clear();
                items.add(item);
            }
            if (player.hasPermission("skylyfe.is.sethome"))
            {
                Item item = new Item(Material.BLUE_BED,ChatColor.GOLD + "Set home", description, 4, 5);
                description.clear();
                items.add(item);
            }
            if (player.hasPermission("skylyfe.is.Team"))
            {
                Item item = new Item(Material.CHEST,ChatColor.GOLD + "Team Management", description, 3, 3);
                description.clear();
                items.add(item);
            }
            if (player.hasPermission("skylyfe.is.top"))
            {
                description = islandManager.IslandTop(player);
                Item item = new Item(Material.DIAMOND,ChatColor.GOLD + "Island top", description, 3, 7);
                description.clear();
                items.add(item);
            }
            if (player.hasPermission("skylyfe.is.biome"))
            {
                description.add(ChatColor.YELLOW + "Changes the biome of your island");
                description.add(ChatColor.UNDERLINE + "" + ChatColor.RED + "Note that you need to relog to see the change of biome");
                Item item = new Item(Material.DIAMOND,ChatColor.GOLD + "Island biome", description, 3, 2);
                description.clear();
                items.add(item);
            }
        }
        if (inventype.equals(Inventype.Team))
        {
            Item item = new Item(Material.BOOK,ChatColor.GOLD + "Go back", description, 6, 1);
            description.clear();
            items.add(item);
            if (islandManager.HasInvite(player))
            {
                if (player.hasPermission("skylyfe.is.accept"))
                {
                    item = new Item(Material.EMERALD_BLOCK,ChatColor.GOLD + "Accept invite", description, 3, 4);
                    description.clear();
                    items.add(item);
                }
                if (player.hasPermission("skylyfe.is.reject"))
                {
                    item = new Item(Material.REDSTONE_BLOCK,ChatColor.GOLD + "Reject invite", description, 3, 6);
                    description.clear();
                    items.add(item);
                }
            }
            else
            {
                if (player.hasPermission("skylyfe.is.listcoops"))
                {
                    description = islandManager.GetMembers(player);
                    item = new Item(Material.PLAYER_HEAD,ChatColor.GOLD + "Members", description, 2, 2);
                    description.clear();
                    items.add(item);
                }
            }
        }
        if (inventype.equals(Inventype.Biome))
        {
            Item item = new Item(Material.GRASS_BLOCK,ChatColor.GOLD + "Plains", description, 1, 1);
            items.add(item);
            item = new Item(Material.SUNFLOWER,ChatColor.GOLD + "Sunflower Plains", description, 1, 2);
            items.add(item);
            item = new Item(Material.OAK_SAPLING,ChatColor.GOLD + "Flower Forest", description, 1, 3);
            items.add(item);
            item = new Item(Material.OAK_LOG,ChatColor.GOLD + "Forest", description, 1, 4);
            items.add(item);
            item = new Item(Material.LILY_PAD,ChatColor.GOLD + "Svamp", description, 1, 5);
            items.add(item);
            item = new Item(Material.COCOA_BEANS,ChatColor.GOLD + "Jungle", description, 1, 6);
            items.add(item);
            item = new Item(Material.CLAY,ChatColor.GOLD + "River", description, 1, 7);
            items.add(item);
            item = new Item(Material.SAND,ChatColor.GOLD + "Beach", description, 1, 8);
            items.add(item);
            item = new Item(Material.BROWN_MUSHROOM,ChatColor.GOLD + "Mushroom Fields", description, 1, 9);
            items.add(item);
            item = new Item(Material.STONE,ChatColor.GOLD + "Mountains", description, 2, 1);
            items.add(item);
            item = new Item(Material.SPRUCE_LOG,ChatColor.GOLD + "Taiga", description, 2, 2);
            items.add(item);
            item = new Item(Material.SNOWBALL,ChatColor.GOLD + "Snowy Taiga", description, 2, 3);
            items.add(item);
            item = new Item(Material.SNOW_BLOCK,ChatColor.GOLD + "Snowy Tundra", description, 2, 4);
            items.add(item);
            item = new Item(Material.ICE,ChatColor.GOLD + "Frozen River", description, 2, 5);
            items.add(item);
            item = new Item(Material.DEAD_BUSH,ChatColor.GOLD + "Desert", description, 2, 6);
            items.add(item);
            item = new Item(Material.ACACIA_LOG,ChatColor.GOLD + "Savanna", description, 2, 7);
            items.add(item);
            item = new Item(Material.TERRACOTTA,ChatColor.GOLD + "Badlands", description, 2, 8);
            items.add(item);
            item = new Item(Material.LIGHT_GRAY_STAINED_GLASS_PANE,ChatColor.GOLD + "The void", description, 2, 9);
            items.add(item);
            item = new Item(Material.WATER_BUCKET,ChatColor.GOLD + "Ocean", description, 3, 1);
            items.add(item);
            item = new Item(Material.WATER_BUCKET,ChatColor.GOLD + "Deep Ocean", description, 3, 2);
            items.add(item);
            item = new Item(Material.FIRE_CORAL,ChatColor.GOLD + "Deep Ocean", description, 3, 3);
            items.add(item);
            item = new Item(Material.BLUE_ICE,ChatColor.GOLD + "Frozen Ocean", description, 3, 4);
            items.add(item);
            item = new Item(Material.BOOK,ChatColor.GOLD + "Go back", description, 6, 1);
            items.add(item);
        }
        for (Item item : items)
        {
            if (item == null)
                continue;
            i.setItem(item.placement, item.item);
        }
        player.openInventory(i);
    }
}
enum Inventype {Biome, Island, Team}
