package io.github.aavild;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SkyLyfeMain extends JavaPlugin {
    private Commands commands = new Commands();
    private IslandManager islandManager = new IslandManager();
    private Schematic schematic = new Schematic();
    private IslandPositionManager islandPositionManager = new IslandPositionManager();
    private IslandProtection islandProtection = new IslandProtection();
    private GUIEventHandler guiEventHandler = new GUIEventHandler();
    private GUIManager guiManager = new GUIManager();
    private String world;
    private File f;
    @Override
    public void onEnable() {
        LoadConfig();
        LoadPlayerData();
        for (String command : commands.cmds)
            getCommand(command).setExecutor(commands);
        commands.islandManager = islandManager;
        commands.schematic = schematic;
        commands.guiManager = guiManager;
        islandManager.islandPositionManager = islandPositionManager;
        islandManager.schematic = schematic;
        islandManager.main = this;
        guiManager.main = this;
        guiManager.islandManager = islandManager;
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
    }
    @Override
    public void onDisable() {
        Save();
    }


    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new CustomChunkGenerator();
    }

    void Save()
    {
        try
        {
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
    private void LoadPlayerData()
    {
        File dir = getDataFolder();

        if (!dir.exists())
            if (!dir.mkdir())
                getServer().broadcastMessage("SkyLyfe: Failed to make directory for saving playerdata");
        f = (new File(getDataFolder(), "players.dat"));
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
    private void LoadConfig()
    {
        saveDefaultConfig();
        int IslandDistance = 1 + getConfig().getInt("IslandDistance", 5);
        int IslandSize = getConfig().getInt("IslandSize", 129);
        islandPositionManager.distance = IslandDistance + IslandSize;
        islandProtection.islandsize = IslandSize;
        islandManager.IslandSize = IslandSize;
        world = getConfig().getString("SkyworldName", "Skyblocks");

    }
}
