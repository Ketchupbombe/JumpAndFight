package de.ketchupbombe.world;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.TileEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
@EqualsAndHashCode
public class RelativeBlock {

    @Setter
    private Material material;
    private final byte data;

    private final int addX;
    private final int addY;
    private final int addZ;

    @Getter
    private Location relativeLocation;
    private NBTTagCompound nbtTagCompound;

    public RelativeBlock(Material material, byte data, TileEntity tileEntity, int addX, int addY, int addZ) {
        this.material = material;
        this.data = data;
        this.addX = addX;
        this.addY = addY;
        this.addZ = addZ;

        if(tileEntity != null) {
            this.nbtTagCompound = new NBTTagCompound();
            tileEntity.b(nbtTagCompound);
        }
    }

    @SuppressWarnings("deprecation")
    public BlockState paste(Location origin) {
        int ox = origin.getBlockX(), oy = origin.getBlockY(), oz = origin.getBlockZ();
        Block topaste = origin.getWorld().getBlockAt(ox + addX, oy + addY, oz + addZ);
        relativeLocation = topaste.getLocation();
        topaste.setTypeIdAndData(material.getId(), data, false);

        TileEntity tilePaste = ((CraftWorld) origin.getWorld()).getTileEntityAt(topaste.getX(), topaste.getY(), topaste.getZ());
        if (tilePaste != null && nbtTagCompound != null) {
            nbtTagCompound.setInt("x", topaste.getX());
            nbtTagCompound.setInt("y", topaste.getY());
            nbtTagCompound.setInt("z", topaste.getZ());

            tilePaste.a(nbtTagCompound);
        }
        topaste.getState().update(false, false);

        if (relativeLocation.getY() >= 256)
            throw new RuntimeException("Module height is over max build height");

        return topaste.getState();
    }

    public Block getRelativeBlock(){
        return relativeLocation.getBlock();
    }
}
