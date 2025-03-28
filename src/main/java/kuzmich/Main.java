package kuzmich;

import kuzmich.dao.AuthorDao;
import kuzmich.dto.BookDto;
import kuzmich.entity.Author;
import kuzmich.service.AuthorService;
import kuzmich.service.BookService;

public class Main {
    public static void main(String[] args) {
        AuthorService authorService = new AuthorService();
        BookService bookService = new BookService();
        System.out.println(authorService.findAll());
        System.out.println(bookService.update(new BookDto(6L, "Первый", 13,new Author(3))));
    }
}
