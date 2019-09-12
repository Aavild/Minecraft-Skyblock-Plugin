package io.github.aavild;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class CustomChunkGenerator extends ChunkGenerator
{
    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome)
    {
        //Chunk generation code here.
        return createChunkData(world);
    }
}
