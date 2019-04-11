package io.github.aavild;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class IslandPositionManager {
    World skyworld;
    private int distance = 64;
    Location location (int index, CommandSender sender)
    {
        int i = index % 4;
        int I = (int) Math.sqrt((double) index / 4);
        int Q = I * I + I;
        I += 1;
        int n = Q - index / 4;
        if (n >= 0)
        {
            switch (i)
            {
                case 1:
                    return new Location(skyworld, (I - n) * distance - distance / 2, 64, I * distance - distance / 2);
                case 2:
                    return new Location(skyworld, (I - n) * -1 * distance + distance / 2, 64, I * distance - distance / 2);
                case 3:
                    return new Location(skyworld, (I - n) * -1 * distance + distance / 2, 64, I * -1 * distance + distance / 2);
                default:
                    return new Location(skyworld, (I - n) * distance - distance / 2, 64, I * -1 * distance + distance / 2);
            }
        }
        else
        {
            switch (i)
            {
                case 1:
                    return new Location(skyworld, I * distance - distance / 2, 64, (I + n) * distance - distance / 2);
                case 2:
                    return new Location(skyworld, I * -1 * distance + distance / 2, 64, (I + n) * distance - distance / 2);
                case 3:
                    return new Location(skyworld, I * -1 * distance + distance / 2, 64, (I + n) * -1 * distance + distance / 2);
                default:
                    return new Location(skyworld, I * distance - distance / 2, 64, (I + n) * -1 * distance + distance / 2);
            }
        }
    }
}
