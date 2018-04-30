package de.ketchupbombe.module;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class BufferedModule {
    @Getter
    @Setter
    private Location location1;
    @Getter
    @Setter
    private Location location2;
    @Getter
    private final String author;
    @Getter
    @Setter
    private boolean enabled = false;
    @Getter
    private ModuleDifficulty difficulty;

    public BufferedModule(String author) {
        this.author = author;
    }

    public boolean canCreate() {
        return ((location1 != null) && (location2 != null) && (difficulty != null));
    }

    public void setStartBlock(Block block) {
        //set difficulty
        this.difficulty = ModuleManager.getModuleDifficulty(block);
    }

    public Module save() {
        if (canCreate()) {
            ModuleManager.storeModule(this);

            return new Module(difficulty, location1, location2, author, enabled);
        }
        //TODO: create own exceptions
        return null;
    }

}
