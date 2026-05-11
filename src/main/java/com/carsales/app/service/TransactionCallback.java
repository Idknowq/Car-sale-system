package com.carsales.app.service;

import java.sql.Connection;

@FunctionalInterface
public interface TransactionCallback<T> {
    T doInTransaction(Connection connection) throws Exception;
}
