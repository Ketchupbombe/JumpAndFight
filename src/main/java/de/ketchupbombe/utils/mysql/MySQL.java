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
        JumpAndFight.getInstance().getExecutor().execute(() -> updateSync(sql));
    }

    public void updateSync(String sql) {
        try {
            PreparedStatement ps = this.connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResult(String qry) {
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
}
