package kuzmich.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesUtilTest {

    @Test
    void loadPropertiesTest() {
        assertEquals("org.postgresql.Driver", PropertiesUtil.get("db.driver"));
        assertEquals("jdbc:postgresql://localhost:34567/test", PropertiesUtil.get("db.url"));
        assertEquals("test", PropertiesUtil.get("db.username"));
        assertEquals("test", PropertiesUtil.get("db.password"));
    }
}