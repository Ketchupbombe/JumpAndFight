package de.ketchupbombe.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class LocationWrapper {

    public static String locationToString(Location location) {
        String world = location.getWorld().getName();
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        return (world + ":") +
                x + ":" +
                y + ":" +
                z + ":" +
                yaw + ":" +
                pitch;
    }

    public static Location stringToLocation(String string) {
        String[] values = string.split(":");
        World world = Bukkit.getWorld(values[0]);
        double x = Double.parseDouble(values[1]);
        double y = Double.parseDouble(values[2]);
        double z = Double.parseDouble(values[3]);
        float yaw = Float.parseFloat(values[4]);
        float pitch = Float.parseFloat(values[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }

}
