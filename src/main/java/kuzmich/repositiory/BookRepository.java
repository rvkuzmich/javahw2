package kuzmich.repositiory;

import kuzmich.entity.Author;
import kuzmich.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    Book save(Book book);

    boolean delete(long id);

    boolean update(Book book);

    Optional<Book> findById(long id);

    List<Book> findAll();
}
