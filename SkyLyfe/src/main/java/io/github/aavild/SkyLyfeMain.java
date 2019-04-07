package io.github.aavild;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class SkyLyfeMain extends JavaPlugin {
    private Commands commands = new Commands();
    private IslandManager islandManager = new IslandManager();
    private Schematic schematic = new Schematic();
    private IslandPositionManager islandPositionManager = new IslandPositionManager();
    @Override
    public void onEnable() {
        for (String command : commands.cmds)
            getCommand(command).setExecutor(commands);
        commands.islandManager = islandManager;
        commands.schematic = schematic;
        WorldCreator wc = new WorldCreator("Skyblocks");
        wc.generator(new CustomChunkGenerator());
        World skyworld = Bukkit.createWorld(wc);
        commands.skyworld = skyworld;
        islandManager.skyworld = skyworld;
        islandPositionManager.skyworld = skyworld;
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }


    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new CustomChunkGenerator();
    }
}
