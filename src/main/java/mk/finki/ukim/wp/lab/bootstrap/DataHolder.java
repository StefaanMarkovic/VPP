package mk.finki.ukim.wp.lab.bootstrap;

import jakarta.annotation.PostConstruct;
import mk.finki.ukim.wp.lab.model.Author;
import mk.finki.ukim.wp.lab.model.Book;
import mk.finki.ukim.wp.lab.repository.AuthorRepository;
import mk.finki.ukim.wp.lab.repository.BookRepository;
import org.springframework.stereotype.Component;

@Component
public class DataHolder {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public DataHolder(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    public void init() {
        if (authorRepository.count() == 0) {
            Author a1 = new Author(null, "George", "Orwell", "UK",
                    "Author of 1984 and Animal Farm.");
            Author a2 = new Author(null, "J.R.R.", "Tolkien", "UK",
                    "Author of The Hobbit and LOTR.");
            Author a3 = new Author(null, "Fyodor", "Dostoevsky", "Russia",
                    "Author of Crime and Punishment.");

            a1 = authorRepository.save(a1);
            a2 = authorRepository.save(a2);
            a3 = authorRepository.save(a3);

            bookRepository.save(new Book(null, "1984", "Dystopian", 4.8, a1));
            bookRepository.save(new Book(null, "The Hobbit", "Fantasy", 4.7, a2));
            bookRepository.save(new Book(null, "Crime and Punishment", "Classic", 4.9, a3));
        }
    }
}
