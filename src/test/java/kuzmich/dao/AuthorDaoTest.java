package kuzmich.dao;

import kuzmich.entity.Author;
import kuzmich.utils.ConnectionManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class AuthorDaoTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    ConnectionManager connectionManager;
    static AuthorDao dao;

    @BeforeAll
    static void beforeAll() {
        dao = AuthorDao.getInstance();
        postgres.start();
        ConnectionManager.setConnectionForTests(postgres.getJdbcUrl(),postgres.getUsername(),postgres.getPassword());
    }

    @AfterAll
    static void afterAll() {
        dao = null;
        postgres.stop();
    }

    @Test
    void save() {
        dao.save(new Author("John", "Doe"));
        dao.save(new Author("Jane", "Doe"));

        List<Author> authors = dao.findAll();

        assertEquals(2,authors.size());
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void getInstance() {
    }
}