package kuzmich.entity;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {

    static Author author;

    @BeforeEach
    void setUp() {
        author = new Author(1L, "Name", "Surname", new ArrayList<>());
        author.getBookList().add(new Book(1L, "Title", 364, new Author()));
    }

    @AfterAll
    static void tearDownAfterClass() {
        author = null;
    }

    @Test
    void getId() {
        long expectedId = 1L;
        assertEquals(expectedId, author.getId());
    }

    @Test
    void setId() {
        long newId = 2L;
        author.setId(newId);
        assertEquals(newId, author.getId());
    }

    @Test
    void getName() {
        String expectedName = "Name";
        assertEquals(expectedName, author.getName());
    }

    @Test
    void setName() {
        String newName = "New Name";
        author.setName(newName);
        assertEquals(newName, author.getName());
    }

    @Test
    void getSurname() {
        String expectedSurname = "Surname";
        assertEquals(expectedSurname, author.getSurname());
    }

    @Test
    void setSurname() {
        String newSurname = "New Surname";
        author.setSurname(newSurname);
        assertEquals(newSurname, author.getSurname());
    }

    @Test
    void getBookList() {
        ArrayList<Book> expectedBookList = new ArrayList<>();
        expectedBookList.add(new Book(1L, "Title", 364, new Author()));
        assertEquals(expectedBookList.size(), author.getBookList().size());
    }

    @Test
    void setBookList() {
        List<Book> newBookList = new ArrayList<>();
        newBookList.add(new Book(2L, "New Title", 234, new Author()));
        author.setBookList(newBookList);
        assertEquals(newBookList, author.getBookList());
    }

    @Test
    void testEquals() {
        Author second = new Author(1L, "Name", "Surname", new ArrayList<>());
        assertEquals(second, author);
        Book book = new Book();
        assertNotEquals(second, book);
    }

    @Test
    void testHashCode() {
        Author second = new Author(1L, "Name", "Surname", new ArrayList<>());
        assertEquals(second.hashCode(), author.hashCode());
    }

    @Test
    void testToString() {
        String expected =
                "Author{" +
                "id=" + author.getId() +
                ", name='" + author.getName() + '\'' +
                ", surname='" + author.getSurname() + '\'' +
                ", bookList=" + author.getBookList() +
                "}";
        assertEquals(expected, author.toString());
    }

    @Test
    void testEmptyConstructor() {
        Author expected = new Author();
        assertNotNull(expected);
        assertEquals(0, expected.getId());
        assertNull(expected.getName());
        assertNull(expected.getSurname());
        assertNull(expected.getBookList());
    }

    @Test
    void testConstructor() {
        long expectedId = 1L;
        String expectedName = "Name";
        String expectedSurname = "Surname";
        Author expected1 = new Author(expectedId, expectedName, expectedSurname);
        Author expected2 = new Author(expectedName, expectedSurname);

        assertNotNull(expected1);
        assertNotNull(expected2);

        assertEquals(expectedId, expected1.getId());
        assertEquals(expectedName, expected1.getName());
        assertEquals(expectedSurname, expected1.getSurname());

        assertEquals(expectedName, expected2.getName());
        assertEquals(expectedSurname, expected2.getSurname());
    }
}