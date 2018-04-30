package de.ketchupbombe.module;

import de.ketchupbombe.JumpAndFight;
import de.ketchupbombe.utils.LocationWrapper;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class ModuleManager {

    static void storeModule(BufferedModule module) {
        //load data from BufferedModule
        String location1 = LocationWrapper.locationToString(module.getLocation1());
        String location2 = LocationWrapper.locationToString(module.getLocation2());
        int difficulty = module.getDifficulty().getValue();
        String author = module.getAuthor();
        boolean enabled = module.isEnabled();
        //insert data into database
        JumpAndFight.getInstance().getMySQL().update("INSERT INTO jump_and_fight_modules(location1, location2, author, module_difficulty, enabled) " +
                "VALUES ('" + location1 + "','" + location2 + "','" + author + "'," + difficulty + "," + enabled + ");");
    }

    public static ArrayList<Module> getModules(ModuleDifficulty difficulty, int amount) {
        ArrayList<Module> modules = new ArrayList<>();
        try {
            //get modules by difficulty and with limit
            ResultSet resultSet = JumpAndFight.getInstance().getMySQL().getResult("SELECT * FROM jump_and_fight_modules WHERE enabled=" + true + " " +
                    "AND module_difficulty=" + difficulty.getValue() + " ORDER BY RAND() LIMIT " + amount + ";");
            while (resultSet.next()) {
                Location location1 = LocationWrapper.stringToLocation(resultSet.getString("location1"));
                Location location2 = LocationWrapper.stringToLocation(resultSet.getString("location2"));
                String author = resultSet.getString("author");
                modules.add(new Module(difficulty, location1, location2, author, true));
            }
        } catch (SQLException ignored) {

        }
        //return searching modules
        return modules;
    }

    protected static ModuleDifficulty getModuleDifficulty(Block startBlock) {
        //BlockFace North == player looking into south!
        Block signBlock = startBlock.getRelative(BlockFace.NORTH);
        if (signBlock.getState() instanceof Sign) {
            Sign sign = (Sign) signBlock.getState();
            String difficulty = sign.getLine(0);
            switch (difficulty.toUpperCase()) {
                case "EASY":
                    return ModuleDifficulty.EASY;
                case "MEDIUM":
                    return ModuleDifficulty.MEDIUM;
                case "HARD":
                    return ModuleDifficulty.HARD;
                default:
                    return null;
            }
        }
        return null;
    }

}
