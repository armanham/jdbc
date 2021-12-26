package com.aca.db.initializer;

import com.aca.db.connectionpool.ConnectionPool;

public interface DBInitializer {
    ConnectionPool initialize();
}
