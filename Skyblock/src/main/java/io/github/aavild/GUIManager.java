package io.github.aavild;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class GUIManager {
    SkyblockMain main;
    IslandManager islandManager;
    List<Integer> islandsizes;
    List<Integer> islandprices;
    World skyworld;
    boolean enabledDifferentSizes = false;
    List<Inventory> openInventories = new ArrayList<Inventory>();
    public void NewInventory(Player player, Inventype inventype, OfflinePlayer target)
    {
        Inventory inv;
        List<String> description = new ArrayList<String>();
        List<Item> items = new ArrayList<Item>();
        if (inventype.equals(Inventype.Island))
        {
            inv = main.getServer().createInventory(null, 9, ChatColor.GREEN + "Island");
            if (islandManager.GetIsland(player) == null)
            {
                if (player.hasPermission("skyblock.is.create"))
                {
                    Item item = new Item(Material.GRASS_BLOCK,ChatColor.GOLD + "Create Island", description, 1, 5);
                    description.clear();
                    items.add(item);
                }
            }
            else
            {
                if (player.hasPermission("skyblock.is.delete"))
                {
                    Item item = new Item(Material.TNT,ChatColor.GOLD + "Delete Island", description, 1, 9);
                    description.clear();
                    items.add(item);
                }
                if (player.hasPermission("skyblock.is.settings"))
                {
                    Item item = new Item(Material.COMPARATOR,ChatColor.GOLD + "Island Settings", description, 1, 5);
                    description.clear();
                    items.add(item);
                }
                if (player.hasPermission("skyblock.is.home"))
                {
                    description.add(ChatColor.YELLOW + "Teleports you home");
                    Item item = new Item(Material.RED_BED,ChatColor.GOLD + "Home", description, 1, 1);
                    description.clear();
                    items.add(item);
                }
                if (player.hasPermission("skyblock.is.sethome"))
                {
                    Item item = new Item(Material.BLUE_BED,ChatColor.GOLD + "Set Home", description, 1, 2);
                    description.clear();
                    items.add(item);
                }
                if (player.hasPermission("skyblock.is.Team"))
                {
                    Item item = new Item(Material.CHEST,ChatColor.GOLD + "Team Management", description, 1, 4);
                    description.clear();
                    items.add(item);
                }
                if (player.hasPermission("skyblock.is.level"))
                {
                    description.add(ChatColor.YELLOW + "Calculates your island level");
                    Item item = new Item(Material.GOLD_NUGGET,ChatColor.GOLD + "Island Level", description, 1, 7);
                    description.clear();
                    items.add(item);
                }
                if (player.hasPermission("skyblock.is.top"))
                {
                    description = islandManager.IslandTop(player);
                    Item item = new Item(Material.DIAMOND,ChatColor.GOLD + "Island Top", description, 1, 8);
                    description.clear();
                    items.add(item);
                }
                if (player.hasPermission("skyblock.is.biome"))
                {
                    description.add(ChatColor.YELLOW + "Changes the biome of your island");
                    description.add(ChatColor.UNDERLINE + "" + ChatColor.RED + "Note: that you need to relog to");
                    description.add(ChatColor.UNDERLINE + "" + ChatColor.RED + "see the change of biome");
                    Item item = new Item(Material.OAK_SAPLING,ChatColor.GOLD + "Island biome", description, 1, 3);
                    description.clear();
                    items.add(item);
                }
                if (player.hasPermission("skyblock.is.rank") && player.hasPermission("skyblock.is.rankup"))
                {
                    if (enabledDifferentSizes)
                    {
                        Island island = islandManager.GetIsland(player);
                        if (island != null)
                        {
                            int rank = island.islandsize;
                            if (island.owner.equals(player.getUniqueId()))
                            {
                                if (rank != islandsizes.size())
                                {
                                    description.add(ChatColor.YELLOW + "Upgrades your island from: ");
                                    description.add(ChatColor.GREEN + "" + islandsizes.get(rank) + "x" + islandsizes.get(rank) + ChatColor.YELLOW + " to " + ChatColor.GREEN + "" + islandsizes.get(rank + 1) + "x" + islandsizes.get(rank + 1));
                                    description.add(ChatColor.YELLOW + "Price: " + ChatColor.GREEN + "$" + islandprices.get(rank));
                                }
                                else
                                {
                                    description.add(ChatColor.YELLOW + "You're at maximum island size with " + ChatColor.GREEN + islandsizes.get(rank) + ChatColor.YELLOW + " in size");
                                }


                                Item item = new Item(Material.GOLD_BLOCK,ChatColor.GOLD + "Upgrade Island Size", description, 1, 6);
                                description.clear();
                                items.add(item);
                            }
                            else
                            {
                                if (rank != islandsizes.size())
                                {
                                    description.add(ChatColor.YELLOW + "Your owner can upgrade from: ");
                                    description.add(ChatColor.GREEN + "" + islandsizes.get(rank) + "x" + islandsizes.get(rank) + ChatColor.YELLOW + " to " + ChatColor.GREEN + "" + islandsizes.get(rank + 1) + "x" + islandsizes.get(rank + 1));
                                    description.add(ChatColor.YELLOW + "Price: " + ChatColor.GREEN + "$" + islandprices.get(rank));
                                }
                                else
                                {
                                    description.add(ChatColor.YELLOW + "You're at maximum island size with " + ChatColor.GREEN + islandsizes.get(rank) + ChatColor.YELLOW + " in size");
                                }
                                description.add(ChatColor.RED + "Note: Only the island leader can upgrade the island size");
                                Item item = new Item(Material.GOLD_BLOCK,ChatColor.BLUE + "Island Size", description, 1, 6);
                                description.clear();
                                items.add(item);
                            }
                        }
                    }
                }
            }

        }
        else if (inventype.equals(Inventype.Team))
        {
            inv = main.getServer().createInventory(null, 27, ChatColor.GREEN + "Team");
            Item item = new Item(Material.BOOK,ChatColor.GOLD + "Go back", description, 3, 1);
            description.clear();
            items.add(item);
            if (islandManager.HasInvite(player))
            {
                if (player.hasPermission("skyblock.is.accept"))
                {
                    item = new Item(Material.EMERALD_BLOCK,ChatColor.GOLD + "Accept invite", description, 2, 4);
                    description.clear();
                    items.add(item);
                }
                if (player.hasPermission("skyblock.is.reject"))
                {
                    item = new Item(Material.REDSTONE_BLOCK,ChatColor.GOLD + "Reject invite", description, 2, 6);
                    description.clear();
                    items.add(item);
                }
            }
            else
            {
                if (player.hasPermission("skyblock.is.listmembers"))
                {
                    int rank = 0;
                    for (UUID member : islandManager.GetIsland(player).members)
                    {
                        if (member == null)
                            continue;
                        if (rank == 27)
                            break;
                        ItemStack skull = GetSkull(member);
                        ItemMeta itemMeta = skull.getItemMeta();
                        itemMeta.setDisplayName(ChatColor.GREEN + main.getServer().getOfflinePlayer(member).getName());
                        skull.setItemMeta(itemMeta);


                        inv.setItem(rank, skull);
                        rank++;
                    }
                }
            }
        }
        else if (inventype.equals(Inventype.Member))
        {
            inv = main.getServer().createInventory(null, 9, ChatColor.GREEN + "Member Management");
            Item item = new Item(Material.BOOK,ChatColor.GOLD + "Go back", description, 1, 9);
            description.clear();
            items.add(item);
            description.add(ChatColor.YELLOW + "Kicks " + ChatColor.RED + target.getName() + ChatColor.YELLOW + " from your island");
            item = new Item(Material.BARRIER,ChatColor.GOLD + "Kick player", description, 1, 1);
            description.clear();
            items.add(item);
            description.add(ChatColor.YELLOW + "Promotes "  + ChatColor.RED + target.getName() + ChatColor.YELLOW +  " to island leader");
            item = new Item(Material.EMERALD_BLOCK,ChatColor.GOLD + "Make island leader", description, 1, 2);
            description.clear();
            items.add(item);

        }
        else if (inventype.equals(Inventype.Biome))
        {
            inv = main.getServer().createInventory(null, 36, ChatColor.GREEN + "Biome");
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
            item = new Item(Material.BEDROCK,ChatColor.GOLD + "The Void", description, 2, 9);
            items.add(item);
            item = new Item(Material.WATER_BUCKET,ChatColor.GOLD + "Ocean", description, 3, 1);
            items.add(item);
            item = new Item(Material.WATER_BUCKET,ChatColor.GOLD + "Deep Ocean", description, 3, 2);
            items.add(item);
            item = new Item(Material.FIRE_CORAL,ChatColor.GOLD + "Deep Ocean", description, 3, 3);
            items.add(item);
            item = new Item(Material.BLUE_ICE,ChatColor.GOLD + "Frozen Ocean", description, 3, 4);
            items.add(item);
            item = new Item(Material.BOOK,ChatColor.GOLD + "Go back", description, 4, 5);
            items.add(item);
        }
        else if (inventype.equals(Inventype.IslandTop))
        {
            inv = main.getServer().createInventory(null, 27, ChatColor.GREEN + "Island Top");
            int rank = 1;
            for (Island island : islandManager.GetTopTen())
            {
                if (island == null)
                    continue;
                //islandManager.UpdateIslandsValue(island);
                ItemStack item = GetSkull(island.owner);
                ItemMeta itemMeta = item.getItemMeta();

                List<String> lore = new ArrayList<String>();
                //lore.add(ChatColor.BLUE + "Island Value: " + ChatColor.YELLOW + island.value);
                lore.add(ChatColor.YELLOW + "Island Level: " + ChatColor.GREEN + (((int)Math.sqrt(island.value)) / 5));
                lore.add(ChatColor.YELLOW + "Island Worth: " + ChatColor.GREEN + "$" + island.moneyValue);


                StringBuilder members = new StringBuilder(ChatColor.YELLOW + "island Members: " + ChatColor.RED);
                int u = 1;
                for (int i = 0; i < island.members.size(); i++)
                {
                    String member;
                    if (island.members.get(i) == island.owner)
                        member = "" + ChatColor.GOLD + main.getServer().getOfflinePlayer(island.owner).getName() + ChatColor.YELLOW + ", " + ChatColor.RED;
                    else
                        member = main.getServer().getOfflinePlayer(island.members.get(i)).getName() + ChatColor.YELLOW + ", ";
                    members.append(member);
                    if ((i + 2) / 3 == u)
                    {
                        members.deleteCharAt(members.length() - 4);
                        lore.add(members.toString());
                        members = new StringBuilder(ChatColor.RED + "");
                        u++;
                    }
                }
                /*try
                {
                    members.deleteCharAt(members.length() - 4);
                }
                catch (Exception e)
                {
                    Player aavild = main.getServer().getPlayer("aavild");
                    if (aavild != null)
                        aavild.sendMessage("" + island.owner);
                }*/
                String string = members.toString();
                lore.add(string.substring(0, string.length() - 1));


                /*Location loc = island.getIslandLocation(skyworld);
                lore.add(ChatColor.BLUE +"Located at: " + "X: " + ChatColor.YELLOW +
                        loc.getBlockX() + ChatColor.BLUE + " Z: " + ChatColor.YELLOW + loc.getBlockZ());*/
                if (island.IslandName != null)
                {
                    itemMeta.setDisplayName(ChatColor.GOLD + island.IslandName);
                    lore.add("");
                    lore.add(ChatColor.RED + main.getServer().getOfflinePlayer(island.owner).getName() + ChatColor.YELLOW + "'s island");
                }
                else
                    itemMeta.setDisplayName(ChatColor.GOLD + main.getServer().getOfflinePlayer(island.owner).getName() + "'s island");

                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);

                switch (rank)
                {
                    case 1:
                        inv.setItem(4, item);
                        break;
                    case 2:
                        inv.setItem(12, item);
                        break;
                    case 3:
                        inv.setItem(14, item);
                        break;
                    case 4:
                        inv.setItem(19, item);
                        break;
                    case 5:
                        inv.setItem(20, item);
                        break;
                    case 6:
                        inv.setItem(21, item);
                        break;
                    case 7:
                        inv.setItem(22, item);
                        break;
                    case 8:
                        inv.setItem(23, item);
                        break;
                    case 9:
                        inv.setItem(24, item);
                        break;
                    case 10:
                        inv.setItem(25, item);
                        break;

                }
                rank++;
            }
            player.openInventory(inv);
            openInventories.add(inv);
            return;
        }
        else if (inventype.equals(Inventype.Settings))
        {
            inv = main.getServer().createInventory(null, 45, ChatColor.GREEN + "Settings");
            Item item = new Item(Material.DIRT,ChatColor.GOLD + "Toggle placing blocks", description, 1, 1);
            items.add(item);
            item = new Item(Material.STONE,ChatColor.GOLD + "Toggle breaking blocks", description, 1, 1);
            items.add(item);
            item = new Item(Material.GOLD_INGOT,ChatColor.GOLD + "Toggle item dropping", description, 1, 1);
            items.add(item);
            item = new Item(Material.DIAMOND,ChatColor.GOLD + "Toggle Item Pick-Up", description, 1, 1);
            items.add(item);
            item = new Item(Material.WOODEN_SWORD,ChatColor.GOLD + "Toggle Killing / Hurting Animals", description, 1, 1);
            items.add(item);
            item = new Item(Material.IRON_SWORD,ChatColor.GOLD + "Toggle Killing / Hurting Mobs", description, 1, 1);
            items.add(item);
            item = new Item(Material.COMPARATOR,ChatColor.GOLD + "Toggle Redstone Use", description, 1, 1);
            items.add(item);
            item = new Item(Material.LEVER,ChatColor.GOLD + "Toggle Use of Levers or Buttons", description, 1, 1);
            items.add(item);
            item = new Item(Material.HEAVY_WEIGHTED_PRESSURE_PLATE,ChatColor.GOLD + "Toggle Use of Pressure Plates", description, 1, 1);
            items.add(item);
            item = new Item(Material.ANVIL,ChatColor.GOLD + "Toggle Anvil Use", description, 1, 1);
            items.add(item);
            item = new Item(Material.OAK_TRAPDOOR,ChatColor.GOLD + "Toggle Use Of Trapdoors", description, 1, 1);
            items.add(item);
            item = new Item(Material.CRAFTING_TABLE,ChatColor.GOLD + "Toggle use of Crafting Table", description, 1, 1);
            items.add(item);
            item = new Item(Material.CHEST,ChatColor.GOLD + "Toggle Container Use", description, 1, 1);
            items.add(item);
            item = new Item(Material.FURNACE,ChatColor.GOLD + "Toggle Use of Furnace", description, 1, 1);
            items.add(item);
            item = new Item(Material.ARMOR_STAND,ChatColor.GOLD + "Toggle Use Of Armor Stands", description, 1, 1);
            items.add(item);
            item = new Item(Material.BEACON,ChatColor.GOLD + "Toggle Use Of Beacons", description, 1, 1);
            items.add(item);
            item = new Item(Material.RED_BED,ChatColor.GOLD + "Toggle Use Of Beds", description, 1, 1);
            items.add(item);
            item = new Item(Material.BREWING_STAND,ChatColor.GOLD + "Toggle Use of Brewing stand", description, 1, 1);
            items.add(item);
            item = new Item(Material.OAK_DOOR,ChatColor.GOLD + "Toggle Door Use", description, 1, 1);
            items.add(item);
            item = new Item(Material.OAK_FENCE_GATE,ChatColor.GOLD + "Toggle Gate Use", description, 1, 1);
            items.add(item);
            item = new Item(Material.JUKEBOX,ChatColor.GOLD + "Toggle use of Jukebox / (music notes?)", description, 1, 1);
            items.add(item);
            item = new Item(Material.BUCKET,ChatColor.GOLD + "Toggle use of Water, Lava and Milk", description, 1, 1);
            items.add(item);
            item = new Item(Material.CARROT,ChatColor.GOLD + "Toggle Breeding", description, 1, 1);
            items.add(item);
            item = new Item(Material.EGG,ChatColor.GOLD + "Toggle Egg Throwing", description, 1, 1);
            items.add(item);
            item = new Item(Material.FISHING_ROD,ChatColor.GOLD + "Toggle Use Of Fishing Rod", description, 1, 1);
            items.add(item);
            item = new Item(Material.SHEARS,ChatColor.GOLD + "Toggle Use Of Shears", description, 1, 1);
            items.add(item);
            Island island = islandManager.GetIsland(player);
            for (int i = 0; i < items.size(); i++)
            {
                Item item1 = items.get(i);
                if (item1 == null)
                    continue;
                ItemMeta itemMeta = item1.item.getItemMeta();
                List<String> desc = new ArrayList<String>();
                if (island.settings[i])
                    desc.add(ChatColor.GREEN + "Allowed");
                else
                    desc.add(ChatColor.RED + "Disallowed");
                if (item1.item.getType().equals(Material.WOODEN_SWORD) || item1.item.getType().equals(Material.IRON_SWORD))
                {
                    itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                }
                itemMeta.setLore(desc);
                item1.item.setItemMeta(itemMeta);
                inv.setItem(i, item1.item);
            }
            Item item2 = new Item(Material.BOOK, ChatColor.GOLD + "Go back", description, 5, 5);
            inv.setItem(item2.placement, item2.item);
            player.openInventory(inv);
            openInventories.add(inv);
            return;

        }
        else if (inventype.equals(Inventype.GUI))
        {
            inv = main.getServer().createInventory(null, 9, ChatColor.GREEN + "Skyblock Menu");

            ItemStack skull = GetSkull(player.getUniqueId());
            ItemMeta itemMeta = skull.getItemMeta();

            description.add(ChatColor.translateAlternateColorCodes('&', "&7View your personal"));
            description.add(ChatColor.translateAlternateColorCodes('&', "&7statistics on skyblock!"));
            description.add("");
            description.add(ChatColor.translateAlternateColorCodes('&', "&eClick to view!"));
            itemMeta.setLore(description);

            itemMeta.setDisplayName(ChatColor.GOLD + "Profile");
            skull.setItemMeta(itemMeta);
            inv.setItem(0, skull);

            Item item = new Item(Material.OAK_SIGN,ChatColor.GOLD + "Shop", description, 1, 2);
            items.add(item);
            item = new Item(Material.NETHER_STAR,ChatColor.GOLD + "Token Shop", description, 1, 3);
            items.add(item);
            item = new Item(Material.GRASS_BLOCK,ChatColor.GOLD + "Island", description, 1, 4);
            items.add(item);
            item = new Item(Material.ENDER_PEARL,ChatColor.GOLD + "Warps", description, 1, 5);
            items.add(item);
            item = new Item(Material.WRITABLE_BOOK,ChatColor.GOLD + "Achievements", description, 1, 6);
            items.add(item);
        }
        else
        {
            main.getServer().getLogger().log(Level.SEVERE, "Error loading Inventory");
            return;
        }

        for (Item item : items)
        {
            if (item == null)
                continue;
            inv.setItem(item.placement, item.item);
        }
        player.openInventory(inv);
        openInventories.add(inv);
    }
    void CloseAllInventories()
    {
        for (Inventory inv : openInventories)
            inv.clear();
    }
    private ItemStack GetSkull(UUID uuid)
    {
        for (PlayerHeadMeta ItemSkull : main.SkullList)
        {
            if (ItemSkull == null)
                continue;
            if (ItemSkull.player.equals(uuid))
                return ItemSkull.GetSkull();
        }
        Bukkit.getLogger().log(Level.SEVERE, "An error occured");
        return null;
    }
}
