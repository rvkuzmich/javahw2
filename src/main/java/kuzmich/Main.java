package kuzmich;

import kuzmich.dao.AuthorDao;
import kuzmich.dto.BookDto;
import kuzmich.entity.Author;
import kuzmich.service.AuthorService;
import kuzmich.service.BookService;

public class Main {
    public static void main(String[] args) {
        AuthorDao authorDao = AuthorDao.getInstance();
        System.out.println(authorDao.delete(1L));
    }
}
