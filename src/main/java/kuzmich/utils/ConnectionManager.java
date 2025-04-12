package kuzmich.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class ConnectionManager {
    private static final String DRIVER_KEY = "db.driver";
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final String TEST_KEY = "test";
    private static HikariDataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            initConnectionPool();
        }
        return dataSource;
    }

    private static void initConnectionPool() {
        String testScope = System.getProperty(TEST_KEY);
        if (testScope != null && testScope.equals("true")) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(System.getProperty("url"));
            config.setUsername(System.getProperty("user"));
            config.setPassword(System.getProperty("pass"));
            config.setDriverClassName(System.getProperty("driver"));
            dataSource = new HikariDataSource(config);
            return;
        }

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(PropertiesUtil.get(DRIVER_KEY));
        config.setJdbcUrl(PropertiesUtil.get(URL_KEY));
        config.setUsername(PropertiesUtil.get(USERNAME_KEY));
        config.setPassword(PropertiesUtil.get(PASSWORD_KEY));
        dataSource = new HikariDataSource(config);
    }

    public static void close() {
        dataSource = null;
    }

    private ConnectionManager() {
    }
}
