package com.carsales.app;

import com.carsales.app.config.ApplicationConfig;
import com.carsales.app.config.DatabaseHealthChecker;
import com.carsales.app.ui.ConsoleMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        try {
            ApplicationConfig config = ApplicationConfig.load();
            DatabaseHealthChecker.checkConnection(config);

            ConsoleMenu menu = new ConsoleMenu();
            menu.start();
        } catch (Exception e) {
            log.error("Application startup failed", e);
            System.exit(1);
        }
    }
}
