package io.github.aavild;

import me.blackvein.quests.Quests;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

public class SkyLyfeMain extends JavaPlugin {
    //Config
    private String world;
    private String defaultschematic;
    private boolean enabledDifferentSizes = false;
    private List<Integer> sizes;
    private List<Integer> islandprices;
    List<Prefix> prefixes = new ArrayList<Prefix>();

    //Load skyworld
    private WandCreator wandCreator = new WandCreator();
    private WandHandler wandHandler = new WandHandler(wandCreator);
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {return new CustomChunkGenerator(); }

    //Classes
    private Commands commands = new Commands();
    IslandManager islandManager = new IslandManager();
    private IslandPositionManager islandPositionManager = new IslandPositionManager();
    private GUIManager guiManager = new GUIManager();
    private SchematicManager schematicManager = new SchematicManager();

    //Eventhandlers
    private GUIEventHandler guiEventHandler = new GUIEventHandler();
    private IslandProtection islandProtection = new IslandProtection();
    private PlayerJoinEventHandler playerJoinEventHandler = new PlayerJoinEventHandler();
    private AutoCompleter autoCompleter = new AutoCompleter();
    private ChatEventHandler chatEventHandler = new ChatEventHandler();

    //Skullmeta for each player who has ever joined
    List<PlayerHeadMeta> SkullList;

    //Dependency Plugin
    Economy economy;
    Quests quests;

    @Override
    public void onEnable() {

        //Load config and defaults
        MakeAlwaysSunny();
        LoadConfig();
        saveResource("default.schematic", true);
        LoadSavedSchematic();
        InitializePrefixes();
        //Load soft-dependencies
        quests = (Quests) getServer().getPluginManager().getPlugin("Quests-4.0.0-rc.3");
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
        {
            SkyLyfePlaceholder skyLyfePlaceholder = new SkyLyfePlaceholder();
            skyLyfePlaceholder.register();
        }
        if (getServer().getPluginManager().getPlugin("Vault") != null)
        {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null)
            {
                getLogger().log(Level.SEVERE, ChatColor.RED + "found Vault but failed loading it. Did you forget to add an economy plugin?");
            }
            else
                economy = rsp.getProvider();
            getLogger().log(Level.INFO, ChatColor.GREEN + "[SkyL] Using vault dependency");
            islandProtection.enabledDifferentSizes = enabledDifferentSizes;
            islandManager.enabledDifferentSizes = enabledDifferentSizes;
            guiManager.enabledDifferentSizes = enabledDifferentSizes;
            commands.enabledDifferentSizes = enabledDifferentSizes;
        }


        //Load skyworld
        WorldCreator wc = new WorldCreator(world);
        wc.generator(new CustomChunkGenerator());
        World skyworld = Bukkit.createWorld(wc);
        skyworld.getWorldBorder().setSize(5000000);
        islandManager.standardWorld = getServer().getWorlds().get(0);
        for (World world : getServer().getWorlds())
        {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        }
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                for (Player player : getServer().getOnlinePlayers())
                    for (Prefix prefix : prefixes)
                        if (player.hasPermission(prefix.PermissionNode))
                            chatEventHandler.prefixMap.put(player, prefix.Name);

            }
        }, 0L, 20L);
        LoadSavedIslands(); // Must be loaded after skyworld has been assigned
        LoadSavedSkullMeta();
        LoadSavedOGS();

        //assign classes
        islandManager.main = this;
        commands.main = this;
        guiManager.main = this;
        schematicManager.main = this;
        islandProtection.main = this;
        playerJoinEventHandler.main = this;
        autoCompleter.main = this;
        guiEventHandler.main = this;
        chatEventHandler.main = this;
        commands.skyworld = skyworld;
        guiManager.skyworld = skyworld;
        islandManager.skyworld = skyworld;
        islandPositionManager.skyworld = skyworld;
        islandProtection.skyworld = skyworld;
        commands.islandManager = islandManager;
        guiManager.islandManager = islandManager;
        islandProtection.islandManager = islandManager;
        guiEventHandler.islandManager = islandManager;
        chatEventHandler.islandManager = islandManager;
        playerJoinEventHandler.islandManager = islandManager;
        commands.guiManager = guiManager;
        guiEventHandler.guiManager = guiManager;
        commands.wandCreator = wandCreator;
        commands.wandHandler = wandHandler;
        commands.schematicManager = schematicManager;
        islandManager.islandPositionManager = islandPositionManager;
        islandManager.islandProtection = islandProtection;
        playerJoinEventHandler.islandProtection = islandProtection;
        islandManager.chatEventHandler = chatEventHandler;
        commands.chatEventHandler = chatEventHandler;
        playerJoinEventHandler.chatEventHandler = chatEventHandler;

        //Load EventHandlers
        getServer().getPluginManager().registerEvents(islandProtection, this);
        getServer().getPluginManager().registerEvents(guiEventHandler, this);
        getServer().getPluginManager().registerEvents(wandHandler, this);
        getServer().getPluginManager().registerEvents(playerJoinEventHandler, this);
        getServer().getPluginManager().registerEvents(chatEventHandler, this);

        //Load commands
        for (String command : commands.cmds)
        {
            getCommand(command).setExecutor(commands);
            getCommand(command).setTabCompleter(autoCompleter);
        }
        getLogger().log(Level.INFO, ChatColor.GREEN + "Succesfully loaded SkyLyfe");

        /*
        for (int i = 0; i < 17; i++)
        {
            getLogger().log(Level.SEVERE, islandPositionManager.location(i).toString());
        }
         */

    }
    @Override
    public void onDisable()
    {
        SaveIslands();
        SaveSkullMeta();
        SaveOGS();
        guiManager.CloseAllInventories();
    }
    void SaveIslands()
    {
        //Save the Islands data
        try
        {
            File f = new File(getDataFolder(), "islands.dat");
            if (!f.exists())
                f.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(islandManager.islands);
            oos.flush();
            oos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void LoadSavedIslands()
    {
        islandManager.islands = new ArrayList<Island>();
        File f = (new File(getDataFolder(), "islands.dat"));
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Object o = ois.readObject();
            ois.close();
            islandManager.islands = (List<Island>) o;
        }
        catch (Exception e)
        {
            islandManager.islands = new ArrayList<Island>();
        }
        if (islandManager.islands == null)
            islandManager.islands = new ArrayList<Island>();
    }
    private void LoadSavedOGS()
    {
        File f = (new File(getDataFolder(), "savedOGS.dat"));
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Object o = ois.readObject();
            ois.close();
            chatEventHandler.OGplayers = (Map<UUID, Boolean>) o;
        }
        catch (Exception e)
        {
            chatEventHandler.OGplayers = new HashMap<UUID, Boolean>();
        }
        if (chatEventHandler.OGplayers == null)
            chatEventHandler.OGplayers = new HashMap<UUID, Boolean>();

        /*File customConfigFile = new File(getDataFolder(), "OGS.yml");
        if (!customConfigFile.exists())
        {
            customConfigFile.getParentFile().mkdirs();
            saveResource("OGS.yml", false);
        }
        FileConfiguration OGSConfig = new YamlConfiguration();
        try
        {
            OGSConfig.load(customConfigFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        List<String> OGStrings = OGSConfig.getStringList("OGS");
        for (String s : OGStrings)
        {
            try
            {
                if (!chatEventHandler.OGplayers.get(UUID.fromString(s)))
                    chatEventHandler.OGplayers.put(UUID.fromString(s), true);
            }
            catch (Exception ignored)
            {

            }
        }
        getServer().broadcastMessage("OGS: " + chatEventHandler.OGplayers.size());*/
    }
    void SaveOGS()
    {
        //Save the Islands data
        try
        {
            File f = new File(getDataFolder(), "savedOGS.dat");
            if (!f.exists())
                f.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(chatEventHandler.OGplayers);
            oos.flush();
            oos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    void SaveSchematic(Schematic schematic, String name)
    {
        try
        {
            File f = new File(getDataFolder(), name + ".schematic");
            if (f.exists())
            {
                getServer().broadcastMessage(ChatColor.RED + "That schematic already exists. If you want to replace that " +
                        "schematic please delete it before creating a new one");
                return;
            }
            f.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(schematic);
            oos.flush();
            oos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void LoadSavedSchematic()
    {
        File f = (new File(getDataFolder(), defaultschematic + ".schematic"));
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Object o = ois.readObject();
            ois.close();
            islandManager.schematic = (Schematic) o;
        }
        catch (Exception e)
        {
            getServer().broadcastMessage(ChatColor.RED + "[SkyLyfe] Failed to load schematic");
        }
    }
    void SaveSkullMeta()
    {
        //Save the Islands data
        try
        {
            File f = new File(getDataFolder(), "skullmeta.dat");
            if (!f.exists())
                f.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(SkullList);
            oos.flush();
            oos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void LoadSavedSkullMeta()
    {
        File f = (new File(getDataFolder(), "skullmeta.dat"));
        try
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            Object o = ois.readObject();
            ois.close();
            SkullList = (List<PlayerHeadMeta>) o;
        }
        catch (Exception e)
        {
            SkullList = new ArrayList<PlayerHeadMeta>();
        }
        if (SkullList == null)
            SkullList = new ArrayList<PlayerHeadMeta>();
    }
    private void LoadConfig()
    {
        LoadValuesConfig();
        saveDefaultConfig();
        FileConfiguration config = getConfig();
        int IslandSize = config.getInt("IslandSize", 129);
        islandProtection.islandsize = IslandSize;
        islandManager.IslandSize = IslandSize;

        int IslandDistance = config.getInt("IslandDistance", 5);
        islandPositionManager.TotalIslandSize = IslandDistance + IslandSize;
        islandProtection.islandTotalSize = IslandDistance + IslandSize;
        islandPositionManager.TotalIslandSize = IslandDistance + IslandSize;

        enabledDifferentSizes = config.getBoolean("DifferentSizes", false);
        sizes = config.getIntegerList("Sizes");
        islandprices = config.getIntegerList("Prices");
        islandProtection.islandSizes = sizes;
        guiManager.islandsizes = sizes;
        commands.islandsizes = sizes;
        islandManager.sizes = sizes;
        islandManager.islandprices = islandprices;
        if (!islandprices.isEmpty())
            islandprices.remove(0);
        guiManager.islandprices = islandprices;
        commands.islandprices = islandprices;
        if (!sizes.isEmpty())
        {
            islandProtection.islandsize = sizes.get(sizes.size() - 1);
            islandManager.IslandSize = sizes.get(sizes.size() - 1);
            islandPositionManager.TotalIslandSize = IslandDistance + sizes.get(sizes.size() - 1);
            islandProtection.islandTotalSize = IslandDistance + sizes.get(sizes.size() - 1);
            islandPositionManager.TotalIslandSize = IslandDistance + sizes.get(sizes.size() - 1);
        }
        {
            int sizeDifference;
            getLogger().log(Level.CONFIG, ChatColor.RED + "The config for sizes and islandprices is not of equal length. Will only load working prices");
                if (sizes.size() > islandprices.size() - 1)
                {
                    sizeDifference = sizes.size() - islandprices.size() - 1;
                    for (int i = 0; i < sizeDifference; i++)
                        sizes.remove(sizes.size() - 1);
                }
                else
                {
                    sizeDifference = islandprices.size() - 1 - sizes.size();
                    for (int i = 0; i < sizeDifference; i++)
                        islandprices.remove(islandprices.size() - 1);
                }
            getLogger().log(Level.CONFIG, ChatColor.RED + "The difference in size was " + sizeDifference);
        }
        if (sizes.isEmpty())
        {
            getLogger().log(Level.CONFIG,ChatColor.RED + "Since islandsizes is empty differentSizes will be disabled");
            enabledDifferentSizes = false;
        }
        world = config.getString("SkyworldName", "Skyblocks");
        defaultschematic = config.getString("Schematic", "default");
    }
    private void LoadValuesConfig()
    {
        File customConfigFile = new File(getDataFolder(), "values.yml");
        if (!customConfigFile.exists())
        {
            customConfigFile.getParentFile().mkdirs();
            saveResource("values.yml", false);
        }
        FileConfiguration valuesConfig= new YamlConfiguration();
        try
        {
            valuesConfig.load(customConfigFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        List<Material> materials = new ArrayList<Material>();
        List<Integer> values = new ArrayList<Integer>();
        List<Integer> moneyValues = new ArrayList<Integer>();
        List<String> blocks = valuesConfig.getStringList("Blocks");
        //Bukkit.getServer().broadcastMessage(blocks.size() + "");

        for (String s : blocks)
        {
            String[] strings = s.split("-");
            if (strings.length < 3)
            {
                getServer().getLogger().log(Level.INFO,ChatColor.YELLOW + strings[0] + "is not typed correctly, skipping");
                continue;
            }
            //Bukkit.getServer().broadcastMessage(strings[0].trim());
            materials.add(Material.getMaterial(strings[0].trim().toUpperCase()));
            values.add(Integer.valueOf(strings[1].trim()));
            moneyValues.add(Integer.valueOf(strings[2].trim()));
        }
        islandManager.moneyValues = moneyValues;
        islandManager.values = values;
        islandManager.materials = materials;

    }
    private void MakeAlwaysSunny()
    {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                for(World world : Bukkit.getServer().getWorlds()) {
                    world.setThundering(false);
                    world.setStorm(false);
                    world.setFullTime(7000);
                }

            }
        }, 0L, 20L);
    }
    void InitializePrefixes() {
        prefixes.add(new Prefix("mmoskylyfe.prefix.default", ChatColor.translateAlternateColorCodes('&', "&8[&7lvl. €&8]&r $&8[&7OG&8]$&r &8[&7Islander&8] &r&7%1s&0: &7%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.skymage", ChatColor.translateAlternateColorCodes('&', "&0[&2lvl. €&0]&r $&0[&2OG&0]$&r &2&kii&0[&2SkyMage&0]&2&kii &r&2%1s&0: &2%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.skywizard", ChatColor.translateAlternateColorCodes('&', "&0[&9lvl. €&0]&r $&0[&9OG&0]$&r &9&kii&0[&9SkyWizard&0]&9&kii &r&9%1s&0: &9%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.skymaster", ChatColor.translateAlternateColorCodes('&', "&0[&dlvl. €&0]&r $&0[&dOG&0]$&r &d&kii&0[&dSkyMaster&0]&d&kii &r&d%1s&0: &d%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.skylord", ChatColor.translateAlternateColorCodes('&', "&0[&alvl. €&0]&r $&0[&aOG&0]$&r &a&kii&0[&aSkyLord&0]&a&kii &r&a%1s&0: &a%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.skylegend", ChatColor.translateAlternateColorCodes('&', "&0[&3lvl. €&0]&r $&0[&3OG&0]$&r &3&kii&0[&3SkyLegend&0]&3&kii &r&3%1s&0: &3%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.skyimmortal", ChatColor.translateAlternateColorCodes('&', "&0[&4lvl. €&0]&r $&0[&4OG&0]$&r &4&kii&0[&4SkyImmortal&0]&4&kii &r&4%1s&0: &4%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.skygod", ChatColor.translateAlternateColorCodes('&', "&0[&blvl. €&0]&r $&0[&bOG&0]$&r &b&kii&0[&bSkyGod&0]&b&kii &r&b%1s&0: &b%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.helper", ChatColor.translateAlternateColorCodes('&', "&0[&elvl. €&0]&r $&0[&eOG&0]$&r &e&kii&0[&eHelper&0]&e&kii &r&e%1s&0: &e%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.mod", ChatColor.translateAlternateColorCodes('&', "&0[&2lvl. €&0]&r $&0[&2OG&0]$&r &2&kii&0[&2Mod&0]&2&kii &r&2%1s&0: &2%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.headmod", ChatColor.translateAlternateColorCodes('&', "&0[&alvl. €&0]&r $&0[&aOG&0]$&r &a&kii&0[&aHead-Mod&0]&a&kii &r&a%1s&0: &a%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.subadmin", ChatColor.translateAlternateColorCodes('&', "&0[&clvl. €&0]&r $&0[&cOG&0]$&r &c&kii&0[&cSub-Admin&0]&c&kii &r&c%1s&0: &c%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.admin", ChatColor.translateAlternateColorCodes('&', "&0[&4lvl. €&0]&r $&0[&4OG&0]$&r &4&kii&0[&4Admin&0]&4&kii &r&4%1s&0: &4%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.lazydefault", ChatColor.translateAlternateColorCodes('&', "&0[&blvl. €&0]&r $&0[&bOG&0]$&r &8[&7LazyDefault&8] &r&b%1s&0: &b%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.gizmo", ChatColor.translateAlternateColorCodes('&', "&0[&blvl. €&0]&r $&0[&bOG&0]$&r &0&l[&8&lG&7&liz&f&lmo&0&l] &r&b%1s&0: &b%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.rosie", ChatColor.translateAlternateColorCodes('&', "&0[&blvl. €&0]&r $&0[&bOG&0]$&r &0&l[&4&lR&d&lo&a&ls&b&li&e&le&c&l<3&0&l] &r&b%1s&0: &b%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.owner", ChatColor.translateAlternateColorCodes('&', "&0[&blvl. €&0]&r $&0[&bOG&0]$&r &b&kii&0[&bOwner&0]&b&kii &r&b%1s&0: &b%2s")));
        prefixes.add(new Prefix("mmoskylyfe.prefix.mahshadow", ChatColor.translateAlternateColorCodes('&', "&0[&blvl. €&0]&r $&0[&bOG&0]$&r &b&kii&f&l[&8&lCh&7&lha&f&lya&f&l]&b&kii&r &b%1s&0: &b%2s")));
    }
}
