package de.ketchupbombe.listeners;

import de.ketchupbombe.module.Route;
import de.ketchupbombe.module.RouteManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class PlayerMoveListener implements Listener {


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        //TODO: GAMPHASES!!!
        Player player = event.getPlayer();
        Route route = RouteManager.getPlayerRoute(player.getUniqueId());
        //handle while Player is moving
        if (route != null) {
            route.onMove(event);
        }
    }


}
