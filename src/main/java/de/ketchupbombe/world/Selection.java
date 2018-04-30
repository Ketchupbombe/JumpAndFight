package de.ketchupbombe.world;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class Selection implements Iterable<Block> {
    @Getter
    private final Location max;
    @Getter
    private final Location min;

    public Selection(Location min, Location max) {
        this.max = new Location(min.getWorld(), Math.max(min.getX(), max.getX()), Math.max(min.getY(), max.getY()), Math.max(min.getZ(), max.getZ()));
        this.min = new Location(min.getWorld(), Math.min(min.getX(), max.getX()), Math.min(min.getY(), max.getY()), Math.min(min.getZ(), max.getZ()));
    }
    public ArrayList<Block> getBlocks() {
        ArrayList<Block> blocks = new ArrayList<>();
        World w = this.max.getWorld();
        int x = this.min.getBlockX();
        int y = this.min.getBlockY();
        int z = this.min.getBlockZ();
        int ex = this.max.getBlockX() + 1;
        int ey = this.max.getBlockY() + 1;

        for(int ez = this.max.getBlockZ() + 1; z < ez; y = this.min.getBlockY()) {
            while(y < ey) {
                while(x < ex) {
                    blocks.add((new Location(w, (double)x, (double)y, (double)z)).getBlock());
                    ++x;
                }

                ++y;
                x = this.min.getBlockX();
            }

            ++z;
        }

        return blocks;
    }

    public Iterator<Block> iterator() {
        return new Selection.BlockIterator(this.getBlocks());
    }

    private class BlockIterator implements Iterator<Block> {
        private int i = 0;
        private final List<Block> blocks;

        public BlockIterator(List<Block> blocks) {
            this.blocks = blocks;
        }

        public boolean hasNext() {
            return this.i < this.blocks.size();
        }

        public Block next() {
            Block b = this.blocks.get(this.i);
            ++this.i;
            return b;
        }

        public void remove() {
            this.blocks.remove(this.i);
            --this.i;
        }
    }
}
