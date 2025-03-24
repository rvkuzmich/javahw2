package kuzmich.entity;

import java.util.Objects;

public class Book {
    private long id;
    private String title;
    private int pageCount;
    private Author author;

    public Book() {
    }

    public Book(String title, int pageCount) {
        this.title = title;
        this.pageCount = pageCount;
    }

    public Book(String title, int pageCount, Author author) {
        this.title = title;
        this.pageCount = pageCount;
        this.author = author;
    }

    public Book(long id, String title, int pageCount, Author author) {
        this.id = id;
        this.title = title;
        this.pageCount = pageCount;
        this.author = author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return id == book.id
               && pageCount == book.pageCount
               && Objects.equals(title, book.title)
               && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, pageCount, author);
    }

    @Override
    public String toString() {
        return "Book{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", pageCount=" + pageCount +
               ", author=" + author +
               '}';
    }
}
