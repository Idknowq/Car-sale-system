package com.carsales.app.dao;

import java.sql.Connection;

public abstract class BaseDao {
    protected final Connection connection;

    protected BaseDao(Connection connection) {
        this.connection = connection;
    }
}
