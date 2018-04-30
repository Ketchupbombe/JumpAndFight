package de.ketchupbombe.commands;

import de.ketchupbombe.module.*;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class JumpAndFightCommand implements CommandExecutor {

    /*

        Command currently only used for tests!!!

     */

    private HashMap<UUID, BufferedModule> creatingModules = new HashMap<>();
    private ArrayList<Route> routes = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            BufferedModule module;
            if (creatingModules.containsKey(player.getUniqueId())) {
                module = creatingModules.get(player.getUniqueId());
                player.sendMessage("modules exists already");
            } else {
                module = new BufferedModule(player.getName());
                creatingModules.put(player.getUniqueId(), module);
                player.sendMessage("module created");
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("pos1")) {
                    Block block = getNextBlock(player);
                    if (block.getType() == Material.GOLD_BLOCK) {
                        module.setLocation1(block.getLocation());
                        player.sendMessage("pos1 set");
                    }
                }
                if (args[0].equalsIgnoreCase("pos2")) {
                    Block block = getNextBlock(player);
                    if (block.getType() == Material.GOLD_BLOCK) {
                        module.setLocation2(block.getLocation());
                        player.sendMessage("pos2 set");
                    }
                }
                if (args[0].equalsIgnoreCase("start")) {
                    Block block = getNextBlock(player);
                    if (block.getType() == Material.REDSTONE_BLOCK) {
                        module.setStartBlock(block);
                        player.sendMessage("start set");
                    }
                }
                if (args[0].equalsIgnoreCase("save")) {
                    module.setEnabled(true);
                    if (module.canCreate()) {
                        module.save();
                        player.sendMessage(module.getLocation1().toString() + " \n " + module.getLocation2() + " \n " + module.getDifficulty());
                        creatingModules.remove(player.getUniqueId());
                    }
                }
                if (args[0].equalsIgnoreCase("join")) {
                    Route usersRout = routes.get(0);
                    usersRout.appendPlayer(player);
                }
                if (args[0].equalsIgnoreCase("createRoute")) {
                    ArrayList<Module> modules = new ArrayList<>();
                    modules.addAll(ModuleManager.getModules(ModuleDifficulty.EASY, 1));
                    modules.addAll(ModuleManager.getModules(ModuleDifficulty.MEDIUM, 1));
                    modules.addAll(ModuleManager.getModules(ModuleDifficulty.HARD, 1));
                    routes = RouteManager.createRoutes(player.getLocation(), modules, 2, 40);
                }
            }
        }
        return false;
    }

    private Block getNextBlock(Player player) {
        BlockIterator iterator = new BlockIterator(player, 5);
        Block firstBlock = iterator.next();
        while (iterator.hasNext()) {
            firstBlock = iterator.next();
            if (firstBlock.getType() == Material.AIR) continue;
            break;
        }
        return firstBlock;
    }

}
