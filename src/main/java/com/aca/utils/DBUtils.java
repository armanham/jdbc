package com.aca.utils;

import java.sql.Connection;
import java.sql.SQLException;

public class DBUtils {

    public DBUtils() {
        throw new UnsupportedOperationException();
    }

    public static void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException ignored) {

            }
        }
    }
}
