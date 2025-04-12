package kuzmich.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionManagerTest {

    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @BeforeAll
    static void setUpBeforeClass() {
        postgres.start();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgres.getJdbcUrl());
        config.setUsername(postgres.getUsername());
        config.setPassword(postgres.getPassword());
        config.setDriverClassName(postgres.getDriverClassName());
    }

    @AfterAll
    static void tearDownAfterClass() {
        postgres.stop();

        PropertiesUtil.set("db.driver", "org.postgresql.Driver");
        PropertiesUtil.set("db.url", "jdbc:postgresql://localhost:34567/test");
        PropertiesUtil.set("db.username", "test");
        PropertiesUtil.set("db.password", "test");
    }

    @AfterEach
    void tearDown() {
        ConnectionManager.close();
    }

    @Test
    void getDataSourceTest() {
        System.setProperty("test", "true");
        System.setProperty("url", postgres.getJdbcUrl());
        System.setProperty("user", postgres.getUsername());
        System.setProperty("pass", postgres.getPassword());
        System.setProperty("driver", postgres.getDriverClassName());

        HikariDataSource ds = (HikariDataSource) ConnectionManager.getDataSource();

        assertNotNull(ds);
        assertEquals(postgres.getJdbcUrl(), ds.getJdbcUrl());
        assertEquals(postgres.getDriverClassName(), ds.getDriverClassName());
        assertEquals(postgres.getUsername(), ds.getUsername());
        assertEquals(postgres.getPassword(), ds.getPassword());

        System.clearProperty("test");
        System.clearProperty("url");
        System.clearProperty("user");
        System.clearProperty("pass");
        System.clearProperty("driver");
    }

    @Test
    void initConnectionPoolTest() {
        PropertiesUtil.set("db.driver", postgres.getDriverClassName());
        PropertiesUtil.set("db.url", postgres.getJdbcUrl());
        PropertiesUtil.set("db.username", postgres.getUsername());
        PropertiesUtil.set("db.password", postgres.getPassword());

        HikariDataSource ds = (HikariDataSource) ConnectionManager.getDataSource();

        assertNotNull(ds);
        assertEquals(postgres.getJdbcUrl(), ds.getJdbcUrl());
        assertEquals(postgres.getDriverClassName(), ds.getDriverClassName());
        assertEquals(postgres.getUsername(), ds.getUsername());
        assertEquals(postgres.getPassword(), ds.getPassword());
    }
}