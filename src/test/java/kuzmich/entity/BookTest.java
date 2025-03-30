package kuzmich.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    static Book book;

    @BeforeEach
    void setUp() {
        book = new Book(1L, "Title", 2, new Author(1L, "Name", "Surname"));
    }

    @Test
    void getId() {
        long expectedId = 1L;
        assertEquals(expectedId, book.getId());
    }

    @Test
    void setId() {
        long newId = 2L;
        book.setId(newId);
        assertEquals(newId, book.getId());
    }

    @Test
    void getTitle() {
        String expectedTitle = "Title";
        assertEquals(expectedTitle, book.getTitle());
    }

    @Test
    void setTitle() {
        String newTitle = "New Title";
        book.setTitle(newTitle);
        assertEquals(newTitle, book.getTitle());
    }

    @Test
    void getPageCount() {
        int expectedPageCount = 2;
        assertEquals(expectedPageCount, book.getPageCount());
    }

    @Test
    void setPageCount() {
        int newPageCount = 5;
        book.setPageCount(newPageCount);
        assertEquals(newPageCount, book.getPageCount());
    }

    @Test
    void getAuthor() {
        Author expectedAuthor = new Author(1L, "Name", "Surname");
        assertEquals(expectedAuthor, book.getAuthor());
    }

    @Test
    void setAuthor() {
        Author newAuthor = new Author(2L, "New Author Name", "New Author Surname");
        book.setAuthor(newAuthor);
        assertEquals(newAuthor, book.getAuthor());
    }

    @Test
    void testEquals() {
        Book second = new Book(1L, "Title", 2, new Author(1L, "Name", "Surname"));
        assertEquals(second, book);
        Author author = new Author();
        assertNotEquals(second, author);
    }

    @Test
    void testHashCode() {
        Book second = new Book(1L, "Title", 2, new Author(1L, "Name", "Surname"));
        assertEquals(second.hashCode(), book.hashCode());
    }

    @Test
    void testToString() {
        String expected = "Book{" +
                          "id=" + book.getId() +
                          ", title='" + book.getTitle() + '\'' +
                          ", pageCount=" + book.getPageCount() +
                          ", authorName=" + book.getAuthor().getName() +
                          ", authorSurname=" + book.getAuthor().getSurname() +
                          "}";
        assertEquals(expected, book.toString());
    }

    @Test
    void testEmptyConstructor() {
        Book expectedBook = new Book();
        assertNotNull(expectedBook);
        assertEquals(0, expectedBook.getId());
        assertNull(expectedBook.getTitle());
        assertEquals(0, expectedBook.getPageCount());
        assertNull(expectedBook.getAuthor());
    }

    @Test
    void testConstructor() {
        long expectedId = 1L;
        String expectedTitle = "Title";
        int expectedPageCount = 10;
        Author expectedAuthor = new Author(1L, "Name", "Surname");

        Book expectedBook1 = new Book(expectedTitle, expectedPageCount);
        Book expectedBook2 = new Book(expectedTitle, expectedPageCount, expectedAuthor);
        Book expectedBook3 = new Book(expectedId, expectedTitle, expectedPageCount);

        assertNotNull(expectedBook1);
        assertNotNull(expectedBook2);
        assertNotNull(expectedBook3);

        assertEquals(expectedTitle, expectedBook1.getTitle());
        assertEquals(expectedPageCount, expectedBook1.getPageCount());

        assertEquals(expectedTitle, expectedBook2.getTitle());
        assertEquals(expectedPageCount, expectedBook2.getPageCount());
        assertEquals(expectedAuthor, expectedBook2.getAuthor());

        assertEquals(expectedId, expectedBook3.getId());
        assertEquals(expectedTitle, expectedBook3.getTitle());
        assertEquals(expectedPageCount, expectedBook3.getPageCount());

    }
}