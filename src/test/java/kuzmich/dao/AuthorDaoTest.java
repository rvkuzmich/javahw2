package kuzmich.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kuzmich.entity.Author;
import kuzmich.entity.Book;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthorDaoTest {

    private static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");
    private static HikariDataSource ds;
    private static AuthorDao authorDao;
    private static BookDao bookDao;

    @BeforeAll
    static void beforeAll() {
        postgres.start();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgres.getJdbcUrl());
        config.setUsername(postgres.getUsername());
        config.setPassword(postgres.getPassword());
        config.setDriverClassName(postgres.getDriverClassName());
        ds = new HikariDataSource(config);
        authorDao = new AuthorDao(ds);
        bookDao = new BookDao(ds);
    }

    @BeforeEach
    void beforeEach() {
        authorDao.clearTableForTest();
    }

    @AfterAll
    static void afterAll() {
        authorDao = null;
        bookDao = null;
        postgres.stop();
    }

    @Test
    void emptyConstructorTest() {
       AuthorDao authorDao = new AuthorDao();
        assertNotNull(authorDao);
    }

    @Test
    void save() {
        Author expected = new Author(1L, "Лев", "Толстой");

        Author actual = authorDao.save(expected);

        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getSurname(), actual.getSurname());
    }

    @Test
    void updateSuccess() {
        Author expected = authorDao.save(new Author("Лев", "Толстой"));

        expected.setName("Роман");
        expected.setSurname("Прокофьев");

        boolean res = authorDao.update(expected);
        assertTrue(res);

        Optional<Author> authorOptional = authorDao.findById(expected.getId());
        Author actual = authorOptional.orElse(null);

        assertEquals(expected, actual);
    }

    @Test
    void updateFailure() {
        Author author = new Author(100L, "Роман", "Прокофьев");

        boolean res = authorDao.update(author);
        assertFalse(res);
    }

    @Test
    void deleteSuccess() {
        Author expected = authorDao.save(new Author("Лев", "Толстой"));

        boolean res = authorDao.delete(expected.getId());
        assertTrue(res);
    }

    @Test
    void deleteFailure() {
        boolean res = authorDao.delete(100L);
        assertFalse(res);
    }

    @Test
    void findById() {
        Author expected = authorDao.save(new Author("Роман", "Прокофьев"));

        Optional<Author> authorOptional = authorDao.findById(expected.getId());
        Author actual = authorOptional.orElse(null);

        assertNotNull(actual);

        assertEquals("Роман", actual.getName());
        assertEquals("Прокофьев", actual.getSurname());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void findByIdWithBooks() {
        ArrayList<Book> books = new ArrayList<>();
        Author expected = authorDao.save(new Author("Name", "Surname"));

        books.add(new Book(1L, "Title", 200, expected));
        expected.setBookList(books);

        bookDao.save(books.get(0));
        authorDao.update(expected);

        Optional<Author> authorOptional = authorDao.findById(expected.getId());
        Author actual = authorOptional.orElse(null);
        assertNotNull(actual);
        assertEquals("Name", actual.getName());
        assertEquals("Surname", actual.getSurname());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(books.get(0), actual.getBookList().get(0));
    }

    @Test
    void findAll() {
        authorDao.save(new Author("Лев", "Толстой"));
        authorDao.save(new Author("Александр", "Пушкин"));

        List<Author> authors = authorDao.findAll();

        assertNotNull(authors);
        assertEquals(2, authors.size());
    }

}