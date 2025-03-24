package kuzmich.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import kuzmich.service.AuthorService;

@WebListener
public class ContextListener implements ServletContextListener {
    private AuthorService authorService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();

        authorService = new AuthorService();
        servletContext.setAttribute("authorService", authorService);
    }

    public void contextDestroyed(ServletContextEvent sce) {
        authorService = null;
    }
}
