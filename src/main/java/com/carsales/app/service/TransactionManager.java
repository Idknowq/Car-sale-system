package com.carsales.app.service;

import com.carsales.app.config.ApplicationConfig;
import com.carsales.app.config.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private final ApplicationConfig config;

    public TransactionManager(ApplicationConfig config) {
        this.config = config;
    }

    public <T> T executeInTransaction(TransactionCallback<T> callback) throws Exception {
        try (Connection connection = ConnectionFactory.getConnection(config)) {
            boolean oldAutoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);
            try {
                T result = callback.doInTransaction(connection);
                connection.commit();
                return result;
            } catch (Exception ex) {
                connection.rollback();
                throw ex;
            } finally {
                connection.setAutoCommit(oldAutoCommit);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Transaction failed", e);
        }
    }
}
