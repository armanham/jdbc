package com.aca.db.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPool extends AbstractObjectPool<Connection> {

    private final String url;

    private final Properties properties;

    public ConnectionPool(String url, Properties properties) {
        this.url = url;
        this.properties = properties;

        this.initializePool();
    }

    @Override
    protected Connection createObject() {
        try {
            return DriverManager.getConnection(url, properties);
        } catch (SQLException e) {
            throw new IllegalStateException("Create SQL connection field", e);
        }
    }
}
