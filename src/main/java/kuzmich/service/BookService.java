package kuzmich.service;

import kuzmich.dao.AuthorDao;
import kuzmich.dao.BookDao;
import kuzmich.dto.AuthorDto;
import kuzmich.dto.BookDto;
import kuzmich.entity.Author;
import kuzmich.entity.Book;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookService {
    private final BookDao bookDao;
    private final AuthorDao authorDao;

    public BookService() {
        this.bookDao = BookDao.getInstance();
        this.authorDao = AuthorDao.getInstance();
    }

    public BookDto save(BookDto bookDto) {
        Optional<Author> authorOptional = authorDao.findById(bookDto.getAuthor().getId());
        Author author = null;
        if (authorOptional.isPresent()) {
            author = authorOptional.get();
        }
        Book book = bookDao.save(new Book(bookDto.getTitle(), bookDto.getPageCount(), author));
        return mapToBookDto(book);
    }

    public boolean update(BookDto bookDto) {
        Book book = mapToBook(bookDto);
        return bookDao.update(book);
    }

    public boolean delete(long id) {
        return bookDao.delete(id);
    }

    public BookDto findById(long id) {
        Optional<Book> bookOptional = bookDao.findById(id);
        return bookOptional.map(this::mapToBookDto).orElse(null);
    }

    public List<BookDto> findAll() {
        return bookDao.findAll().stream().map(this::mapToBookDto).collect(Collectors.toList());
    }

    private Book mapToBook(BookDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setPageCount(bookDto.getPageCount());
        book.setAuthor(new Author(bookDto.getAuthor().getId()));
        return book;
    }

    private BookDto mapToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setPageCount(book.getPageCount());
        bookDto.setAuthor(new Author(book.getAuthor().getId(),
                book.getAuthor().getName(), book.getAuthor().getSurname()));
        return bookDto;
    }
}
