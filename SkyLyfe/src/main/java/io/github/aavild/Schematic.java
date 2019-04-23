package io.github.aavild;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class Schematic {
    Material[][][] skyblocks()
    {
        Material[][][] template = new Material[8][9][8];
        for (int x = 0; x < template.length; x++)
        {
            for (int y = 0; y < template[x].length; y++)
            {
                for (int z = 0; z < template[x][y].length; z++)
                {
                    if (y < 2 && x < 6 && z < 6)
                    {
                        if (z < 3)
                        {
                            if (x < 3)
                            {
                                template[x][y][z] = Material.DIRT;
                            }
                        }
                        else
                        {
                            template[x][y][z] = Material.DIRT;
                        }
                    }
                    else if (y == 2 && x < 6 && z < 6)
                    {
                        if (z < 3)
                        {
                            if (x < 3)
                            {
                                template[x][y][z] = Material.GRASS_BLOCK;
                            }
                        }
                        else
                        {
                            template[x][y][z] = Material.GRASS_BLOCK;
                        }
                    }
                    else if (x == 5 && z == 5)
                    {
                        if (y > 2)
                        {
                            if (y != 8)
                            {
                                template[x][y][z] = Material.OAK_LOG;
                            }
                            else
                            {
                                template[x][y][z] = Material.OAK_LEAVES;
                            }
                        }
                    }
                    else if (x > 2 && z > 2 && y > 4)
                    {
                        if (!(x == 3 && z == 3) && !(x == 7 && z == 3) && !(x == 7 && z == 7))
                        {
                            if (y < 7)
                            {
                                template[x][y][z] = Material.OAK_LEAVES;
                            }
                            else if (x > 3 && x < 7 && z > 3 && z < 7)
                            {
                                if (!(x == 4 && z == 4) && !(x == 6 && z == 4) && !(x == 4 && z == 6) && !(x == 6 && z == 6) || y != 8)
                                {
                                    template[x][y][z] = Material.OAK_LEAVES;
                                }
                            }
                        }
                        else if (x == 7 && z == 3 && y == 6)
                            template[x][y][z] = Material.OAK_LEAVES;
                    }


                    if (template[x][y][z] == null)
                    {
                        template[x][y][z] = Material.AIR;
                    }
                }
            }
        }
        template[1][3][0] = Material.CHEST;
        template[0][0][0] = Material.BEDROCK;
        return template;
    }
}
