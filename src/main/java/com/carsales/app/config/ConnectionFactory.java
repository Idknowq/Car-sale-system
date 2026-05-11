package com.carsales.app.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {
    private ConnectionFactory() {
    }

    public static Connection getConnection(ApplicationConfig config) throws SQLException {
        validateVendor(config.getDbVendor());
        try {
            Class.forName(config.getDbDriverClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JDBC driver not found: " + config.getDbDriverClassName(), e);
        }
        return DriverManager.getConnection(
                config.getDbUrl(),
                config.getDbUsername(),
                config.getDbPassword()
        );
    }

    private static void validateVendor(String vendor) {
        if (!"opengauss".equalsIgnoreCase(vendor) && !"postgres".equalsIgnoreCase(vendor)) {
            throw new IllegalStateException("Unsupported db.vendor: " + vendor + ". Expected: opengauss or postgres");
        }
    }
}
