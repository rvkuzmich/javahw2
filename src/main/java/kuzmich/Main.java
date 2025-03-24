package kuzmich;

import kuzmich.dao.AuthorDao;
import kuzmich.service.AuthorService;

public class Main {
    public static void main(String[] args) {
        AuthorService authorService = new AuthorService();
        System.out.println(authorService.findAll());
    }
}
