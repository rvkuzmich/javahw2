package kuzmich.dao;

import kuzmich.entity.Author;
import kuzmich.entity.Book;
import kuzmich.exception.DaoException;
import kuzmich.repositiory.BookRepository;
import kuzmich.utils.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDao implements BookRepository {
    private static final BookDao INSTANCE = new BookDao();
    private static final String PREPARE_DATABASE_SQL = """
            create table if not exists author(
                id serial primary key not null,
                name varchar(50) not null,
                surname varchar(50) not null
            );
            create table if not exists book(
                id serial primary key not null,
                title varchar(50) not null,
                page_count int not null,
                author_id int not null references author(id)
            );
            truncate author restart identity cascade;
            truncate book restart identity;
            """;
    private static final String SAVE_SQL = """
            insert into book (title, page_count, author_id)
            values (?, ?, ?)
            """;
    private static final String UPDATE_SQL = """
            update book
            set title = ?,
                page_count = ?,
                author_id = ?
            where id = ?;
            """;
    private static final String DELETE_SQL = """
            delete from book
            where id = ?;
            """;
    private static final String FIND_BY_ID_SQL = """
            select book.id, book.title, book.page_count,
                   author.id as author_id, author.name, author.surname
            from book left join author on book.author_id = author.id
            where book.id = ?
            """;
    private static final String FIND_ALL_SQL = """
            select book.id, book.title, book.page_count, book.author_id as author_id,
                   author.name, author.surname
            from book left join author on book.author_id = author.id
            order by book.id
            """;

    @Override
    public Book save(Book book) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getTitle());
            statement.setInt(2, book.getPageCount());
            statement.setLong(3, book.getAuthor().getId());
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                book.setId(keys.getLong("id"));
            }
            return book;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean update(Book book) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, book.getTitle());
            statement.setInt(2, book.getPageCount());
            statement.setLong(3, book.getAuthor().getId());
            statement.setLong(4, book.getId());
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
    public Optional<Book> findById(long id) {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            Book book = null;
            if (resultSet.next()) {
                book = buildBook(resultSet);
            }
            return Optional.ofNullable(book);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Book> findAll() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(FIND_ALL_SQL)) {
            List<Book> books = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                books.add(buildBook(resultSet));
            }
            return books;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private static Book buildBook(ResultSet resultSet) throws SQLException {
        return new Book(resultSet.getLong("id"),
                resultSet.getString("title"),
                resultSet.getInt("page_count"),
                new Author(resultSet.getLong("author_id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname")));
    }

    public static BookDao getInstance() {
        prepareDatabase();
        return INSTANCE;
    }

    private BookDao() {
    }

    private static void prepareDatabase() {
        try (var connection = ConnectionManager.get();
             var statement = connection.prepareStatement(PREPARE_DATABASE_SQL)) {
            statement.execute();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
