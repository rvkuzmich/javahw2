package kuzmich.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kuzmich.dto.AuthorDto;
import kuzmich.service.AuthorService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/authors/*")
public class AuthorServlet extends HttpServlet {

    private AuthorService authorService;

    @Override
    public void init() throws ServletException {
        final Object obj = getServletContext().getAttribute("authorService");
        if (!(obj instanceof AuthorService)) {
            throw new IllegalStateException("Service does not initialize!");
        } else {
            this.authorService = (AuthorService) obj;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String[] parts = pathInfo.split("/");
        Long authorId = Long.parseLong(parts[1]);
        resp.setContentType("application/json; charset=UTF-8");
        AuthorService authorService = new AuthorService();
        AuthorDto author = authorService.findById(authorId);
        ObjectMapper mapper = new ObjectMapper();
        try (PrintWriter out = resp.getWriter()) {
            String json = mapper.writeValueAsString(author);
            out.write(json);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
