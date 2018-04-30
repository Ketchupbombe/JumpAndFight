package de.ketchupbombe.module;

import de.ketchupbombe.world.MapManager;
import de.ketchupbombe.world.RelativeBlock;
import de.ketchupbombe.world.Selection;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class Module {

    @Getter
    private final ModuleDifficulty difficulty;
    @Getter
    private final Location location1;
    @Getter
    private final Location location2;
    @Getter
    private final int distance;
    @Getter
    private final String creator;
    @Getter
    private final ArrayList<RelativeBlock> blocks = new ArrayList<>();
    @Getter
    private RelativeBlock start, end, chest;
    @Getter
    private int lowestPoint = 256;
    @Getter
    private boolean enabled;

    Module(ModuleDifficulty difficulty, Location location1, Location location2, String creator, boolean enabled) {
        this.difficulty = difficulty;
        this.location1 = location1;
        this.location2 = location2;
        this.creator = creator;
        this.distance = MapManager.getLength(location1, location2);
        this.enabled = enabled;

        this.searchBlocks();
    }

    private void searchBlocks() {
        Selection selection = new Selection(location1, location2);
        List<Block> selectionList = selection.getBlocks();
        Block startBlock = null;
        //search for first redstone-block to set the start
        for (Block current : selectionList) {
            if (current.getType().equals(Material.REDSTONE_BLOCK)) {
                startBlock = current;
                break;
            }
        }

        boolean foundStart = false;
        boolean foundChest = false;

        Block lastEmerald = null;
        RelativeBlock lastEmeraldRelative = null;

        for (Block block : selectionList) {
            Material type = block.getType();

            //if nothing -> go on!
            if (type.equals(Material.AIR)) continue;

            //Emerald-block == end of module (only last block)
            if (type.equals(Material.EMERALD_BLOCK)) {
                lastEmerald = block;
                blocks.add(lastEmeraldRelative = MapManager.createRelativeBlock(startBlock, block));
                //Bedrock == chest (only first block)
            } else if (type.equals(Material.BEDROCK) && !foundChest) {
                chest = MapManager.createRelativeBlock(startBlock, block, Material.ENDER_CHEST);
                foundChest = true;
                //Redstone-block == start of module (only first block)
            } else if (type.equals(Material.REDSTONE_BLOCK) && !foundStart) {
                start = MapManager.createRelativeBlock(startBlock, block, Material.SMOOTH_BRICK);
                foundStart = true;
                //Gold-block == marker block -> area around module
            } else if (!isMarkerBlock(block) && !isDifficultyBlock(startBlock, block)) {
                RelativeBlock rb = MapManager.createRelativeBlock(startBlock, block);
                blocks.add(rb);
            }
        }
        //create checkpoint
        if (lastEmerald != null) {
            end = MapManager.createRelativeBlock(startBlock, lastEmerald, Material.GOLD_BLOCK);
            blocks.remove(lastEmeraldRelative);
            blocks.add(MapManager.createRelativeBlock(startBlock, lastEmerald.getRelative(BlockFace.UP), Material.IRON_PLATE));
        }

        //add chest
        if (chest != null) {
            blocks.add(0, chest);
        }
        //add start and end
        blocks.add(0, start);
        blocks.add(blocks.size(), end);

    }


    void createModule(Location startBlock) {
        for (RelativeBlock block : blocks)
            checkLowest(block.paste(startBlock));
    }

    private BlockState checkLowest(BlockState blockState) {
        checkLowest(blockState.getY());

        return blockState;
    }

    private void checkLowest(int y) {
        if (y < lowestPoint)
            lowestPoint = y;
    }

    private boolean isDifficultyBlock(Block startBlock, Block block) {
        return !(startBlock == null || block == null)
                && (startBlock.getRelative(BlockFace.NORTH).equals(block)
                || startBlock.getRelative(BlockFace.NORTH).getRelative(BlockFace.UP).equals(block));
    }

    private boolean isMarkerBlock(Block block) {
        Location blockLocation = block.getLocation();
        return blockLocation.equals(location1) || blockLocation.equals(location2);
    }

}
