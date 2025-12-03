package mk.finki.ukim.wp.lab.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.finki.ukim.wp.lab.model.Book;
import mk.finki.ukim.wp.lab.service.BookService;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "BookListServlet" , urlPatterns = {"","/books"})
@Component
public class BookListServlet extends HttpServlet {
    private final SpringTemplateEngine springTemplateEngine;
    private final BookService bookService;

    public BookListServlet(SpringTemplateEngine springTemplateEngine, BookService bookService) {
        this.springTemplateEngine = springTemplateEngine;
        this.bookService = bookService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IWebExchange webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req, resp);

        WebContext context = new WebContext(webExchange);

        String titleSearch = req.getParameter("titleSearch");
        String minRatingParam = req.getParameter("minRating");

        Double minRating = null;
        if (minRatingParam != null && !minRatingParam.isEmpty()) {
            try {
                minRating = Double.parseDouble(minRatingParam);
            } catch (NumberFormatException ex) {
                // ако не е број, ќе ја игнорираме вредноста
                minRating = null;
            }
        }

        List<Book> books;

        if (titleSearch != null && !titleSearch.isEmpty()) {
            books = bookService.searchBooks(titleSearch, minRating);
        } else if (minRating != null) {
            books = bookService.filterByMinRating(minRating);
        } else {
            books = bookService.listAllSortedByAuthorName();
        }

        Map<Long, Long> authorBookCounts = new HashMap<>();
        for (Book book : books) {
            Long authorId = book.getAuthor().getId();
            if (!authorBookCounts.containsKey(authorId)) {
                authorBookCounts.put(authorId, bookService.countBooksByAuthorId(authorId));
            }
        }

        context.setVariable("titleSearch",titleSearch);
        context.setVariable("minRating",minRatingParam);
        context.setVariable("books", books);
        context.setVariable("authorBookCounts", authorBookCounts);

        springTemplateEngine.process("listBooks.html",context,resp.getWriter());
    }
}
