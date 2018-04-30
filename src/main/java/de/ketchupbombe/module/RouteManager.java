package de.ketchupbombe.module;

import lombok.Getter;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class RouteManager {
    @Getter
    public static final HashMap<UUID, Route> playerRoutes = new HashMap<>();

    public static void setPlayerRoute(UUID uuid, Route route) {
        playerRoutes.put(uuid, route);
    }

    public static Route getPlayerRoute(UUID uuid) {
        return playerRoutes.get(uuid);
    }

    public static ArrayList<Route> createRoutes(Location startLocation, ArrayList<Module> modules, int routeAmount, int distanceBetweetRoutes) {
        ArrayList<Route> routes = new ArrayList<>();
        Location currentLocation = startLocation.clone();
        for (int i = 1; i <= routeAmount; i++) {
            Route route = new Route(modules);
            route.createRoute(currentLocation);
            currentLocation.setX(currentLocation.getX() + distanceBetweetRoutes);
            routes.add(route);
        }

        return routes;
    }

}
