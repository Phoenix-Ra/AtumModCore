package me.phoenixra.atumodcore.api.database;


import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * EXPERIMENTAL
 */
public class MySQLDatabase implements Database {
    @Getter
    private final AtumMod atumMod;
    private final Logger log;
    private Connection connection;
    private final String host;
    private final String username;
    private final String password;
    private final String database;
    private final int port;

    /**
     * MySQL Database.
     * <br><br>
     * Tries to connect to the database using the given parameters
     * after the creation of an instance
     *
     * @param atumMod  The mod instance
     * @param host     The server host
     * @param port     The server port
     * @param database The database
     * @param username The username
     * @param password The password
     */
    public MySQLDatabase(AtumMod atumMod, String host, int port, String database, String username, String password) {
        this.atumMod = atumMod;
        this.database = database;
        this.port = port;
        this.host = host;
        this.username = username;
        this.password = password;
        this.log = atumMod.getLogger();
        initialize();
    }

    private boolean initialize() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false", username, password);
            return true;
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundException! " + e.getMessage());
        } catch (SQLException e) {
            log.error("SQLException! " + e.getMessage());
        }
        return false;
    }

    @Override
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(0)) {
                initialize();
            }
        } catch (SQLException e) {
            initialize();
        }
        return connection;
    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            log.error("Failed to close database connection! " + e.getMessage());
        }
    }


}