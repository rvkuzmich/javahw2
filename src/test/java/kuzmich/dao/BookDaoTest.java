package kuzmich.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import kuzmich.entity.Author;
import kuzmich.entity.Book;
import kuzmich.utils.ConnectionManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookDaoTest {

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
        bookDao.clearTableForTest();
    }

    @AfterAll
    static void afterAll() {
        bookDao = null;
        authorDao = null;
        postgres.stop();
        ConnectionManager.close();
    }

    @Test
    void emptyConstructorTest() {
        System.setProperty("test", "true");
        System.setProperty("url", postgres.getJdbcUrl());
        System.setProperty("user", postgres.getUsername());
        System.setProperty("pass", postgres.getPassword());
        System.setProperty("driver", postgres.getDriverClassName());

        BookDao bookDao = new BookDao();
        assertNotNull(bookDao);

        System.clearProperty("test");
        System.clearProperty("url");
        System.clearProperty("user");
        System.clearProperty("pass");
        System.clearProperty("driver");
    }

    @Test
    void save() {
        Author expectedAuthor = authorDao.save(new Author("Лев", "Толстой"));
        Book expectedBook = new Book("Метель", 25, expectedAuthor);

        Book actualBook = bookDao.save(expectedBook);
        assertEquals(expectedBook, actualBook);
        assertEquals(expectedAuthor, actualBook.getAuthor());
    }

    @Test
    void update() {

        Author author = authorDao.save(new Author("Роман", "Прокофьев"));
        Book book = bookDao.save(new Book("Название", 12, author));

        book.setTitle("Стеллар");
        book.setPageCount(453);

        boolean res = bookDao.update(book);
        assertTrue(res);

        Book actualBook = bookDao.findById(book.getId()).orElse(null);
        assertEquals(book, actualBook);

    }

    @Test
    void delete() {
        Author expectedAuthor = authorDao.save(new Author("Иван", "Иванов"));
        Book expectedBook = bookDao.save(new Book("Новая", 15, expectedAuthor));

        boolean res = bookDao.delete(expectedBook.getId());
        assertTrue(res);

        res = bookDao.delete(expectedBook.getId());
        assertFalse(res);

        Optional<Book> bookOptional = bookDao.findById(expectedBook.getId());
        assertFalse(bookOptional.isPresent());
    }

    @Test
    void findById() {
        Author expectedAuthor = authorDao.save(new Author("Иван", "Иванов"));
        Book expectedBook = bookDao.save(new Book("Новая", 15, expectedAuthor));

        Optional<Book> actualBook = bookDao.findById(expectedBook.getId());
        assertTrue(actualBook.isPresent());

        assertEquals(expectedBook, actualBook.get());
    }

    @Test
    void findAll() {
        Author author1 = authorDao.save(new Author("Иван", "Иванов"));
        Author author2 = authorDao.save(new Author("Петр", "Петров"));
        bookDao.save(new Book("Первая", 15, author1));
        bookDao.save(new Book("Вторая", 20, author2));

        List<Book> books = bookDao.findAll();

        assertNotNull(books);
        assertEquals(2, books.size());
    }

}