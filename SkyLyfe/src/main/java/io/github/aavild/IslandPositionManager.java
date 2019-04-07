package io.github.aavild;

import org.bukkit.Location;
import org.bukkit.World;

public class IslandPositionManager {
    World skyworld;
    int listlength = 0;
    Location location () {
        int x = listlength / 2;
        int z = listlength / 2 + listlength % 2;
        // 1
        // x = 0 && z = 1

        // 2
        // x = 1 && z = 1

        // 3
        // x = 1 && z = 2

        // 4
        // x = 2 && z = 2

        // 5
        // x = 2 && z = 3


        int x = listlength / 4;
        int z = listlength / 4 + listlength % 4;
        // 1
        // x = 0 && z = 1

        // 2
        // x = 0 && z = 2

        // 3
        // x = 0 && z = 3

        // 4
        // x = 1 && z = 1

        // 5
        // x = 1 && z = 2
        double doubleroot = Math.sqrt((double) listlength - 1);
        int root = (int) doubleroot;
        // 0
        // root = 0

        // 1
        // root = 1

        // 2
        // root = 1

        // 3
        // root = 1

        // 4
        // root = 2

        // 5
        // root = 2

        // 6
        // root = 2

        // 7
        // root = 2

        // 8
        // root = 2

        // 9
        // root = 3

        // 10
        // root = 3
    }
}
