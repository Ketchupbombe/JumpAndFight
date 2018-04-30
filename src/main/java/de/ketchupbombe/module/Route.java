package de.ketchupbombe.module;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class Route {

    private final ArrayList<Module> modules;
    private final ArrayList<Location> checkpoints;

    private Player player;
    private boolean reachedGoal = false;
    private Location lastCheckpoint, start, end;
    private int currentModule;


    public Route(ArrayList<Module> modules) {
        this.modules = modules;
        checkpoints = new ArrayList<>();
    }

    public void createRoute(Location startLocation) {
        Location origin = startLocation.clone();
        //go through each module
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            module.createModule(origin);
            origin = module.getEnd().getRelativeLocation().clone().add(0, 0, 3);

            //handle first block for start
            if (i == 0) {
                Block startBlock = module.getStart().getRelativeBlock();
                startBlock.setType(Material.REDSTONE_BLOCK);
                Block startPressure = startBlock.getRelative(BlockFace.UP);
                startPressure.setType(Material.GOLD_PLATE);
                this.start = startPressure.getLocation().clone();
                this.checkpoints.add(start);
            }

            //handle last block for end
            if (i == (modules.size() - 1)) {
                Block endBlock = module.getEnd().getRelativeBlock();
                endBlock.setType(Material.EMERALD_BLOCK);
                Block endPressure = endBlock.getRelative(BlockFace.UP);
                endPressure.setType(Material.GOLD_PLATE);
                this.end = endPressure.getLocation().clone();
            }
            //add module checkpoints
            checkpoints.add(module.getEnd().getRelativeBlock().getRelative(BlockFace.UP).getLocation());
        }
    }

    //add player to this route an register him
    public void appendPlayer(Player player) {
        this.player = player;
        RouteManager.setPlayerRoute(player.getUniqueId(), this);
    }

    public void onStart(Block interactBlock) {
        //yet: nothing different
        onCheckpoint(interactBlock);
    }

    //called while Player reach a checkpoint
    public void onCheckpoint(Block interactBlock) {
        if (this.reachedGoal) return;
        int checkpointIndex = this.checkpoints.indexOf(interactBlock.getLocation());
        if (checkpointIndex == -1) return;
        int oldCheckpointIndex = this.checkpoints.indexOf(this.lastCheckpoint);
        if (oldCheckpointIndex <= checkpointIndex) {
            this.lastCheckpoint = interactBlock.getLocation().clone();
            this.currentModule = checkpointIndex;

        }
    }

    //called if Player reached goal
    public void onEnd(Block interactBlock) {
        if (!reachedGoal) {
            this.lastCheckpoint = interactBlock.getLocation().clone();
            reachedGoal = true;
        }
    }

    //called always while Player is moving
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        //handle if player is falling
        if (player.getLocation().getY() < this.modules.get(this.currentModule).getLowestPoint() - 7) {
            this.teleportToLastCheckpoint();
        }
    }

    private void teleportToLastCheckpoint() {
        if (this.lastCheckpoint != null)
            this.player.teleport(this.lastCheckpoint.clone().add(0.5, 0, 0.5));
    }

}
