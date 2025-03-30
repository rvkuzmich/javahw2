package kuzmich.dao;

import kuzmich.entity.Author;
import kuzmich.utils.ConnectionManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthorDaoTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");
    static AuthorDao dao;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        ConnectionManager.setConnectionForTests(postgres.getJdbcUrl(),postgres.getUsername(),postgres.getPassword());
        dao = AuthorDao.getInstance();
    }

    @AfterAll
    static void afterAll() {
        dao = null;
        postgres.stop();
    }

    @Test
    void save() {
        Author expected1 = dao.save(new Author( "Дмитрий", "Серебряков"));
        Author expected2 = dao.save(new Author(2L, "Лев", "Толстой"));

        Author actual1 = dao.findById(expected1.getId()).orElse(null);
        Author actual2 = dao.findById(expected2.getId()).orElse(null);

        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);

    }

    @Test
    void update() {
        dao.save(new Author("Иван", "Иванов"));

        Author expected = new Author(2L,"Роман", "Прокофьев");

        boolean res = dao.update(expected);
        assertTrue(res);

        Optional<Author> authorOptional = dao.findById(2L);
        Author actual = authorOptional.orElse(null);

        assertEquals(expected,actual);
    }

    @Test
    void delete() {
        Author expected = dao.save(new Author(1L,"Иван", "Иванов"));

        boolean res = dao.delete(expected.getId());
        assertTrue(res);

        res = dao.delete(1L);
        assertFalse(res);

        Optional<Author> authorOptional = dao.findById(expected.getId());
        assertFalse(authorOptional.isPresent());

    }

    @Test
    void findById() {
        dao.save(new Author("Александр", "Пушкин"));
        Author expected = dao.save(new Author("Роман", "Прокофьев"));

        Optional<Author> authorOptional = dao.findById(expected.getId());
        Author actual = authorOptional.orElse(null);

        assertNotNull(actual);

        assertEquals("Роман",actual.getName());
        assertEquals("Прокофьев",actual.getSurname());
    }

    @Test
    void findAll() {
        dao.save(new Author("Иван", "Иванов"));
        dao.save(new Author("Петр", "Петров"));

        List<Author> authors = dao.findAll();

        assertNotNull(authors);
        assertEquals(2,authors.size());
    }

    @Test
    void getInstance() {
        AuthorDao authorDao = AuthorDao.getInstance();
        assertNotNull(authorDao);
    }
}