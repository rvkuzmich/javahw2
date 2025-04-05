package kuzmich.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kuzmich.exception.SqlConnectionException;

import java.sql.Connection;
import java.sql.SQLException;

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

    public static Connection get() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new SqlConnectionException(e);
        }
    }

//    public static void setConnectionForTests(String url, String username, String password) {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl(url);
//        config.setUsername(username);
//        config.setPassword(password);
//        dataSource = new HikariDataSource(config);
//    }

    private ConnectionManager() {
    }
}
