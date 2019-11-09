package io.github.aavild;

import me.blackvein.quests.Quests;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class SkyLyfeMain extends JavaPlugin {
    //Config
    private String world;
    private String defaultschematic;
    private boolean enabledDifferentSizes = false;
    private List<Integer> sizes;
    private List<Integer> islandprices;

    //Load skyworld
    private WandCreator wandCreator = new WandCreator();
    private WandHandler wandHandler = new WandHandler(wandCreator);
    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {return new CustomChunkGenerator(); }

    //Classes
    private Commands commands = new Commands();
    private IslandManager islandManager = new IslandManager();
    private IslandPositionManager islandPositionManager = new IslandPositionManager();
    private GUIManager guiManager = new GUIManager();
    private SchematicManager schematicManager = new SchematicManager();

    //Eventhandlers
    private GUIEventHandler guiEventHandler = new GUIEventHandler();
    private IslandProtection islandProtection = new IslandProtection();
    private PlayerJoinEventHandler playerJoinEventHandler = new PlayerJoinEventHandler();

    //Skullmeta for each player who has ever joined
    List<PlayerHeadMeta> SkullList;

    //Dependency Plugin
    Quests quests;
    Economy economy;

    @Override
    public void onEnable() {

        //Load config and defaults
        LoadConfig();
        saveResource("default.schematic", true);
        LoadSavedSchematic();

        //Load soft-dependencies
        quests = (Quests) getServer().getPluginManager().getPlugin("Quests-3.7.8");
        if (getServer().getPluginManager().getPlugin("Vault") != null)
        {
            RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null)
            {
                getLogger().log(Level.SEVERE, "found Vault but failed loading it. Did you forget to add an economy plugin?");
            }
            else
                economy = rsp.getProvider();
            getLogger().log(Level.INFO, "[SkyL] Using vault dependency");
            islandProtection.enabledDifferentSizes = enabledDifferentSizes;
            islandManager.enabledDifferentSizes = enabledDifferentSizes;
            guiManager.enabledDifferentSizes = enabledDifferentSizes;
            commands.enabledDifferentSizes = enabledDifferentSizes;
        }


        //Load skyworld
        WorldCreator wc = new WorldCreator(world);
        wc.generator(new CustomChunkGenerator());
        World skyworld = Bukkit.createWorld(wc);
        islandManager.standardWorld = getServer().getWorlds().get(0);

        LoadSavedIslands(); // Must be loaded after skyworld has been assigned
        LoadSavedSkullMeta();

        //assign classes
        islandManager.main = this;
        commands.main = this;
        guiManager.main = this;
        schematicManager.main = this;
        islandProtection.main = this;
        playerJoinEventHandler.main = this;
        commands.skyworld = skyworld;
        islandManager.skyworld = skyworld;
        islandPositionManager.skyworld = skyworld;
        islandProtection.skyworld = skyworld;
        commands.islandManager = islandManager;
        guiManager.islandManager = islandManager;
        islandProtection.islandManager = islandManager;
        guiEventHandler.islandManager = islandManager;
        commands.guiManager = guiManager;
        guiEventHandler.guiManager = guiManager;
        commands.wandCreator = wandCreator;
        commands.wandHandler = wandHandler;
        commands.schematicManager = schematicManager;
        islandManager.islandPositionManager = islandPositionManager;
        islandManager.islandProtection = islandProtection;

        //Load EventHandlers
        getServer().getPluginManager().registerEvents(islandProtection, this);
        getServer().getPluginManager().registerEvents(guiEventHandler, this);
        getServer().getPluginManager().registerEvents(wandHandler, this);
        getServer().getPluginManager().registerEvents(playerJoinEventHandler, this);

        //Load commands
        for (String command : commands.cmds)
            getCommand(command).setExecutor(commands);
        getLogger().log(Level.INFO, "Succesfully loaded SkyLyfe");
    }
    @Override
    public void onDisable()
    {
        SaveIslands();
        SaveSkullMeta();
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
            islandManager.IslandSize = sizes.get(sizes.size() - 1);
        {
            int sizeDifference;
            getLogger().log(Level.CONFIG, "The config for sizes and islandprices is not of equal length. Reducing the length to make them equal size");
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
            getLogger().log(Level.CONFIG, "The difference in size was " + sizeDifference);
        }
        if (sizes.isEmpty())
        {
            getLogger().log(Level.CONFIG,"Since islandsizes is empty differentSizes will be disabled");
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
            //Bukkit.getServer().broadcastMessage(strings[0].trim());
            materials.add(Material.getMaterial(strings[0].trim().toUpperCase()));
            values.add(Integer.valueOf(strings[1].trim()));
            moneyValues.add(Integer.valueOf(strings[2].trim()));
        }
        islandManager.values = values;
        islandManager.materials = materials;

    }
}
