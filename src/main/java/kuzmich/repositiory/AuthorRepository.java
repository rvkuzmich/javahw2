package kuzmich.repositiory;

import kuzmich.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {

    Author save(Author author);

    boolean delete(long id);

    boolean update(Author author);

    Optional<Author> findById(long id);

    List<Author> findAll();

}
