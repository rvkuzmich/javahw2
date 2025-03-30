package kuzmich.dto;

import kuzmich.entity.Author;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuthorDtoTest {

    static AuthorDto dto;

    @BeforeEach
    void setUp() {
        dto = new AuthorDto(1L, "Name", "Surname", new ArrayList<>());
        dto.getBookDtoList().add(new BookDto(1L, "Title", 364, new Author()));
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
    void getName() {
        String expectedName = "Name";
        assertEquals(expectedName, dto.getName());
    }

    @Test
    void setName() {
        String newName = "New Name";
        dto.setName(newName);
        assertEquals(newName, dto.getName());
    }

    @Test
    void getSurname() {
        String expectedSurname = "Surname";
        assertEquals(expectedSurname, dto.getSurname());
    }

    @Test
    void setSurname() {
        String newSurname = "New Surname";
        dto.setSurname(newSurname);
        assertEquals(newSurname, dto.getSurname());
    }

    @Test
    void getBookDtoList() {
        List<BookDto> expectedBookDtoList = new ArrayList<>();
        expectedBookDtoList.add(new BookDto(1L, "Title", 364, new Author()));
        assertEquals(expectedBookDtoList.size(), dto.getBookDtoList().size());
    }

    @Test
    void setBookDtoList() {
        List<BookDto> newBookDtoList = new ArrayList<>();
        newBookDtoList.add(new BookDto(2L, "New Title", 234, new Author()));
        dto.setBookDtoList(newBookDtoList);
        assertEquals(newBookDtoList, dto.getBookDtoList());
    }

    @Test
    void testToString() {
        String expected =
                "AuthorDto{" +
                "id=" + dto.getId() +
                ", name='" + dto.getName() + '\'' +
                ", surname='" + dto.getSurname() + '\'' +
                ", bookDtoList=" + dto.getBookDtoList() +
                "}";
        assertEquals(expected, dto.toString());
    }

    @Test
    void testEquals() {
        AuthorDto second = new AuthorDto(1L, "Name", "Surname", new ArrayList<>());
        assertEquals(second, dto);
    }

    @Test
    void testHashCode() {
        AuthorDto authorDto = new AuthorDto(1L, "Name", "Surname", new ArrayList<>());
        assertEquals(authorDto.hashCode(), dto.hashCode());
    }

    @Test
    void testEmptyConstructor() {
        AuthorDto authorDto = new AuthorDto();
        assertNotNull(authorDto);
    }

}