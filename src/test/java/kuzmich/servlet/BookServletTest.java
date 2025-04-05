package kuzmich.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kuzmich.dto.AuthorDto;
import kuzmich.dto.BookDto;
import kuzmich.entity.Author;
import kuzmich.entity.Book;
import kuzmich.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookServletTest extends Mockito{

    HttpServletRequest request;
    HttpServletResponse response;
    BookService bookService;
    Author author1;
    Book book1;
    Book book2;
    BookDto bookDto1;
    BookDto bookDto2;
    AuthorDto authorDto1;
    List<Book> books1 = new ArrayList<>();
    List<BookDto> bookDtoList1 = new ArrayList<>();

    @BeforeEach
    void setUp() {
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        bookService = mock(BookService.class);
        author1 = new Author(1L, "Name1", "Surname1");

        book1 = new Book(1L, "Title1", 12, author1);
        book2 = new Book(2L, "Title2", 13, author1);
        books1.add(book1);
        books1.add(book2);
        author1.setBookList(books1);

        authorDto1 = new AuthorDto(1L, "Name1", "Surname1", bookDtoList1);

        bookDto1 = new BookDto(1L, "Title1", 12, new Author(1L));
        bookDto2 = new BookDto(2L, "Title2", 13, author1);
        bookDtoList1.add(bookDto1);
        bookDtoList1.add(bookDto2);
    }

    @Test
    void testConstructor() {
        BookServlet constructed = new BookServlet(bookService);
        assertNotNull(constructed);
    }

    @Test
    void doGetAll() throws Exception {
        when(bookService.findAll()).thenReturn(bookDtoList1);
        when(request.getRequestURI()).thenReturn("/books");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new BookServlet(bookService).doGet(request, response);
        verify(response).setContentType("application/json; charset=UTF-8");
        verify(bookService).findAll();
    }

    @Test
    void doGetAllNotFound() throws Exception {
        when(bookService.findAll()).thenReturn(new ArrayList<>());
        when(request.getRequestURI()).thenReturn("/books");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new BookServlet(bookService).doGet(request, response);
        verify(request, atLeast(1)).getRequestURI();
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(bookService).findAll();
    }

    @Test
    void doGetOne() throws Exception {
        when(bookService.findById(bookDto1.getId())).thenReturn(bookDto1);
        when(request.getRequestURI()).thenReturn("/books/1");
        when(request.getPathInfo()).thenReturn("/1");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new BookServlet(bookService).doGet(request, response);
        verify(request, atLeast(1)).getRequestURI();
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json; charset=UTF-8");
    }

    @Test
    void doGetNotFound() throws Exception {
        when(bookService.findById(bookDto1.getId())).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/books/1");
        when(request.getPathInfo()).thenReturn("/1");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new BookServlet(bookService).doGet(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(bookService).findById(bookDto1.getId());
    }


    @Test
    void doPost() throws Exception {
        when(bookService.save(new BookDto("Title1", 12, new Author(1L)))).thenReturn(bookDto1);
        when(request.getParameter("title")).thenReturn(bookDto1.getTitle());
        when(request.getParameter("page_count")).thenReturn(String.valueOf(bookDto1.getPageCount()));
        when(request.getParameter("author_id")).thenReturn(String.valueOf(bookDto1.getAuthor().getId()));
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new BookServlet(bookService).doPost(request, response);
        verify(request, atLeast(1)).getParameter("title");
        verify(request, atLeast(1)).getParameter("page_count");
        verify(request, atLeast(1)).getParameter("author_id");
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(bookService).save(new BookDto("Title1", 12, new Author(1L)));
    }

    @Test
    void doDeleteSuccess() throws Exception {
        boolean success = true;
        when(bookService.delete(bookDto1.getId())).thenReturn(success);
        when(request.getPathInfo()).thenReturn("/1");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new BookServlet(bookService).doDelete(request, response);
        verify(request, atLeast(1)).getPathInfo();
        verify(bookService).delete(bookDto1.getId());
        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void doDeleteNotFound() throws Exception {
        boolean success = false;
        when(bookService.delete(bookDto1.getId())).thenReturn(success);
        when(request.getPathInfo()).thenReturn("/1");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new BookServlet(bookService).doDelete(request, response);
        verify(request, atLeast(1)).getPathInfo();
        verify(bookService).delete(bookDto1.getId());
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doDeleteBadRequest() throws Exception {
        when(request.getPathInfo()).thenReturn("/1l");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new BookServlet(bookService).doDelete(request, response);
        verify(request, atLeast(1)).getPathInfo();
        verify(response).setContentType("application/json; charset=UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPutSuccess() throws Exception {
        boolean success = true;
        when(bookService.update(bookDto1)).thenReturn(success);
        when(request.getParameter("title")).thenReturn(bookDto1.getTitle());
        when(request.getParameter("page_count")).thenReturn(String.valueOf(bookDto1.getPageCount()));
        when(request.getParameter("author_id")).thenReturn(String.valueOf(authorDto1.getId()));
        when(request.getPathInfo()).thenReturn("/1");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new BookServlet(bookService).doPut(request, response);
        verify(request, atLeast(1)).getPathInfo();
        verify(request, atLeast(1)).getParameter("title");
        verify(request, atLeast(1)).getParameter("page_count");
        verify(request, atLeast(1)).getParameter("author_id");
        verify(bookService).update(bookDto1);
    }

    @Test
    void doPutFailure() throws Exception {
        boolean success = false;
        when(bookService.update(bookDto1)).thenReturn(success);
        when(request.getParameter("title")).thenReturn(bookDto1.getTitle());
        when(request.getParameter("page_count")).thenReturn(String.valueOf(bookDto1.getPageCount()));
        when(request.getParameter("author_id")).thenReturn(String.valueOf(authorDto1.getId()));
        when(request.getPathInfo()).thenReturn("/1");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        new BookServlet(bookService).doPut(request, response);
        writer.flush();
        verify(request, atLeast(1)).getPathInfo();
        verify(request, atLeast(1)).getParameter("title");
        verify(request, atLeast(1)).getParameter("page_count");
        verify(request, atLeast(1)).getParameter("author_id");
        verify(bookService).update(bookDto1);
    }
}