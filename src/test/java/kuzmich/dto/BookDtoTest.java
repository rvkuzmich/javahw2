package kuzmich.dto;

import kuzmich.entity.Author;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookDtoTest {

    static BookDto dto;

    @BeforeEach
    void setUp() {
        dto = new BookDto(1L, "Title", 15, new Author(1L, "Иван", "Иванов"));
    }

    @AfterAll
    static void tearDownAfterClass() {
        dto = null;
    }

    @Test
    void getId() {
        long expectedId = 1L;
        assertEquals(expectedId, dto.getId());
    }

    @Test
    void setId() {
        long newId = 2L;
        dto.setId(newId);
        assertEquals(newId, dto.getId());
    }

    @Test
    void getTitle() {
        String expectedTitle = "Title";
        assertEquals(expectedTitle, dto.getTitle());
    }

    @Test
    void setTitle() {
        String newTitle = "New Title";
        dto.setTitle(newTitle);
        assertEquals(newTitle, dto.getTitle());
    }

    @Test
    void getPageCount() {
        int expectedPageCount = 15;
        assertEquals(expectedPageCount, dto.getPageCount());
    }

    @Test
    void setPageCount() {
        int newPageCount = 2;
        dto.setPageCount(newPageCount);
        assertEquals(newPageCount, dto.getPageCount());
    }

    @Test
    void getAuthor() {
        Author expectedAuthor = new Author(1L, "Иван", "Иванов");
        assertEquals(expectedAuthor, dto.getAuthor());
    }

    @Test
    void setAuthor() {
        Author newAuthor = new Author(2L, "Петр", "Петров");
        dto.setAuthor(newAuthor);
        assertEquals(newAuthor, dto.getAuthor());
    }

    @Test
    void testToString() {
        String expected =
                "BookDto{" +
                "id=" + dto.getId() +
                ", title='" + dto.getTitle() + '\'' +
                ", pageCount=" + dto.getPageCount() +
                ", authorName=" + dto.getAuthor().getName() +
                ", authorSurname=" + dto.getAuthor().getSurname() +
                '}';
        assertEquals(expected, dto.toString());
    }

    @Test
    void testEquals() {
        BookDto second = new BookDto(1L, "Title", 15, new Author(1L, "Иван", "Иванов"));
        assertEquals(second, dto);
        AuthorDto authorDto = new AuthorDto();
        assertNotEquals(dto, authorDto);
    }

    @Test
    void testHashCode() {
        BookDto second = new BookDto(1L, "Title", 15, new Author(1L, "Иван", "Иванов"));
        assertEquals(second.hashCode(), dto.hashCode());
    }

    @Test
    void testEmptyConstructor() {
        BookDto bookDto = new BookDto();
        assertNotNull(bookDto);
    }
}