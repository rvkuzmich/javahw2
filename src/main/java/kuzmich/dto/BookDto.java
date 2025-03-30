package kuzmich.dto;

import kuzmich.entity.Author;

import java.util.Objects;

public class BookDto {
    private long id;
    private String title;
    private int pageCount;
    private Author author;

    public BookDto() {
    }

    public BookDto(long id, String title, int pageCount, Author author) {
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
    public String toString() {
        return "BookDto{" +
               "id=" + id +
               ", title='" + title + '\'' +
               ", pageCount=" + pageCount +
               ", authorName=" + author.getName() +
               ", authorSurname=" + author.getSurname() +
               "}";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BookDto)) return false;
        BookDto bookDto = (BookDto) o;
        return id == bookDto.id && pageCount == bookDto.pageCount && Objects.equals(title, bookDto.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, pageCount);
    }
}
