package mk.finki.ukim.wp.lab.web.controller;

import mk.finki.ukim.wp.lab.model.Book;
import mk.finki.ukim.wp.lab.service.AuthorService;
import mk.finki.ukim.wp.lab.service.BookService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    // constructor injection
    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }


    @GetMapping("/book-form")
    public String getAddBookPage(Model model) {

        // add режим – немаме постоечка книга
        model.addAttribute("book", null);
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
                               @RequestParam(required = false) String titleSearch,
                               @RequestParam(required = false) Double minRating,
                               Model model) {

        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }


        List<Book> books;

        if (titleSearch != null && !titleSearch.isEmpty()) {
            books = bookService.searchBooks(titleSearch, minRating);
        } else if (minRating != null) {
            books = bookService.filterByMinRating(minRating);
        } else {
            books = bookService.listAllSortedByAuthorName();
        }

//        Map<Long, Long> authorBookCounts = new HashMap<>();
//        for (Book book : books) {
//            Long authorId = book.getAuthor().getId();
//            if (!authorBookCounts.containsKey(authorId)) {
//                authorBookCounts.put(authorId, bookService.countBooksByAuthorId(authorId));
//            }
//        }

        model.addAttribute("titleSearch", titleSearch);
        model.addAttribute("minRating", minRating);
        model.addAttribute("books", books);


        model.addAttribute("titleSearch", titleSearch);
        model.addAttribute("minRating", minRating);


        List<Book> all_books = bookService.listAll();
        model.addAttribute("books", books);


        Map<mk.finki.ukim.wp.lab.model.Author, Long> authorBookCounts =
                all_books.stream()
                        .collect(Collectors.groupingBy(
                                Book::getAuthor,
                                Collectors.counting()
                        ));


        model.addAttribute("authorBookCounts", authorBookCounts);


        return "listBooks";
    }


}