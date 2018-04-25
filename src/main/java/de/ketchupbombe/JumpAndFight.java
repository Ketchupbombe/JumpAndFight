package de.ketchupbombe;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class JumpAndFight extends JavaPlugin {

    @Getter
    private static JumpAndFight instance;

    @Override
    public void onEnable() {
        instance = this;
    }
}
