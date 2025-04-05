package kuzmich.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kuzmich.dto.AuthorDto;
import kuzmich.service.AuthorService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class AuthorServlet extends HttpServlet {

    private static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
    private static final String NAME_PARAMETER = "name";
    private static final String SURNAME_PARAMETER = "surname";
    private static final String BAD_REQUEST_RESPONSE = "Bad Request";
    private static final String NOT_FOUND_RESPONSE = "Not Found";

    private final transient AuthorService authorService;

    public AuthorServlet(AuthorService authorService) {
        this.authorService = authorService;
    }

    public AuthorServlet() {
        authorService = new AuthorService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE_JSON);
        ObjectMapper mapper = new ObjectMapper();

        try (PrintWriter out = resp.getWriter()) {
            if (req.getRequestURI().equals("/authors") || req.getRequestURI().equals("/authors/")) {
                List<AuthorDto> authors = authorService.findAll();
                if (authors.isEmpty()) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write(NOT_FOUND_RESPONSE);
                    return;
                }
                resp.setStatus(HttpServletResponse.SC_OK);
                String json = mapper.writeValueAsString(authors);
                out.write(json);
            } else {
                String pathInfo = req.getPathInfo();
                String[] parts = pathInfo.split("/");
                if (isNotNumeric(parts[1])) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.write(BAD_REQUEST_RESPONSE);
                    return;
                }
                long authorId = Long.parseLong(parts[1]);
                AuthorDto author = authorService.findById(authorId);
                if (author == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write(NOT_FOUND_RESPONSE);
                    return;
                }
                String json = mapper.writeValueAsString(author);
                resp.setStatus(HttpServletResponse.SC_OK);
                out.write(json);
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE_JSON);
        AuthorDto authorDto = new AuthorDto();
        try (PrintWriter out = resp.getWriter()) {
            if (req.getParameter(NAME_PARAMETER) == null || req.getParameter(NAME_PARAMETER).isEmpty()
                || req.getParameter(SURNAME_PARAMETER) == null || req.getParameter(SURNAME_PARAMETER).isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write(BAD_REQUEST_RESPONSE);
                return;
            }
            authorDto.setName(req.getParameter(NAME_PARAMETER));
            authorDto.setSurname(req.getParameter(SURNAME_PARAMETER));
            authorDto = authorService.save(authorDto);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(authorDto);
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
            if (isNotNumeric(parts[1])) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write(BAD_REQUEST_RESPONSE);
                return;
            }
            long authorId = Long.parseLong(parts[1]);
            boolean res = authorService.delete(authorId);
            if (res) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
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
            if (isNotNumeric(parts[1])
                || req.getParameter(NAME_PARAMETER) == null
                || req.getParameter(NAME_PARAMETER).isEmpty()
                || req.getParameter(SURNAME_PARAMETER) == null
                || req.getParameter(SURNAME_PARAMETER).isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write(BAD_REQUEST_RESPONSE);
                return;
            }
            long authorId = Long.parseLong(parts[1]);
            AuthorDto authorDto = new AuthorDto();
            authorDto.setId(authorId);
            authorDto.setName(req.getParameter(NAME_PARAMETER));
            authorDto.setSurname(req.getParameter(SURNAME_PARAMETER));

            boolean res = authorService.update(authorDto);

            if (res) {
                resp.setStatus(HttpServletResponse.SC_OK);
                out.write("Author updated");
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private static boolean isNotNumeric(String str) {
        try {
            Long.parseLong(str);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
