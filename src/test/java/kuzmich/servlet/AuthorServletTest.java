package kuzmich.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kuzmich.dto.AuthorDto;
import kuzmich.dto.BookDto;
import kuzmich.entity.Author;
import kuzmich.entity.Book;
import kuzmich.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthorServletTest extends Mockito {

    HttpServletRequest request;
    HttpServletResponse response;
    AuthorService authorService;
    Author author1;
    Author author2;
    Book book1;
    Book book2;
    Book book3;
    AuthorDto authorDto1;
    AuthorDto authorDto2;
    List<Author> authors = new ArrayList<>();
    List<AuthorDto> authorDtos = new ArrayList<>();
    List<Book> books1 = new ArrayList<>();
    List<Book> books2 = new ArrayList<>();
    List<BookDto> bookDtoList2 = new ArrayList<>();

    @BeforeEach
    void setUp() {
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        authorService = mock(AuthorService.class);
        author1 = new Author(1L, "Name1", "Surname1");
        author2 = new Author(2L, "Name2", "Surname2");

        book1 = new Book(1L, "Title1", 12, author1);
        book2 = new Book(2L, "Title2", 13, author1);
        books1.add(book1);
        books2.add(book2);
        author1.setBookList(books1);

        book3 = new Book(3L, "Title3", 14, author2);
        books2.add(book3);
        author2.setBookList(books2);

        authors.add(author1);
        authors.add(author2);

        authorDto1 = new AuthorDto(1L, "Name1", "Surname1", new ArrayList<>());
        authorDto2 = new AuthorDto(2L, "Name2", "Surname2", bookDtoList2);

        authorDto2.getBookDtoList().add(new BookDto(3L, "Title3", 14, author2));

        authorDtos.add(authorDto1);
        authorDtos.add(authorDto2);

    }

    @Test
    void testConstructor() {
        AuthorServlet constructed = new AuthorServlet(authorService);
        assertNotNull(constructed);
    }

    @Test
    void doGetAll() throws Exception {
        when(authorService.findAll()).thenReturn(authorDtos);
        when(request.getRequestURI()).thenReturn("/authors");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new AuthorServlet(authorService).doGet(request, response);
        verify(request, atLeast(1)).getRequestURI();
        verify(response, atLeast(1)).getWriter();
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json; charset=UTF-8");
        verify(authorService).findAll();
    }

    @Test
    void doGetAllNotFound() throws Exception {
        when(authorService.findAll()).thenReturn(new ArrayList<>());
        when(request.getRequestURI()).thenReturn("/authors");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new AuthorServlet(authorService).doGet(request, response);
        verify(request, atLeast(1)).getRequestURI();
        verify(response, atLeast(1)).getWriter();
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(response).setContentType("application/json; charset=UTF-8");
        verify(authorService).findAll();
    }

    @Test
    void doGetOne() throws Exception {
        when(authorService.findById(1)).thenReturn(authorDto1);
        when(request.getRequestURI()).thenReturn("/authors/1");
        when(request.getPathInfo()).thenReturn("/1");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new AuthorServlet(authorService).doGet(request, response);
        verify(authorService).findById(authorDto1.getId());
    }

    @Test
    void doGetOneNotFound() throws Exception {
        when(authorService.findById(authorDto1.getId())).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/authors/18");
        when(request.getPathInfo()).thenReturn("/18");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new AuthorServlet(authorService).doGet(request, response);
        verify(request, atLeast(1)).getRequestURI();
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(response).setContentType("application/json; charset=UTF-8");
    }

    @Test
    void doGetOneBadRequest() throws Exception {
        when(request.getRequestURI()).thenReturn("/authors/1f");
        when(request.getPathInfo()).thenReturn("/1f");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new AuthorServlet(authorService).doGet(request, response);
        verify(request, atLeast(1)).getRequestURI();
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response).setContentType("application/json; charset=UTF-8");
    }

    @Test
    void doPost() throws Exception {
        when(authorService.save(new AuthorDto("Name1", "Surname1"))).thenReturn(authorDto1);
        when(request.getParameter("name")).thenReturn(authorDto1.getName());
        when(request.getParameter("surname")).thenReturn(authorDto1.getSurname());
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new AuthorServlet(authorService).doPost(request, response);
        verify(request, atLeast(1)).getParameter("name");
        verify(request, atLeast(1)).getParameter("surname");
        verify(authorService).save(new AuthorDto("Name1", "Surname1"));
    }

    @Test
    void doPostBadRequest() throws Exception {
        when(request.getParameter("name")).thenReturn(null);
        when(request.getParameter("surname")).thenReturn(null);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new AuthorServlet(authorService).doPost(request, response);
        verify(request, atLeast(1)).getParameter("name");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response).setContentType("application/json; charset=UTF-8");
    }

    @Test
    void doDeleteSuccess() throws Exception {
        boolean success = true;
        when(authorService.delete(authorDto1.getId())).thenReturn(success);
        when(request.getPathInfo()).thenReturn("/1");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new AuthorServlet(authorService).doDelete(request, response);
        verify(request, atLeast(1)).getPathInfo();
        verify(authorService).delete(authorDto1.getId());
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void doDeleteNotFound() throws Exception {
        boolean success = false;
        when(authorService.delete(authorDto1.getId())).thenReturn(success);
        when(request.getPathInfo()).thenReturn("/1");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new AuthorServlet(authorService).doDelete(request, response);
        verify(request, atLeast(1)).getPathInfo();
        verify(authorService).delete(authorDto1.getId());
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doDeleteBadRequest() throws Exception {
        when(request.getPathInfo()).thenReturn("/1l");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new AuthorServlet(authorService).doDelete(request, response);
        verify(request, atLeast(1)).getPathInfo();
        verify(response).setContentType("application/json; charset=UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPutSuccess() throws Exception {
        boolean success = true;
        when(authorService.update(authorDto1)).thenReturn(success);
        when(authorService.findById(authorDto1.getId())).thenReturn(authorDto1);
        when(request.getParameter("name")).thenReturn(authorDto1.getName());
        when(request.getParameter("surname")).thenReturn(authorDto1.getSurname());
        when(request.getPathInfo()).thenReturn("/1");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new AuthorServlet(authorService).doPut(request, response);
        verify(request, atLeast(1)).getPathInfo();
        verify(request, atLeast(1)).getParameter("name");
        verify(request, atLeast(1)).getParameter("surname");
        verify(authorService).update(authorDto1);
    }

    @Test
    void doPutBadRequest() throws Exception {
        when(request.getPathInfo()).thenReturn("/1l");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new AuthorServlet(authorService).doPut(request, response);
        verify(request, atLeast(1)).getPathInfo();
        verify(response).setContentType("application/json; charset=UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPutNotFound() throws Exception {
        when(request.getPathInfo()).thenReturn("/100");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        when(request.getParameter("name")).thenReturn("Name");
        when(request.getParameter("surname")).thenReturn("Surname");
        new AuthorServlet(authorService).doPut(request, response);
        verify(request, atLeast(1)).getPathInfo();
        verify(response).setContentType("application/json; charset=UTF-8");
        verify(authorService).update(new AuthorDto(100, "Name", "Surname"));
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}