package de.ketchupbombe.world;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class EmptyMapGenerator extends ChunkGenerator {

    @SuppressWarnings("deprecation")
    @Override
    public byte[][] generateBlockSections(World world, Random random, int x, int z, BiomeGrid biomes) {
        return new byte[world.getMaxHeight() / 16][];
    }
}
