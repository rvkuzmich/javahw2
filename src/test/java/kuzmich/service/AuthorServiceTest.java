package kuzmich.service;

import kuzmich.dao.AuthorDao;
import kuzmich.dto.AuthorDto;
import kuzmich.dto.BookDto;
import kuzmich.entity.Author;
import kuzmich.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthorServiceTest {

    AuthorDao authorDao = Mockito.mock(AuthorDao.class);
    AuthorService authorService = new AuthorService(authorDao);
    AuthorDto authorDto;
    Author author;

    @BeforeEach
    void setUp() {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1, "Title", 15, new Author("Name", "Surname")));
        List<BookDto> bookDtos = new ArrayList<>();
        bookDtos.add(new BookDto(1, "Title", 15, new Author("Name", "Surname")));
        authorDto = new AuthorDto(1L, "Name", "Surname", bookDtos);
        author = new Author(1L, "Name", "Surname", books);
    }

    @Test
    void testConstructor() {
        AuthorService constructed = new AuthorService(authorDao);
        assertNotNull(constructed);
    }

    @Test
    void save() {
        Mockito.when(authorDao.save(author)).thenReturn(author);

        AuthorDto savedAuthor = authorService.save(authorDto);
        Mockito.verify(authorDao).save(author);

        assertNotNull(savedAuthor);
        assertEquals(authorDto.getId(), savedAuthor.getId());
        assertEquals(authorDto.getName(), savedAuthor.getName());
        assertEquals(authorDto.getSurname(), savedAuthor.getSurname());
    }

    @Test
    void updateSuccess() {
        boolean success = true;
        Mockito.when(authorDao.update(author)).thenReturn(success);

        boolean updatedAuthor = authorService.update(authorDto);
        Mockito.verify(authorDao).update(author);
        assertTrue(updatedAuthor);
    }

    @Test
    void updateFailure() {
        boolean success = false;
        Mockito.when(authorDao.update(author)).thenReturn(success);

        boolean updatedAuthor = authorService.update(authorDto);
        Mockito.verify(authorDao).update(author);
        assertFalse(updatedAuthor);
    }

    @Test
    void deleteSuccess() {
        boolean success = true;
        Mockito.when(authorDao.delete(author.getId())).thenReturn(success);

        boolean deletedAuthor = authorService.delete(authorDto.getId());
        Mockito.verify(authorDao).delete(author.getId());
        assertTrue(deletedAuthor);
    }

    @Test
    void deleteFailure() {
        boolean success = false;
        Mockito.when(authorDao.delete(author.getId())).thenReturn(success);

        boolean deletedAuthor = authorService.delete(authorDto.getId());
        Mockito.verify(authorDao).delete(author.getId());
        assertFalse(deletedAuthor);
    }

    @Test
    void findByIdSuccess() {
        Mockito.when(authorDao.findById(author.getId())).thenReturn(Optional.ofNullable(author));

        AuthorDto foundAuthor = authorService.findById(authorDto.getId());
        Mockito.verify(authorDao).findById(author.getId());
        assertNotNull(foundAuthor);
        assertEquals(authorDto.getId(), foundAuthor.getId());
        assertEquals(authorDto.getName(), foundAuthor.getName());
        assertEquals(authorDto.getSurname(), foundAuthor.getSurname());
    }

    @Test
    void findByIdNotFound() {
        Mockito.when(authorDao.findById(author.getId())).thenReturn(Optional.empty());

        AuthorDto foundAuthor = authorService.findById(authorDto.getId());
        Mockito.verify(authorDao).findById(author.getId());
        assertNull(foundAuthor);
    }

    @Test
    void findAllSuccess() {
        Author author1 = new Author(1L, "Name", "Surname", new ArrayList<>());
        Author author2 = new Author(2L, "Name2", "Surname2", new ArrayList<>());
        Author author3 = new Author(3L, "Name3", "Surname3", new ArrayList<>());
        List<Author> authors = new ArrayList<>();
        authors.add(author1);
        authors.add(author2);
        authors.add(author3);
        Mockito.when(authorDao.findAll()).thenReturn(authors);

        List<AuthorDto> result = authorService.findAll();
        Mockito.verify(authorDao).findAll();

        assertFalse(result.isEmpty());
        assertEquals(author1.getId(), result.get(0).getId());
        assertEquals(author2.getId(), result.get(1).getId());
        assertEquals(author3.getId(), result.get(2).getId());
    }

    @Test
    void findAllEmptyList() {
        List<Author> authors = new ArrayList<>();
        Mockito.when(authorDao.findAll()).thenReturn(authors);

        List<AuthorDto> result = authorService.findAll();
        Mockito.verify(authorDao).findAll();

        assertTrue(result.isEmpty());
    }
}