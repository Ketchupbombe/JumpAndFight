package de.ketchupbombe.world;

import de.ketchupbombe.JumpAndFight;
import net.minecraft.server.v1_8_R3.TileEntity;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.generator.ChunkGenerator;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class MapManager {


    public static int getLength(Location location1, Location location2) {
        int z1 = Math.abs(location1.getBlockZ());
        int z2 = Math.abs(location2.getBlockZ());
        return Math.abs(z1 - z2);
    }

    @SuppressWarnings("deprecation")
    public static RelativeBlock createRelativeBlock(Block origin, Block relative) {
        return createRelativeBlock(origin, relative, relative.getType(), relative.getData());
    }

    public static RelativeBlock createRelativeBlock(Block origin, Block relative, Material newMaterial) {
        return createRelativeBlock(origin, relative, newMaterial, (byte) 0);
    }

    public static RelativeBlock createRelativeBlock(Block origin, Block relative, Material mat, byte data) {
        try {
            Location ob = origin.getLocation(), rb = relative.getLocation();
            int obx = ob.getBlockX(), rbx = rb.getBlockX();
            int oby = ob.getBlockY(), rby = rb.getBlockY();
            int obz = ob.getBlockZ(), rbz = rb.getBlockZ();
            TileEntity tileEntity = ((CraftWorld) origin.getWorld()).getTileEntityAt(rbx, rby, rbz);
            int addx = rbx - obx, addy = rby - oby, addz = rbz - obz;
            return new RelativeBlock(mat, data, tileEntity, addx, addy, addz);
        } catch (Exception e) {
            return null;
        }
    }

    public static void createWorld(String worldname, WorldType worldType, ChunkGenerator chunkGenerator, boolean autoSave) {
        if (!doesWorldExist(worldname)) {
            //World creator
            WorldCreator creator = new WorldCreator(worldname);
            //setting environment to normal
            creator.environment(World.Environment.NORMAL);
            if (worldType != null)
                creator.type(worldType);
            creator.generateStructures(false);
            if (chunkGenerator != null)
                creator.generator(chunkGenerator);
            World world = creator.createWorld();
            world.setAutoSave(false);
        }

    }

    public static boolean doesWorldExist(String worldname) {
        return JumpAndFight.getInstance().getServer().getWorld(worldname) != null;
    }

    public static World createEmptyWorld(String name, boolean save) {
        createWorld(name, WorldType.FLAT, new EmptyMapGenerator(), save);
        World world = Bukkit.getWorld(name);
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setTime(6000);
        return world;
    }

}
