package com.carsales.app.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ApplicationConfig {
    private final String dbVendor;
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final String dbDriverClassName;

    private ApplicationConfig(Properties properties) {
        this.dbVendor = require(properties, "db.vendor");
        this.dbUrl = require(properties, "db.url");
        this.dbUsername = require(properties, "db.username");
        this.dbPassword = require(properties, "db.password");
        this.dbDriverClassName = require(properties, "db.driverClassName");
    }

    public static ApplicationConfig load() {
        Properties properties = new Properties();
        try (InputStream in = ApplicationConfig.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (in == null) {
                throw new IllegalStateException("application.properties not found in classpath");
            }
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
        return new ApplicationConfig(properties);
    }

    private static String require(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required config: " + key);
        }
        return value.trim();
    }

    public String getDbVendor() {
        return dbVendor;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbDriverClassName() {
        return dbDriverClassName;
    }
}
