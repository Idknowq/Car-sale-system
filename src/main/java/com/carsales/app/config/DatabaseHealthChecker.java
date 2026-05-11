package com.carsales.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class DatabaseHealthChecker {
    private static final Logger log = LoggerFactory.getLogger(DatabaseHealthChecker.class);

    private DatabaseHealthChecker() {
    }

    public static void checkConnection(ApplicationConfig config) {
        try (Connection conn = ConnectionFactory.getConnection(config);
             PreparedStatement ps = conn.prepareStatement("SELECT 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                log.info("Database connectivity check passed. db.vendor={}", config.getDbVendor());
            } else {
                throw new IllegalStateException("Database connectivity check failed: no result from SELECT 1");
            }
        } catch (Exception e) {
            throw new RuntimeException("Database connectivity check failed", e);
        }
    }
}
