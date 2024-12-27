package ru.sfedu.tripshelperpack.utils;

import ru.sfedu.tripshelperpack.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    private static final String DB_URL = ConfigurationUtil.getConfigurationEntry(Constants.DB_URL);
    private static final String DB_USER = ConfigurationUtil.getConfigurationEntry(Constants.DB_USER).trim();
    private static final String DB_PASSWORD = ConfigurationUtil.getConfigurationEntry(Constants.DB_PASSWORD).trim();

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver не найден", e);
        }
    }

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}