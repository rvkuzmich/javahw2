package kuzmich.dao;

import kuzmich.entity.Author;
import kuzmich.utils.PropertiesUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthorDaoTest {

    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");
    private static AuthorDao dao;
    private static MockedStatic<PropertiesUtil> propertiesUtilMockedStatic;


    @BeforeAll
    static void beforeAll() {
        postgres.start();
        propertiesUtilMockedStatic = Mockito.mockStatic(PropertiesUtil.class);
        Mockito.when(PropertiesUtil.get("db.driver")).thenReturn(postgres.getDriverClassName());
        Mockito.when(PropertiesUtil.get("db.url")).thenReturn(postgres.getJdbcUrl());
        Mockito.when(PropertiesUtil.get("db.username")).thenReturn(postgres.getUsername());
        Mockito.when(PropertiesUtil.get("db.password")).thenReturn(postgres.getPassword());
    }

    @BeforeEach
    void beforeEach() {
        dao = AuthorDao.getInstance();
    }

    @AfterAll
    static void afterAll() {
        dao = null;
        postgres.stop();
        propertiesUtilMockedStatic.close();
    }

    @Test
    void save() {
        Author expected = new Author(1L, "Лев", "Толстой");

        Author actual = dao.save(expected);

        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
    }

    @Test
    void updateSuccess() {
        Author expected = dao.save(new Author("Лев", "Толстой"));

        expected.setName("Роман");
        expected.setSurname("Прокофьев");

        boolean res = dao.update(expected);
        assertTrue(res);

        Optional<Author> authorOptional = dao.findById(expected.getId());
        Author actual = authorOptional.orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    void updateFailure() {
        Author author = new Author(100L, "Роман", "Прокофьев");

        boolean res = dao.update(author);
        assertFalse(res);
    }

    @Test
    void deleteSuccess() {
        Author expected = dao.save(new Author("Лев", "Толстой"));

        boolean res = dao.delete(expected.getId());
        assertTrue(res);
    }

    @Test
    void deleteFailure() {
        boolean res = dao.delete(100L);
        assertFalse(res);
    }

    @Test
    void findById() {
        Author expected = dao.save(new Author("Роман", "Прокофьев"));

        Optional<Author> authorOptional = dao.findById(expected.getId());
        Author actual = authorOptional.orElse(null);

        assertNotNull(actual);

        assertEquals("Роман", actual.getName());
        assertEquals("Прокофьев", actual.getSurname());
    }

    @Test
    void findAll() {
        dao.save(new Author("Лев", "Толстой"));
        dao.save(new Author("Александр", "Пушкин"));

        List<Author> authors = dao.findAll();

        assertNotNull(authors);
        assertEquals(2, authors.size());
    }

    @Test
    void getInstance() {
        AuthorDao authorDao = AuthorDao.getInstance();
        assertNotNull(authorDao);
    }
}