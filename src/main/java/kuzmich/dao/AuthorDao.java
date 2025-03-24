package kuzmich.dao;

import kuzmich.entity.Author;
import kuzmich.entity.Book;
import kuzmich.exception.DaoException;
import kuzmich.repositiory.AuthorRepository;
import kuzmich.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDao implements AuthorRepository {

    private static final String AUTHOR_SURNAME_COLUMN_LABEL = "surname";
    private static final AuthorDao INSTANCE = new AuthorDao();
    private static final String SAVE_SQL = """
                insert into author (name, surname)
                values (?, ?)
            """;
    private static final String UPDATE_SQL = """
            update author
            set name = ?,
                surname = ?
            where id = ?;
            """;
    private static final String DELETE_SQL = """
                delete from author
                where id = ?
            """;
    private static final String FIND_BY_ID_SQL = """
                select public.author.id, public.author.name, public.author.surname, public.book.author_id, public.book.title, public.book.page_count
                from public.author left join book on public.author.id = public.book.author_id
                where public.author.id = ?
            """;
    private static final String FIND_ALL_AUTHORS_SQL = """
                select public.author.id, public.author.name, public.author.surname
                from public.author
            """;
    private static final String FIND_BY_NAME_AND_SURNAME_SQL = """
            select id, name, surname
            from author
            where name = ? and surname = ?
            """;

    @Override
    public Author save(Author author) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, author.getName());
            statement.setString(2, author.getSurname());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                author.setId(keys.getLong("id"));
            }
            return author;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(Author author) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, author.getName());
            statement.setString(2, author.getSurname());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean delete(long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<Author> findById(long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Author author = null;
            ArrayList<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                if (author == null) {
                    author = new Author(resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getString(AUTHOR_SURNAME_COLUMN_LABEL));
                }
                books.add(new Book(resultSet.getString("title"), resultSet.getInt("page_count")));
            }
            if (author != null) {
                author.setBookList(books);
            }
            return Optional.ofNullable(author);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL_AUTHORS_SQL)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                authors.add(new Author(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString(AUTHOR_SURNAME_COLUMN_LABEL)));
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            for (Author author : authors) {
                statement.setLong(1, author.getId());
                author.setBookList(new ArrayList<>());
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    if (author.getId() == resultSet.getLong("author_id")) {
                        author.getBookList().add(new Book(resultSet.getString("title"), resultSet.getInt("page_count")));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
        return authors;
    }

    public Optional<Author> findByNameAndSurname(String name, String surname) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_NAME_AND_SURNAME_SQL)) {
            statement.setString(1, name);
            statement.setString(2, surname);
            ResultSet resultSet = statement.executeQuery();
            Author author = null;
            if (resultSet.next()) {
                author = new Author(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString(AUTHOR_SURNAME_COLUMN_LABEL));
            }
            return Optional.ofNullable(author);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public static AuthorDao getInstance() {
        return INSTANCE;
    }

    private AuthorDao() {
    }
}
