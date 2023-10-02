package io.github.aavild;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.*;
import org.bukkit.projectiles.ProjectileSource;

import java.util.List;

public class IslandProtection implements Listener {
    SkyblockMain main;
    World skyworld;
    IslandManager islandManager;
    int islandsize = 129;
    int islandTotalSize = 134;
    boolean enabledDifferentSizes = false;
    List<Integer> islandSizes;

    @EventHandler
    public void OnInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        if (player.getLocation().getWorld() != skyworld)
            return;
        Action action = event.getAction();
        //right click with bucket
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
        {
            Material hand = player.getInventory().getItemInMainHand().getType();
            if (hand == Material.BUCKET)
            {
                Block block = player.getTargetBlock(null, 7);
                if (block.getType() == Material.WATER || block.getType() == Material.LAVA)
                {
                    Location islandloc = GetIslandLocation(block.getLocation());
                    Island toIsland = islandManager.GetIslandFromLoc(islandloc);
                    event.setCancelled(HandleTheInteraction(block.getLocation(), player, toIsland, 21, false));
                    return;
                }
                if (block.getType() == Material.OBSIDIAN)
                {
                    Location islandloc = GetIslandLocation(block.getLocation());
                    Island toIsland = islandManager.GetIslandFromLoc(islandloc);
                    if (!HandleTheInteraction(block.getLocation(), player, toIsland, 21, false))
                    {
                        block.setType(Material.LAVA);
                        //player.getInventory().setItemInMainHand(new ItemStack(Material.LAVA_BUCKET));
                    }
                    else
                        event.setCancelled(true);
                    return;
                }
            }
            else if (hand == Material.LAVA_BUCKET || hand == Material.WATER_BUCKET)
            {
                Block block = player.getTargetBlock(null, 7);
                Location islandloc = GetIslandLocation(block.getLocation());
                Island toIsland = islandManager.GetIslandFromLoc(islandloc);
                event.setCancelled(HandleTheInteraction(block.getLocation(), player, toIsland, 21, false));
                return;
            }
        }
        if (action == Action.RIGHT_CLICK_AIR || action == Action.LEFT_CLICK_AIR)
            return;
        Location loc = event.getClickedBlock().getLocation();
        Location islandloc = GetIslandLocation(loc);
        Island toIsland = islandManager.GetIslandFromLoc(islandloc);
        if (event.getAction().equals(Action.PHYSICAL))
        {
            event.setCancelled(HandleTheInteraction(loc, player, toIsland, 8, false));
            return;
        }
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK))
            return;
        if ((event.isBlockInHand() && event.getPlayer().isSneaking()))
            return;
        Material material = event.getClickedBlock().getType();
        int bool = -1;
        switch (material)
        {
            case LEVER:
            case OAK_BUTTON:
            case BIRCH_BUTTON:
            case ACACIA_BUTTON:
            case SPRUCE_BUTTON:
            case STONE_BUTTON:
            case JUNGLE_BUTTON:
            case DARK_OAK_BUTTON:
                bool = 7;
                break;
            case REPEATER:
            case COMPARATOR:
            case NOTE_BLOCK: //should be in jukebox use?
                bool = 6;
                break;
            case ANVIL:
            case CHIPPED_ANVIL:
            case DAMAGED_ANVIL:
                bool = 9;
                break;
            case ACACIA_TRAPDOOR:
            case BIRCH_TRAPDOOR:
            case CRIMSON_TRAPDOOR:
            case DARK_OAK_TRAPDOOR:
            case JUNGLE_TRAPDOOR:
            case OAK_TRAPDOOR:
            case SPRUCE_TRAPDOOR:
            case WARPED_TRAPDOOR:
                bool = 10;
                break;
            case CRAFTING_TABLE:
                bool = 11;
                break;
            case CHEST:
            case TRAPPED_CHEST:
            case HOPPER:
            case BARREL:
            case DISPENSER:
            case DROPPER:
            case SHULKER_BOX:
            case BLACK_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case LIGHT_GRAY_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case WHITE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
                bool = 12;
                break;
            case FURNACE:
            case BLAST_FURNACE:
            case SMOKER:
                bool = 13;
                break;
            case BEACON:
                bool = 15;
                break;
            case RED_BED:
            case BLUE_BED:
            case BLACK_BED:
            case BROWN_BED:
            case CYAN_BED:
            case GRAY_BED:
            case GREEN_BED:
            case LIGHT_BLUE_BED:
            case LIGHT_GRAY_BED:
            case LIME_BED:
            case MAGENTA_BED:
            case ORANGE_BED:
            case PINK_BED:
            case PURPLE_BED:
            case WHITE_BED:
            case YELLOW_BED:
                bool = 16;
                break;
            case BREWING_STAND:
                bool = 17;
                break;
            case OAK_DOOR:
            case DARK_OAK_DOOR:
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case JUNGLE_DOOR:
            case SPRUCE_DOOR:
            case CRIMSON_DOOR:
            case WARPED_DOOR:
                bool = 18;
                break;
            case OAK_FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case CRIMSON_FENCE_GATE:
            case WARPED_FENCE_GATE:
                bool = 19;
                break;
            case JUKEBOX:
                bool = 20;
                break;
            default:
                return;
        }
        event.setCancelled(HandleTheInteraction(loc, player, toIsland, bool, false));
    }
    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent event)
    {
        if (event.getLocation().getWorld() != skyworld)
            return;
        //event.blockList().removeAll(event.blockList());
        event.setCancelled(true);
    }
    @EventHandler
    public void onEndermanSteal(EntityChangeBlockEvent event)
    {
        event.setCancelled((event.getEntity() instanceof Enderman));
    }
    @EventHandler
    public void mobSpawnEvent(CreatureSpawnEvent event)
    {
        event.setCancelled(event.getSpawnReason() == SpawnReason.NATURAL);
    }
    @EventHandler
    public void onEntityClickAt(PlayerInteractAtEntityEvent event)
    {
        Entity entity = event.getRightClicked();
        if (entity instanceof ArmorStand)
        {
            Location loc = entity.getLocation();
            Player player = event.getPlayer();
            Location islandloc = GetIslandLocation(loc);
            Island toIsland = islandManager.GetIslandFromLoc(islandloc);
            event.setCancelled(HandleTheInteraction(loc, player, toIsland, 14, false));
        }
    }
    @EventHandler
    public void onEntityClick(PlayerInteractEntityEvent event)
    {
        Entity mob = event.getRightClicked();
        Location loc = mob.getLocation();
        Player player = event.getPlayer();
        Location islandloc = GetIslandLocation(loc);
        Island toIsland = islandManager.GetIslandFromLoc(islandloc);
        EntityType mobType = mob.getType();
        if (mob instanceof ArmorStand)
        {
            event.setCancelled(HandleTheInteraction(loc, player, toIsland, 14, false));
            return;
        }
        switch (player.getInventory().getItemInMainHand().getType())
        {
            case BUCKET:
                if (mobType == EntityType.COW)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 21, false));
                break;
            case SHEARS:
                if (mobType == EntityType.SHEEP)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 25, true));
                break;
            case WHEAT: //cow, goat, mushroom, sheep
                if (mobType == EntityType.COW || mobType == EntityType.MUSHROOM_COW || mobType == EntityType.SHEEP)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 22, false));
                break;
            case POTATO: //pig
            case BEETROOT: //pig
                if (mobType == EntityType.PIG)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 22, false));
                break;
            case CARROT: //pig, rabbit, horse, donkey
                if (mobType == EntityType.RABBIT || mobType == EntityType.HORSE || mobType == EntityType.DONKEY || mobType == EntityType.PIG)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 22, false));
                break;
            case GOLDEN_CARROT: //rabbit, horse, donkey
                if (mobType == EntityType.RABBIT || mobType == EntityType.HORSE || mobType == EntityType.DONKEY)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 22, false));
                break;
            case GOLDEN_APPLE: //horse donkey
            case ENCHANTED_GOLDEN_APPLE: //horse donkey
                if (mobType == EntityType.HORSE || mobType == EntityType.DONKEY)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 22, false));
                break;
            case WHEAT_SEEDS: // chicken
            case BEETROOT_SEEDS: //chicken
            case MELON_SEEDS: //chicken
            case PUMPKIN_SEEDS: //chicken
                if (mobType == EntityType.CHICKEN)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 22, false));
                break;
            case CHICKEN: //wolf start
            case COOKED_CHICKEN:
            case BEEF:
            case COOKED_BEEF:
            case PORKCHOP:
            case COOKED_PORKCHOP:
            case RABBIT:
            case COOKED_RABBIT:
            case MUTTON:
            case COOKED_MUTTON:
            case ROTTEN_FLESH: //wolf end
                if (mobType == EntityType.WOLF)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 22, false));
                break;
            case COD: //cat & ocelot
            case SALMON: //cat & ocelot
                if (mobType == EntityType.OCELOT)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 22, false));
                break;
            case DANDELION: //rabbit & bees
                if (mobType == EntityType.RABBIT || mobType == EntityType.BEE)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 22, false));
                break;
            case POPPY: //BEE
                if (mobType == EntityType.BEE)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 22, false));
            case HAY_BLOCK: //Llama
                if (mobType == EntityType.LLAMA)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 22, false));
                break;
            case SEAGRASS: //turtle
                if (mobType == EntityType.TURTLE)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 22, false));
                break;
            case SWEET_BERRIES:
            case BAMBOO:
                if (mobType == EntityType.PANDA)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 22, false));
                break;
            case WARPED_FUNGUS:
                if (mobType == EntityType.STRIDER)
                    event.setCancelled(HandleTheInteraction(loc, player, toIsland, 22, false));
                break;
        }

    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        //player.sendMessage("placed block");
        if (player.getLocation().getWorld() != skyworld)
            return;
        Location loc = event.getBlockPlaced().getLocation();
        Location islandloc = GetIslandLocation(loc);
        //player.sendMessage("Island calculated: " + islandloc);
        Island toIsland = islandManager.GetIslandFromLoc(islandloc);
        /*if (toIsland != null)
            player.sendMessage("found island you placed on");*/
        event.setCancelled(HandleTheInteraction(loc, player, toIsland, 0, false));
    }
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        if (player.getLocation().getWorld() != skyworld)
            return;
        Location loc = event.getBlock().getLocation();
        Location islandloc = GetIslandLocation(loc);
        Island toIsland = islandManager.GetIslandFromLoc(islandloc);
        event.setCancelled(HandleTheInteraction(loc, player, toIsland, 1, false));
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent event)
    {
        Player player = event.getPlayer();
        if (player.getLocation().getWorld() != skyworld)
            return;
        Location loc = event.getItemDrop().getLocation();
        Location islandloc = GetIslandLocation(loc);
        Island toIsland = islandManager.GetIslandFromLoc(islandloc);
        event.setCancelled(HandleTheInteraction(loc, player, toIsland, 2, true));
    }
    @EventHandler
    public void onPickUp(EntityPickupItemEvent event)
    {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player))
            return;
        Player player = (Player) entity;
        if (player.getLocation().getWorld() != skyworld)
            return;
        Location loc = player.getLocation();
        Location islandloc = GetIslandLocation(loc);
        Island toIsland = islandManager.GetIslandFromLoc(islandloc);
        event.setCancelled(HandleTheInteraction(loc, player, toIsland, 3, true));
    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if (!(event.getDamager() instanceof Player))
            return;
        Player player = (Player) event.getDamager();
        if (player.getLocation().getWorld() != skyworld)
            return;
        if ((event.getEntity() instanceof Player))
        {
            event.setCancelled(true);
            return;
        }
        if (player.hasPermission("skyblock.admin.bypass.build"))
            return;
        Location loc = event.getEntity().getLocation();
        Location islandloc = GetIslandLocation(loc);
        Island toIsland = islandManager.GetIslandFromLoc(islandloc);
        Entity entity = event.getEntity();
        if (entity instanceof Monster)
            event.setCancelled(HandleTheInteraction(loc, player, toIsland, 5, true));
        else if (entity instanceof ArmorStand)
            event.setCancelled(HandleTheInteraction(loc, player, toIsland, 14, false));
        else
            event.setCancelled(HandleTheInteraction(loc, player, toIsland, 4, true));
    }
    @EventHandler
    public void onEntityThrownEvent(ProjectileLaunchEvent event)
    {
        Projectile projectile = event.getEntity();
        if (!(projectile.getShooter() instanceof Player))
            return;
        Player player = (Player) projectile.getShooter();
        Location loc = player.getLocation();
        Location islandloc = GetIslandLocation(loc);
        Island toIsland = islandManager.GetIslandFromLoc(islandloc);
        if (projectile instanceof Egg)
            event.setCancelled(HandleTheInteraction(loc, player, toIsland, 23, false));
        else if (projectile instanceof FishHook)
            event.setCancelled(HandleTheInteraction(loc, player, toIsland, 24, false));
    }
    @EventHandler
    public void onPotionThrow(PotionSplashEvent event)
    {
        ThrownPotion potion = event.getPotion();
        ProjectileSource source = potion.getShooter();

        if (!(source instanceof Player))
        {
            if (source instanceof LivingEntity)
                return;
            event.setCancelled(true);
            return; // not thrown by player, ignore
        }

        Player player = (Player) source;
        Location loc = player.getLocation();
        Location islandloc = GetIslandLocation(loc);
        Island toIsland = islandManager.GetIslandFromLoc(islandloc);
        event.setCancelled(HandleTheInteraction(loc, player, toIsland, -1, false));

    }
    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        event.setCancelled(HandleTheMovement(event.getTo(), event.getPlayer(), event.getFrom()));
    }
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event)
    {
        event.setCancelled(HandleTheMovement(event.getTo(), event.getPlayer(), event.getFrom()));
    }


    boolean HandleTheMovement(Location gotoloc, Player player, Location gofromloc)
    {
        //Island fromIsland;
        Island toIsland;
        if (gotoloc == null)
            return false;
        if (gotoloc.getWorld() != skyworld)
            return false;
        Location islandloc = GetIslandLocation(gotoloc);
        toIsland = islandManager.GetIslandFromLoc(islandloc);


        if (toIsland == null) //if not within an islands area
        {
            HandleLeaving(gofromloc, player);
            return false;
        }
        if (!WithinIsland(gotoloc,toIsland.getIslandLocation(skyworld), toIsland.islandsize)) //if the player doesnt go within an island
        {
            HandleLeaving(gofromloc, player);
            return false;
        }

        if (toIsland.members.contains(player.getUniqueId())) //if the island the player enters is his own
        {
            if (!WithinIsland(gofromloc, toIsland.getIslandLocation(skyworld), toIsland.islandsize)) //if where the player came from is outside the island
                SendHotBatMessage(player, ChatColor.GREEN + "Welcome back");
        }
        else //the player enters an island that isnt his
        {
            for (IslandCoop coop : islandManager.coops) //checks if the player is cooped
            {
                if (coop.island.equals(toIsland) && coop.cooped.equals(player.getUniqueId()))
                    if (WithinIsland(gotoloc, coop.island.getIslandLocation(skyworld), toIsland.islandsize))
                    {
                        if (!WithinIsland(gofromloc, coop.island.getIslandLocation(skyworld), toIsland.islandsize)) //if where the player came from is outside the island
                        {
                            if (toIsland.IslandName == null)
                                SendHotBatMessage(player, ChatColor.GREEN + "Welcome to " + ChatColor.BLUE + main.getServer().getOfflinePlayer(toIsland.owner).getName() + "s island");
                            else
                                SendHotBatMessage(player, ChatColor.GREEN + "Welcome to " + ChatColor.BLUE + toIsland.IslandName);
                        }
                        return false;
                    }
            }
            if (toIsland.locked || toIsland.Bans.contains(player.getUniqueId())) //if its locked or the player is banned
            {
                if (player.hasPermission("skyblock.admin.bypass.lock"))
                {
                    if (toIsland.locked)
                        SendHotBatMessage(player, ChatColor.YELLOW + "You bypassed the island lock");
                    else
                        SendHotBatMessage(player, ChatColor.YELLOW + "You bypassed the island ban");
                    return  false;
                }
                if (!WithinIsland(gofromloc, toIsland.getIslandLocation(skyworld), toIsland.islandsize)) //if where the player came from is outside the island
                {
                    SendHotBatMessage(player, ChatColor.YELLOW + "You're not allowed to enter this island");
                }
                return true;
            }
            else //if its not locked
            {
                if (!WithinIsland(gofromloc, toIsland.getIslandLocation(skyworld), toIsland.islandsize)) //if where the player came from is outside the island
                {
                    if (toIsland.IslandName == null)
                        SendHotBatMessage(player, ChatColor.GREEN + "Welcome to " + ChatColor.BLUE + main.getServer().getOfflinePlayer(toIsland.owner).getName() + "s island");
                    else
                        SendHotBatMessage(player, ChatColor.GREEN + "Welcome to " + ChatColor.BLUE + toIsland.IslandName);
                }
            }
        }
        return false;
    }
    private boolean HandleTheInteraction(Location loc, Player player, Island island, int isSetting, boolean AllowedOutSideIslands)
    {
        if (player.hasPermission("skyblock.admin.bypass.build"))
            return false;
        //player.sendMessage("Doesnt have admin bypass");
        Location islandloc = GetIslandLocation(loc);
        if (island == null)
        {
            //player.sendMessage("Couldnt find the island");
            return !AllowedOutSideIslands;
        }
        //player.sendMessage("found your island");
        //player.sendMessage("doesnt have setting to allow");
        if (island.members.contains(player.getUniqueId()))
            return !WithinIsland(loc, island.getIslandLocation(skyworld), island.islandsize);
        for (IslandCoop coop : islandManager.coops)
            if (coop.island.equals(island) && coop.cooped.equals(player.getUniqueId()))
                if (WithinIsland(loc, island.getIslandLocation(skyworld), island.islandsize))
                    return false;
        if (isSetting != -1)
            return !island.settings[isSetting];
        //player.sendMessage("is neither member nor cooped on island");
        return true;
    }
    boolean WithinIsland(Location loc, Location islandLocation, int islandsize)
    {
        int size;
        if (enabledDifferentSizes)
            size = islandSizes.get(islandsize);
        else
            size = this.islandsize;
        //if location is within islandlocation's x
        if (loc.getBlockX() >= islandLocation.getBlockX() - size / 2 && loc.getBlockX() <= islandLocation.getBlockX() + (size - 1) / 2)
        {
            //if location is within islandlocation's z
            return (loc.getBlockZ() >= islandLocation.getBlockZ() - size / 2 && loc.getBlockZ() <= islandLocation.getBlockZ() + (size - 1) / 2);
        }
        return false;
    }
    private Location GetIslandLocation(Location loc) //for getting island only DO NOT USE IN WITHINISLAND
    {
        //main.getServer().broadcastMessage("" + CalculateCenterForAxis(loc.getBlockX()) + CalculateCenterForAxis(loc.getBlockZ()));
        return new Location(skyworld, CalculateCenterForAxis(loc.getBlockX()), 64, CalculateCenterForAxis(loc.getBlockZ()));
    }
    private int CalculateCenterForAxis(int i)
    {
        if (i > 0)
        {
            return (i - 1) / islandTotalSize * islandTotalSize + islandTotalSize / 2 + 1;
        }
        else if (i < 0)
        {
            return (i + 1) / islandTotalSize * islandTotalSize - islandTotalSize / 2 - 1;
        }
        return 0;
    }
    private void SendHotBatMessage(Player player, String message)
    {
        try
        {
            TextComponent text = new TextComponent(message);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, text);
        }
        catch (Exception e)
        {
            main.getServer().getConsoleSender().sendMessage("[SkyL] Something went wrong with sending hotbar message");
        }
    }
    private void HandleLeaving(Location gofromloc, Player player)
    {
        Location islandloc2 = GetIslandLocation(gofromloc);
        Island fromisland = islandManager.GetIslandFromLoc(islandloc2);
        if (fromisland == null)
            return;
        if (!WithinIsland(gofromloc, fromisland.getIslandLocation(skyworld), fromisland.islandsize))
            return;
        if (fromisland.members.contains(player.getUniqueId()))
            SendHotBatMessage(player, ChatColor.RED + "You left your island");
        else
        {
            if (fromisland.IslandName == null)
                SendHotBatMessage(player, ChatColor.RED + "You left " + ChatColor.BLUE + main.getServer().getOfflinePlayer(fromisland.owner).getName() + "s island");
            else
                SendHotBatMessage(player, ChatColor.RED + "You left " + ChatColor.BLUE + fromisland.IslandName);
        }
    }
}
