package de.ketchupbombe.utils.mysql;

import de.ketchupbombe.JumpAndFight;
import lombok.Getter;

import java.sql.*;

/**
 * @author Ketchupbombe
 * @version 1.0
 */
public class MySQL {
    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    @Getter
    private Connection connection;

    public MySQL(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }


    public boolean connect() {
        if (!isConnected()) {
            try {
                //set connection
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoreconnect=true",
                        username, password);
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean close() {
        if (isConnected()) {
            try {
                //close connection
                connection.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public void update(String sql) {
        //update async
        JumpAndFight.getInstance().getExecutor().execute(() -> updateSync(sql));
    }

    public void updateSync(String sql) {
        try {
            //update sync
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResult(String qry) {
        //get Result sync
        if (isConnected()) {
            try {
                PreparedStatement ps = this.connection.prepareStatement(qry);
                return ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void loadMySQLTable() {
        //Modules Table
        this.update("CREATE TABLE IF NOT EXISTS jump_and_fight_modules(location1 VARCHAR(250), location2 VARCHAR(250), author VARCHAR(30), module_difficulty INT(1), enabled BOOLEAN);");
    }
}
