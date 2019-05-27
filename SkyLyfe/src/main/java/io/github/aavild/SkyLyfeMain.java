package io.github.aavild;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SkyLyfeMain extends JavaPlugin {
    private Commands commands = new Commands();
    IslandManager islandManager = new IslandManager();
    private IslandPositionManager islandPositionManager = new IslandPositionManager();
    private IslandProtection islandProtection = new IslandProtection();
    private GUIEventHandler guiEventHandler = new GUIEventHandler();
    private GUIManager guiManager = new GUIManager();
    private SchematicManager schematicManager = new SchematicManager();
    private WandCreator wandCreator = new WandCreator();
    private WandHandler wandHandler = new WandHandler(wandCreator);
    private String world;
    private String defaultschematic;
    @Override
    public void onEnable() {
        LoadConfig();
        saveResource("default.schematic", true);
        LoadSavedIslands();
        LoadSavedSchematic();
        for (String command : commands.cmds)
            getCommand(command).setExecutor(commands);
        commands.islandManager = islandManager;
        commands.guiManager = guiManager;
        commands.main = this;
        commands.wandCreator = wandCreator;
        commands.wandHandler = wandHandler;
        commands.schematicManager = schematicManager;
        islandManager.islandPositionManager = islandPositionManager;
        islandManager.main = this;
        guiManager.main = this;
        guiManager.islandManager = islandManager;
        schematicManager.main = this;
        WorldCreator wc = new WorldCreator(world);
        wc.generator(new CustomChunkGenerator());
        World skyworld = Bukkit.createWorld(wc);
        commands.skyworld = skyworld;
        islandManager.skyworld = skyworld;
        islandPositionManager.skyworld = skyworld;
        islandProtection.skyworld = skyworld;
        islandProtection.islandManager = islandManager;
        getServer().getPluginManager().registerEvents(islandProtection, this);
        guiEventHandler.islandManager = islandManager;
        guiEventHandler.guiManager = guiManager;
        getServer().getPluginManager().registerEvents(guiEventHandler, this);
        getServer().getPluginManager().registerEvents(wandHandler, this);
    }
    @Override
    public void onDisable() {
        SaveIslands();
    }


    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new CustomChunkGenerator();
    }

    void SaveIslands()
    {
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
    void SaveSchematic(Schematic schematic, String name)
    {
        try
        {
            File f = new File(getDataFolder(), name + ".schematic");
            if (f.exists())
            {
                getServer().broadcastMessage(ChatColor.RED + "That schematic already exists. If you want to replace that " +
                        "schematic please delete it beforehand creating a new one");
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
    private void LoadSavedIslands()
    {
        File dir = getDataFolder();

        /*if (!dir.exists())
            if (!dir.mkdir())
                getServer().broadcastMessage("SkyLyfe: Failed to make directory for saving playerdata");*/
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
    }
    private void LoadSavedSchematic()
    {
        File dir = getDataFolder();

        /*if (!dir.exists())
            if (!dir.mkdir())
                getServer().broadcastMessage("SkyLyfe: Failed to make directory for saving playerdata");*/
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
    private void LoadConfig()
    {
        saveDefaultConfig();
        int IslandDistance = 1 + getConfig().getInt("IslandDistance", 5);
        int IslandSize = getConfig().getInt("IslandSize", 129);
        defaultschematic = getConfig().getString("Schematic", "default");
        islandPositionManager.distance = IslandDistance + IslandSize;
        islandProtection.islandsize = IslandSize;
        islandManager.IslandSize = IslandSize;
        world = getConfig().getString("SkyworldName", "Skyblocks");
    }
}
