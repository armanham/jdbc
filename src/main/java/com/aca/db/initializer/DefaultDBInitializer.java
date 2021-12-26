package com.aca.db.initializer;

import com.aca.db.connectionpool.ConnectionPool;
import com.aca.utils.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DefaultDBInitializer implements DBInitializer {
    private static final String URL = "url";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final String EQUAL = "=";

    private static final String DB_FILE_PATH = "D:\\IntellIjProjects\\jdbc1\\src\\main\\resources\\db-props.txt\\";

    @Override
    public ConnectionPool initialize() {
        Map<String, String> dbProperties = getDBProperties();
        Properties properties = new Properties();
        properties.setProperty(USERNAME, dbProperties.get(USERNAME));
        properties.setProperty(PASSWORD, dbProperties.get(PASSWORD));

        return new ConnectionPool(getDBProperties().get(URL), properties);
    }

    private Map<String, String> getDBProperties() {
        File file = new File(DB_FILE_PATH);
        if (!file.exists()) {
            throw new IllegalStateException();
        }
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        Map<String, String> props = new HashMap<>();

        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                int index = line.indexOf(EQUAL);
                if (index > 0) {
                    props.put(line.substring(0, index), line.substring(index + 1));
                }
            }
            return props;
        } catch (Exception e) {
            throw new IllegalStateException("Init DB properties failed", e);
        } finally {
            IOUtils.closeAll(fileReader, bufferedReader);
        }
    }
}
