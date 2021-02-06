package io.github.aavild;

import java.util.Comparator;

class IslandComperator implements Comparator<Island> {
    public int compare(Island island1, Island island2) {
        if (island2 == null) {
            return 1;
        } else if (island1 == null) {
            return -1;
        }
        int islandValue1 = island1.value;
        int islandValue2 = island2.value;

        if (islandValue1 > islandValue2) {
            return 1;
        } else if (islandValue1 < islandValue2) {
            return -1;
        } else {
            return 0;
        }
    }
}
