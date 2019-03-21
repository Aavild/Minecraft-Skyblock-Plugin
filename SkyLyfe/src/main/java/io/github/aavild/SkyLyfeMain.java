package io.github.aavild;

import org.bukkit.plugin.java.JavaPlugin;

public class SkyLyfeMain extends JavaPlugin {
    private Commands commands = new Commands();
    private IslandManager islandManager = new IslandManager();
    @Override
    public void onEnable() {
        for (String command : commands.cmds)
            getCommand(command).setExecutor(commands);
        commands.islandManager = islandManager;
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }
}
