package mk.finki.ukim.wp.lab.web.controller;

import mk.finki.ukim.wp.lab.model.Book;
import mk.finki.ukim.wp.lab.service.AuthorService;
import mk.finki.ukim.wp.lab.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;
    // constructor injection (Spring ќе го наполни BookService)
    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }
    @GetMapping("/book-form")
    public String getAddBookPage(Model model) {

        model.addAttribute("book", null); // add режим
        model.addAttribute("authors", authorService.findAll());

        return "book-form";
    }

    @GetMapping("/book-form/{id}")
    public String getEditBookForm(@PathVariable Long id, Model model) {

        Book book = bookService.findById(id);
        if (book == null) {
            return "redirect:/books?error=BookNotFound";
        }

        model.addAttribute("book", book);
        model.addAttribute("authors", authorService.findAll());

        return "book-form"; // го рендерира book-form.html
    }

    @PostMapping("/add")
    public String saveBook(@RequestParam String title,
                           @RequestParam String genre,
                           @RequestParam Double averageRating,
                           @RequestParam Long authorId) {

        bookService.save(title, genre, averageRating, authorId);
        return "redirect:/books";
    }
    @PostMapping("/edit/{bookId}")
    public String editBook(@PathVariable Long bookId,
                           @RequestParam String title,
                           @RequestParam String genre,
                           @RequestParam Double averageRating,
                           @RequestParam Long authorId) {

        bookService.edit(bookId, title, genre, averageRating, authorId);
        return "redirect:/books";
    }
    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }
    @GetMapping
    public String getBooksPage(@RequestParam(required = false) String error,
                               Model model) {

        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }

        List<Book> books = bookService.listAll();
        Map<Long, Long> authorBookCounts = new HashMap<>();

        for (Book book : books) {
            Long authorId = book.getAuthor().getId();
            if (!authorBookCounts.containsKey(authorId)) {
                authorBookCounts.put(authorId, bookService.countBooksByAuthorId(authorId));
            }
        }

        model.addAttribute("books", books);
        model.addAttribute("authorBookCounts", authorBookCounts);
        return "listBooks"; // listBooks.html
    }
}
