package io.github.aavild;

import org.bukkit.Location;
import org.bukkit.World;

public class IslandPositionManager {
    World skyworld;
    int TotalIslandSize = 134;
    Location location (int index)
    {
        int i = index % 4; //returns either 0, 1, 2 or 3 which is used to deterrmine quadrant
        int I = (int) Math.sqrt((double) index / 4); //0, 1, 2, 3 return 0 which is the first 4 center islands then 4 - 15 returns 1 which is 12 in total + 4 is the 16 center islands and so on
        int n = I * I + I - index / 4; //n is the special quadrant number, index 0, 1, 2, 3 will return 0 marking in the first quadrant. 5 - 7 will return 1, 8 - 11 will return 0,
        // 12 - 15 will return -1. This is used to calculate x and y coords. 0 is the base coord which will go like this: 0,0 -> 1,1 -> 2,2 while -1 will go according like this: (starting with 2,2) 2,1 -> 3,2 -> 4,3. -1 will work
        // likewise just with the x-axis instead
        I += 1; //This will mark the first island in (1,1), (1,-1) and so on. then later on (2,2), (2,-2) and so on.
        if (n >= 0) //When n is 0 it doesnt really matter since it will neither nor remove from the standard placement and therefore is not handled
        {
            switch (i) //These switch functions are for each quadrant. The difference between these are dependant on whether n should be reducing on the x axis or the y axis
            {
                case 1: //This calculates the actual location according to the distance. ex distance = 139 for the (1,1) then I will be 1 and n be 0. x will be (70, 70)
                    return new Location(skyworld, (I - n) * TotalIslandSize - TotalIslandSize / 2, 64, I * TotalIslandSize - TotalIslandSize / 2);
                case 2: //Here it will be for another quadrant: (-70, 70)
                    return new Location(skyworld, -1 * (I - n) * TotalIslandSize + TotalIslandSize / 2, 64, I * TotalIslandSize - TotalIslandSize / 2);
                case 3: //Third quadrant: (-70, -70)
                    return new Location(skyworld, -1 * (I - n) * TotalIslandSize + TotalIslandSize / 2, 64, I * -1 * TotalIslandSize + TotalIslandSize / 2);
                default: //Last quadrant: (70, -70)
                    return new Location(skyworld, (I - n) * TotalIslandSize - TotalIslandSize / 2, 64, -1 * I * TotalIslandSize + TotalIslandSize / 2);
            }
        }
        else
        {
            switch (i) //This switch will not happen with any of the (1,1) however for second quadrant it'll happen. In the switch above n was removed from I on the x axis however here it'll be removed from the Y axis
            { //in the (2, 2) n will only be negative from 12 - 15, 12 will be default, 13 case 1, 14 case 2, 15 case 3
                case 1: //for index 13 this one will be run. the main coords would be (2, 2) (209, 209) 209 is 140 + 69 however 13 will make n be -1. this will give (209, 70)
                    return new Location(skyworld, I * TotalIslandSize - TotalIslandSize / 2, 64, (I + n) * TotalIslandSize - TotalIslandSize / 2);
                case 2: //like the other switch case this just shuffles whether x and y is positive or not, (-209, 70)
                    return new Location(skyworld, -1 * I * TotalIslandSize + TotalIslandSize / 2, 64, (I + n) * TotalIslandSize - TotalIslandSize / 2);
                case 3:// (-209, -70)
                    return new Location(skyworld, -1 * I * TotalIslandSize + TotalIslandSize / 2, 64,  -1 * (I + n) * TotalIslandSize + TotalIslandSize / 2);
                default:// (209, -70)
                    return new Location(skyworld, I * TotalIslandSize - TotalIslandSize / 2, 64,  -1 * (I + n) * TotalIslandSize + TotalIslandSize / 2);
            }
        }
    }
}
