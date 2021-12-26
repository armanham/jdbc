package com.aca.db.connectionpool;

import java.util.LinkedList;
import java.util.Queue;

public abstract class AbstractObjectPool<T> implements ObjectPool<T> {
    private static final int DEFAULT_MIN_SIZE = 1;
    private static final int DEFAULT_MAX_SIZE = 1;
    private final int minSize;
    private final int maxSize;
    private final Queue<T> pool;
    private final Queue<T> usedObjects;
    private final Object lockObject = new Object();

    protected AbstractObjectPool() {
        this(DEFAULT_MIN_SIZE, DEFAULT_MAX_SIZE);
    }

    protected AbstractObjectPool(int minSize, int maxSize) {
        if (minSize < 0) {
            throw new IllegalArgumentException("MinSize can be only >= 0");
        }
        if (minSize > maxSize) {
            throw new IllegalArgumentException("MinSize can be only <= MaxSize");
        }
        if (maxSize <= 0) {
            throw new IllegalArgumentException("MaxSize can be only > 0");
        }
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.pool = new LinkedList<>();
        this.usedObjects = new LinkedList<>();
    }

    @Override
    public T get() {
        synchronized (lockObject) {
            if (isObjectAvailable()) {
                T obj = this.pool.poll();
                this.usedObjects.add(obj);
                return obj;
            }
            if (this.usedObjects.size() < maxSize) {
                T obj = createObject();
                this.usedObjects.add(obj);
                return obj;
            }
            try {
                lockObject.wait();
            } catch (InterruptedException ex) {
                throw new IllegalStateException("", ex);
            }
            T obj = this.pool.poll();
            this.usedObjects.add(obj);
            return obj;
        }
    }

    @Override
    public void release(final T obj) {
        synchronized (lockObject) {
            if (this.usedObjects.isEmpty() || !this.usedObjects.contains(obj)) {
                throw new IllegalStateException("Releasing invalid object.");
            }
            this.pool.add(obj);
            this.usedObjects.remove(obj);
            lockObject.notifyAll();
        }
    }


    protected void initializePool() {
        for (int i = 0; i < minSize; i++) {
            this.pool.add(createObject());
        }
    }

    private boolean isObjectAvailable() {
        return !this.pool.isEmpty();
    }

    protected abstract T createObject();
}

