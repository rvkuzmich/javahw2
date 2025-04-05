package kuzmich.service;

import kuzmich.dao.AuthorDao;
import kuzmich.dao.BookDao;
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

class BookServiceTest {

    AuthorDao authorDao = Mockito.mock(AuthorDao.class);
    BookDao bookDao = Mockito.mock(BookDao.class);
    BookService bookService = new BookService(bookDao, authorDao);
    AuthorDto authorDto;
    Author author;
    BookDto bookDto;
    Book book;

    @BeforeEach
    void setUp() {
        List<Book> books = new ArrayList<>();
        List<BookDto> bookDtos = new ArrayList<>();

        book = new Book(1L, "Title1", 15, new Author(1L,"Name1", "Surname1"));
        books.add(book);

        bookDto = new BookDto(1L, "Title1", 15, new Author(1L,"Name1", "Surname1"));
        bookDtos.add(bookDto);

        authorDto = new AuthorDto(1L, "Name1", "Surname1", bookDtos);
        author = new Author(1L, "Name1", "Surname1", books);
    }

    @Test
    void testConstructor() {
        BookService constructed = new BookService(bookDao, authorDao);
        assertNotNull(constructed);
    }

    @Test
    void save() {
        Mockito.when(authorDao.findById(author.getId())).thenReturn(Optional.ofNullable(author));
        Mockito.when(bookDao.save(book)).thenReturn(book);

        BookDto savedBook = bookService.save(bookDto);
        Mockito.verify(bookDao).save(book);

        assertNotNull(savedBook);
        assertEquals(bookDto.getId(), savedBook.getId());
        assertEquals(bookDto.getTitle(), savedBook.getTitle());
        assertEquals(bookDto.getPageCount(), savedBook.getPageCount());
        assertEquals(bookDto.getAuthor(), savedBook.getAuthor());
    }

    @Test
    void updateSuccess() {
        boolean success = true;
        Mockito.when(bookDao.update(book)).thenReturn(success);
        Mockito.when(bookDao.findById(book.getId())).thenReturn(Optional.ofNullable(book));
        boolean updatedBook = bookService.update(bookDto);
        Mockito.verify(bookDao).update(book);
        assertTrue(updatedBook);
    }

    @Test
    void updateFailure() {
        boolean success = false;
        Mockito.when(bookDao.update(book)).thenReturn(success);
        Mockito.when(bookDao.findById(book.getId())).thenReturn(Optional.ofNullable(book));
        boolean updatedBook = bookService.update(bookDto);
        Mockito.verify(bookDao).update(book);
        assertFalse(updatedBook);
    }

    @Test
    void deleteSuccess() {
        boolean success = true;
        Mockito.when(bookDao.delete(book.getId())).thenReturn(success);

        boolean updatedBook = bookService.delete(bookDto.getId());
        Mockito.verify(bookDao).delete(book.getId());
        assertTrue(updatedBook);
    }

    @Test
    void deleteFailure() {
        boolean success = false;
        Mockito.when(bookDao.delete(book.getId())).thenReturn(success);

        boolean updatedBook = bookService.delete(bookDto.getId());
        Mockito.verify(bookDao).delete(book.getId());
        assertFalse(updatedBook);
    }

    @Test
    void findByIdSuccess() {
        Mockito.when(bookDao.findById(book.getId())).thenReturn(Optional.ofNullable(book));

        BookDto found = bookService.findById(bookDto.getId());
        assertNotNull(found);
        Mockito.verify(bookDao).findById(book.getId());
        assertEquals(bookDto.getId(), found.getId());
        assertEquals(bookDto.getTitle(), found.getTitle());
        assertEquals(bookDto.getPageCount(), found.getPageCount());
        assertEquals(bookDto.getAuthor(), found.getAuthor());
    }

    @Test
    void findByIdNotFound() {
        Mockito.when(bookDao.findById(book.getId())).thenReturn(Optional.empty());

        BookDto found = bookService.findById(bookDto.getId());
        Mockito.verify(bookDao).findById(bookDto.getId());
        assertNull(found);
    }

    @Test
    void findAllSuccess() {
        Book book1 = new Book(1L, "Title1", 10, new Author(1L,  "Name1", "Surname1"));
        Book book2 = new Book(2L, "Title2", 15, new Author(2L,  "Name2", "Surname2"));
        Book book3 = new Book(3L, "Title3", 11, new Author(2L,  "Name2", "Surname2"));
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        Mockito.when(bookDao.findAll()).thenReturn(books);

        List<BookDto> result = bookService.findAll();
        Mockito.verify(bookDao).findAll();

        assertFalse(result.isEmpty());
        assertEquals(book1.getId(), result.get(0).getId());
        assertEquals(book2.getId(), result.get(1).getId());
        assertEquals(book3.getId(), result.get(2).getId());
    }

    @Test
    void findAllEmptyList() {
        List<Book> books = new ArrayList<>();
        Mockito.when(bookDao.findAll()).thenReturn(books);

        List<BookDto> result = bookService.findAll();
        Mockito.verify(bookDao).findAll();

        assertTrue(result.isEmpty());
    }
}