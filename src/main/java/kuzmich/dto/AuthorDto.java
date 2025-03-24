package kuzmich.dto;

import java.util.List;

public class AuthorDto {
    private long id;
    private String name;
    private String surname;
    private List<BookDto> bookDtoList;

    public AuthorDto() {
    }

    public AuthorDto(long id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public AuthorDto(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public AuthorDto(long id, String name, String surname, List<BookDto> bookDtoList) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.bookDtoList = bookDtoList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<BookDto> getBookDtoList() {
        return bookDtoList;
    }

    public void setBookDtoList(List<BookDto> bookDtoList) {
        this.bookDtoList = bookDtoList;
    }

    @Override
    public String toString() {
        return "AuthorDto{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", surname='" + surname + '\'' +
               ", bookDtoList=" + bookDtoList +
               '}';
    }
}
