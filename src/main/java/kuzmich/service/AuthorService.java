package kuzmich.service;

import kuzmich.dto.AuthorDto;
import kuzmich.dto.BookDto;
import kuzmich.dao.AuthorDao;
import kuzmich.entity.Author;
import kuzmich.entity.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuthorService {
    private final AuthorDao authorDao;

    public AuthorService() {
        this.authorDao = AuthorDao.getInstance();
    }

    public AuthorDto save(AuthorDto authorDto) {
        Author author = mapToAuthor(authorDto);
        author = authorDao.save(author);
        return mapToAuthorDto(author);
    }

    public boolean update(AuthorDto authorDto) {
        Author author = mapToAuthor(authorDto);
        return authorDao.update(author);
    }

    public boolean delete(long id) {
        return authorDao.delete(id);
    }

    public AuthorDto findById(long id) {
        Optional<Author> author = authorDao.findById(id);
        return author.map(this::mapToAuthorDto).orElse(null);
    }

    public List<AuthorDto> findAll() {
        return authorDao.findAll().stream().map(this::mapToAuthorDto).collect(Collectors.toList());
    }

    private Author mapToAuthor(AuthorDto authorDto) {
        Author author = new Author();
        author.setId(authorDto.getId());
        author.setName(authorDto.getName());
        author.setSurname(authorDto.getSurname());
        List<BookDto> bookDtoList = authorDto.getBookDtoList();
        List<Book> bookList = new ArrayList<>();
        for (BookDto bookDto : bookDtoList) {
            Book book = new Book();
            book.setId(bookDto.getId());
            book.setTitle(bookDto.getTitle());
            book.setAuthor(author);
            bookList.add(book);
        }
        author.setBookList(bookList);
        return author;
    }

    private AuthorDto mapToAuthorDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
        authorDto.setSurname(author.getSurname());
        List<BookDto> bookDtoList = new ArrayList<>();
        List<Book> bookList = author.getBookList();
        for (Book book : bookList) {
            BookDto bookDto = new BookDto();
            bookDto.setId(book.getId());
            bookDto.setTitle(book.getTitle());
            bookDto.setAuthor(author);
            bookDtoList.add(bookDto);
        }
        authorDto.setBookDtoList(bookDtoList);
        return authorDto;
    }
}
