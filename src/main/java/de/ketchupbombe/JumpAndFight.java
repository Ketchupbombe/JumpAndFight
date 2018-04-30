package de.ketchupbombe;

import de.ketchupbombe.commands.JumpAndFightCommand;
import de.ketchupbombe.listeners.PlayerInteractListener;
import de.ketchupbombe.listeners.PlayerMoveListener;
import de.ketchupbombe.utils.Constants;
import de.ketchupbombe.utils.mysql.MySQL;
import de.ketchupbombe.world.MapManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class JumpAndFight extends JavaPlugin {

    //instance
    @Getter
    private static JumpAndFight instance;
    //MySQL
    @Getter
    private MySQL mySQL;
    //executor
    @Getter
    private Executor executor = Executors.newCachedThreadPool();

    //Startup Method
    @Override
    public void onEnable() {
        instance = this;

        //load files
        this.saveDefaultConfig();

        //setup MySQL
        this.setupMySQL();

        //register Listener and Commands
        registerListener();
        registerCommands();
        MapManager.createEmptyWorld("world_modules", true);
    }

    @Override
    public void onDisable() {
        if (this.mySQL.close())
            Bukkit.getConsoleSender().sendMessage(Constants.PREFIX + Constants.MYSQL_PREFIX + "§aMySQL connection closed");
    }

    private void setupMySQL() {
        String host = this.getConfig().getString("MySQL.host");
        int port = this.getConfig().getInt("MySQL.port");
        String database = this.getConfig().getString("MySQL.database");
        String username = this.getConfig().getString("MySQL.username");
        String password = this.getConfig().getString("MySQL.password");
        this.mySQL = new MySQL(host, port, database, username, password);
        if (this.mySQL.connect()) {
            Bukkit.getConsoleSender().sendMessage(Constants.PREFIX + Constants.MYSQL_PREFIX + "§aMySQL connected!");
            this.mySQL.loadMySQLTable();
        } else {
            Bukkit.getConsoleSender().sendMessage(Constants.PREFIX + Constants.MYSQL_PREFIX + "§cMySQL could not connect! " +
                    "Please insert right data in config.yml! + \n Shutting down...");
            Bukkit.shutdown();
        }
    }

    private void registerListener() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerInteractListener(), this);
        pm.registerEvents(new PlayerMoveListener(), this);
    }
    private void registerCommands() {
        this.getCommand("JumpAndFight").setExecutor(new JumpAndFightCommand());
    }

}
