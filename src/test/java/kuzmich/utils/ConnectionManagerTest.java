package kuzmich.utils;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConnectionManagerTest {

    @Test
    void getDataSource() {
        HikariDataSource ds = (HikariDataSource) ConnectionManager.getDataSource();

        assertNotNull(ds);
        assertEquals("jdbc:postgresql://localhost:5432/library", ds.getJdbcUrl());
        assertEquals("postgres", ds.getUsername());
        assertEquals("root", ds.getPassword());
    }
}