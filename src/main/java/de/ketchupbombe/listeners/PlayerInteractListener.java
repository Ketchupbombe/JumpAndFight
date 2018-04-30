package de.ketchupbombe.listeners;

import de.ketchupbombe.module.Route;
import de.ketchupbombe.module.RouteManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        //TODO: GAMPHASES!!!
        if (event.getAction() != Action.PHYSICAL) return;
        Block interactBlock = event.getClickedBlock();
        Block below = interactBlock.getRelative(BlockFace.DOWN);
        Route route = RouteManager.getPlayerRoute(player.getUniqueId());
        //handle start, checkpoint and goal
        if (route != null) {
            if (interactBlock.getType() == Material.IRON_PLATE && below.getType() == Material.GOLD_BLOCK) {
                route.onCheckpoint(interactBlock);
            } else if (interactBlock.getType() == Material.GOLD_PLATE && below.getType() == Material.REDSTONE_BLOCK) {
                route.onStart(interactBlock);
            } else if (interactBlock.getType() == Material.GOLD_PLATE && below.getType() == Material.EMERALD_BLOCK) {
                route.onEnd(interactBlock);
            }
        }
    }

}
