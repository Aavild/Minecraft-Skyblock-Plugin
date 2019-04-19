package io.github.aavild;

import org.bukkit.entity.Player;

import java.io.Serializable;

public class IslandCoopInvite {
    Player player;
    Island island;
    public IslandCoopInvite(Player player, Island island)
    {
        this.player = player;
        this.island = island;
    }
}
