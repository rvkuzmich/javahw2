package kuzmich.service;

import kuzmich.dao.AuthorDao;
import kuzmich.dao.BookDao;
import kuzmich.dto.BookDto;
import kuzmich.entity.Author;
import kuzmich.entity.Book;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookService {
    private final BookDao bookDao;
    private final AuthorDao authorDao;

    public BookService(BookDao bookDao, AuthorDao authorDao) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
    }

    public BookService() {
        this.bookDao = BookDao.getInstance();
        this.authorDao = AuthorDao.getInstance();
    }

    public BookDto save(BookDto bookDto) {
        Book savedBook = bookDao.save(mapToBook(bookDto));
        Optional<Author> authorOptional = authorDao.findById(savedBook.getAuthor().getId());
        savedBook.setAuthor(authorOptional.orElse(null));
        return mapToBookDto(savedBook);
    }

    public boolean update(BookDto bookDto) {

        Optional<Book> book = bookDao.findById(bookDto.getId());
        Optional<Author> authorOptional = authorDao.findById(bookDto.getAuthor().getId());
        Book bookToUpdate = mapToBook(bookDto);
        if (book.isEmpty() || authorOptional.isEmpty()) return false;

        return bookDao.update(bookToUpdate);
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
