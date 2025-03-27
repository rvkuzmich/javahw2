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
                String json = mapper.writeValueAsString(authors);
                out.write(json);
            } else {
                String pathInfo = req.getPathInfo();
                String[] parts = pathInfo.split("/");
                if (isNumeric(parts[1])) {
                    long authorId = Long.parseLong(parts[1]);
                    AuthorDto author = authorService.findById(authorId);
                    String json = mapper.writeValueAsString(author);
                    out.write(json);
                } else {
                    out.write("Некорректный идентификатор автора");
                }
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
            if (!req.getParameter(NAME_PARAMETER).isBlank() && !req.getParameter(SURNAME_PARAMETER).isBlank()) {
                authorDto.setName(req.getParameter(NAME_PARAMETER));
                authorDto.setSurname(req.getParameter(SURNAME_PARAMETER));
                authorDto = authorService.save(authorDto);
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(authorDto);
                out.write(json);
            } else {
                out.write("Некорректные параметры автора");
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
                long authorId = Long.parseLong(parts[1]);
                boolean res = authorService.delete(authorId);
                if (res) {
                    out.print("Автор успешно удален");
                } else {
                    out.print("Автора с таким идентификатором не существует");
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
            if (isNumeric(parts[1])) {
                long authorId = Long.parseLong(parts[1]);
                AuthorDto authorDto = new AuthorDto();
                if (!req.getParameter(NAME_PARAMETER).isBlank() && !req.getParameter(SURNAME_PARAMETER).isBlank()) {
                    authorDto.setId(authorId);
                    authorDto.setName(req.getParameter(NAME_PARAMETER));
                    authorDto.setSurname(req.getParameter(SURNAME_PARAMETER));
                }
                boolean res = authorService.update(authorDto);
                if (res) {
                    out.print("Автор успешно обновлен");
                } else {
                    out.print("Некорректный идентификатор автора");
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
