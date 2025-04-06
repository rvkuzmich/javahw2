package kuzmich.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
                if (books.isEmpty()) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                String json = mapper.writeValueAsString(books);
                resp.setStatus(HttpServletResponse.SC_OK);
                out.write(json);
            } else {
                String pathInfo = req.getPathInfo();
                String[] parts = pathInfo.split("/");
                if (!isNumeric(parts[1])) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                long bookId = Long.parseLong(parts[1]);
                BookDto book = bookService.findById(bookId);
                if (book == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                String json = mapper.writeValueAsString(book);
                resp.setStatus(HttpServletResponse.SC_OK);
                out.write(json);
            }
        } catch (
                IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE_JSON);
        BookDto bookDto = new BookDto();
        try (PrintWriter out = resp.getWriter()) {
            if (req.getParameter(TITLE_PARAMETER) == null
                || req.getParameter(TITLE_PARAMETER).isEmpty()
                || req.getParameter(AUTHOR_ID_PARAMETER) == null
                || req.getParameter(AUTHOR_ID_PARAMETER).isEmpty()
                || req.getParameter(PAGE_COUNT_PARAMETER) == null
                || req.getParameter(PAGE_COUNT_PARAMETER).isEmpty()
                || !isNumeric(req.getParameter(PAGE_COUNT_PARAMETER))
                || !isNumeric(req.getParameter(AUTHOR_ID_PARAMETER))){
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            bookDto.setTitle(req.getParameter(TITLE_PARAMETER));
            bookDto.setPageCount(Integer.parseInt(req.getParameter(PAGE_COUNT_PARAMETER)));
            Author author = new Author();
            author.setId(Long.parseLong(req.getParameter(AUTHOR_ID_PARAMETER)));
            bookDto.setAuthor(author);
            bookDto = bookService.save(bookDto);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(bookDto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            out.write(json);
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
                    resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                } else {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                }
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
            if (!isNumeric(parts[1])
                || !isNumeric(req.getParameter(PAGE_COUNT_PARAMETER))
                || !isNumeric(req.getParameter(AUTHOR_ID_PARAMETER))
                || req.getParameter(TITLE_PARAMETER) == null
                || req.getParameter(TITLE_PARAMETER).isEmpty()
                || req.getParameter(PAGE_COUNT_PARAMETER) == null
                || req.getParameter(PAGE_COUNT_PARAMETER).isEmpty()
                || req.getParameter(AUTHOR_ID_PARAMETER) == null
                || req.getParameter(AUTHOR_ID_PARAMETER).isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            long bookId = Long.parseLong(parts[1]);
            BookDto bookDto = new BookDto();
            bookDto.setId(bookId);
            bookDto.setId(bookId);
            bookDto.setTitle(req.getParameter(TITLE_PARAMETER));
            bookDto.setPageCount(Integer.parseInt(req.getParameter(PAGE_COUNT_PARAMETER)));
            bookDto.setAuthor(new Author(Long.parseLong(req.getParameter(AUTHOR_ID_PARAMETER))));
            boolean res = bookService.update(bookDto);
            if (res) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
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
