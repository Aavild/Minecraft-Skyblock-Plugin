package io.github.aavild;

import org.bukkit.plugin.java.JavaPlugin;

public class SkyLyfeMain extends JavaPlugin {
    private Commands commands = new Commands();
    @Override
    public void onEnable() {
        for (String command : commands.cmds)
            getCommand(command).setExecutor(commands);
    }
    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }
}
