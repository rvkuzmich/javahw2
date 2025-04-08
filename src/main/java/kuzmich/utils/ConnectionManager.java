package kuzmich.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class ConnectionManager {
    private static final String DRIVER_KEY = "db.driver";
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static HikariDataSource dataSource;

    static {
        initConnectionPool();
    }

    private static void initConnectionPool() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(PropertiesUtil.get(DRIVER_KEY));
        config.setJdbcUrl(PropertiesUtil.get(URL_KEY));
        config.setUsername(PropertiesUtil.get(USERNAME_KEY));
        config.setPassword(PropertiesUtil.get(PASSWORD_KEY));
        dataSource = new HikariDataSource(config);
    }

    public static DataSource getDataSource() {
            return dataSource;
    }

    private ConnectionManager() {
    }
}
