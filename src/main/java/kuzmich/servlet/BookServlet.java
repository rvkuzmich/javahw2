package kuzmich.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kuzmich.dto.AuthorDto;
import kuzmich.dto.BookDto;
import kuzmich.entity.Author;
import kuzmich.service.BookService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class BookServlet extends HttpServlet {
    private static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
    private static final String TITLE_PARAMETER = "title";
    private static final String PAGE_COUNT_PARAMETER = "page_count";
    private static final String AUTHOR_ID_PARAMETER = "author_id";

    private final transient BookService bookService;

    public BookServlet(BookService bookService) {
        this.bookService = bookService;
    }

    public BookServlet() {
        bookService = new BookService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE_JSON);
        ObjectMapper mapper = new ObjectMapper();

        try (PrintWriter out = resp.getWriter()) {
            if (req.getRequestURI().equals("/books") || req.getRequestURI().equals("/books/")) {
                List<BookDto> books = bookService.findAll();
                String json = mapper.writeValueAsString(books);
                out.write(json);
            } else {
                String pathInfo = req.getPathInfo();
                String[] parts = pathInfo.split("/");
                if (isNumeric(parts[1])) {
                    long bookId = Long.parseLong(parts[1]);
                    BookDto book = bookService.findById(bookId);
                    String json = mapper.writeValueAsString(book);
                    out.write(json);
                } else {
                    out.write("Некорректный идентификатор книги");
                }
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE_JSON);
        BookDto bookDto = new BookDto();
        try (PrintWriter out = resp.getWriter()) {
            if (!req.getParameter(TITLE_PARAMETER).isBlank()
                && !req.getParameter(PAGE_COUNT_PARAMETER).isBlank()
                && !req.getParameter(AUTHOR_ID_PARAMETER).isBlank()) {
                bookDto.setTitle(req.getParameter(TITLE_PARAMETER));
                if (isNumeric(req.getParameter(PAGE_COUNT_PARAMETER))) {
                    bookDto.setPageCount(Integer.parseInt(req.getParameter(PAGE_COUNT_PARAMETER)));
                } else {
                    out.write("Некорректный параметр страниц книги");
                    return;
                }
                if (isNumeric(req.getParameter(AUTHOR_ID_PARAMETER))) {
                    bookDto.setAuthor(new Author(Long.parseLong(req.getParameter(AUTHOR_ID_PARAMETER))));
                }
                bookDto = bookService.save(bookDto);
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(bookDto);
                out.write(json);
            } else {
                out.write("Некорректные параметры книги");
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE_JSON);
        try (PrintWriter out = resp.getWriter()) {
            String pathInfo = req.getPathInfo();
            String[] parts = pathInfo.split("/");
            if (isNumeric(parts[1])) {
                long bookId = Long.parseLong(parts[1]);
                boolean res = bookService.delete(bookId);
                if (res) {
                    out.print("Книга успешно удалена");
                } else {
                    out.print("Книги с таким идентификатором не существует");
                }
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE_JSON);
        try (PrintWriter out = resp.getWriter()) {
            String pathInfo = req.getPathInfo();
            String[] parts = pathInfo.split("/");
            System.out.println(parts[1]);
            System.out.println(req.getParameter(PAGE_COUNT_PARAMETER));
            System.out.println(req.getParameter(TITLE_PARAMETER));
            if (isNumeric(parts[1])) {
                long bookId = Long.parseLong(parts[1]);
                BookDto bookDto = new BookDto();
                bookDto.setId(bookId);
                bookDto.setTitle(req.getParameter(TITLE_PARAMETER));
                if (isNumeric(req.getParameter(PAGE_COUNT_PARAMETER))) {
                    bookDto.setPageCount(Integer.parseInt(req.getParameter(PAGE_COUNT_PARAMETER)));
                } else {
                    out.write("Некорректный параметр страниц книги");
                    return;
                }

                boolean res = bookService.update(bookDto);
                if (res) {
                    out.print("Книга успешно обновлена");
                } else {
                    out.print("Некорректный идентификатор книги");
                }
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
