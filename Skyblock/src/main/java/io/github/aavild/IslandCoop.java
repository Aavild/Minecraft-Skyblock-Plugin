package io.github.aavild;

import org.bukkit.entity.Player;

import java.util.UUID;

public class IslandCoop {
    Island island;
    UUID cooped;
    public IslandCoop(Island island, UUID cooped)
    {
        this.island = island;
        this.cooped = cooped;
    }
}
