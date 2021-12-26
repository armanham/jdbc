package com.aca.utils;

public final class IOUtils {
    public static void closeAll(AutoCloseable... closeables) {
        if (closeables == null || closeables.length == 0) {
            return;
        }
        for (AutoCloseable closable : closeables) {
            if (closeables != null) {
                try {
                    closable.close();
                } catch (Exception ignored) {

                }
            }
        }
    }

    private IOUtils() {
        throw new UnsupportedOperationException();
    }
}
