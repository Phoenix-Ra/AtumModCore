package me.phoenixra.atumodcore.api.database;


import lombok.Getter;
import me.phoenixra.atumodcore.api.AtumMod;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * EXPERIMENTAL
 */
public class SQLiteDatabase implements Database {

    @Getter
    private final AtumMod atumMod;
    private final Logger log;
    private Connection connection;
    private final String dbLocation;
    private final String dbName;
    private File file;

    /**
     * SQLite Database.
     * <br><br>
     * Tries to connect to the database using the given parameters
     * after the creation of an instance
     *
     * @param atumMod    The mod instance
     * @param dbName     The database name
     * @param dbLocation The database file location
     */
    public SQLiteDatabase(AtumMod atumMod, String dbName, String dbLocation) {
        this.atumMod = atumMod;
        this.dbName = dbName;
        this.dbLocation = dbLocation;
        this.log = getAtumMod().getLogger();
        initialize();
    }

    private boolean initialize() {
        if (file == null) {
            File dbFolder = new File(dbLocation);

            if (!dbFolder.exists() && !dbFolder.mkdir()) {
                log.error("Failed to create database folder!");
                return false;
            }

            file = new File(dbFolder.getAbsolutePath() + File.separator + dbName + ".db");
        }

        try {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
            return true;
        } catch (SQLException ex) {
            log.error("SQLite exception on initialize " + ex);
        } catch (ClassNotFoundException ex) {
            log.error("You need the SQLite library " + ex);
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