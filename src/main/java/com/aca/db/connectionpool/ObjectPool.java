package com.aca.db.connectionpool;

public interface ObjectPool<T> {
    T get();

    void release(T obj);
}


