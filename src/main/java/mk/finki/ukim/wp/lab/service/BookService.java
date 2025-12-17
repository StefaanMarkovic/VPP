package mk.finki.ukim.wp.lab.service;

import mk.finki.ukim.wp.lab.model.Book;

import java.util.List;
import java.util.SplittableRandom;

public interface BookService {
    List<Book> listAll();
    List<Book> listAllSortedByAuthorName();
    List<Book> filterByMinRating(Double minRating);
    List<Book> searchBooks(String text, Double rating);
    Book save(String title, String genre, Double averageRating, Long authorId);
    Book edit(Long id, String title, String genre, Double averageRating, Long authorId);
    void deleteById(Long id);
    Book findById(Long id);
    Long countBooksByAuthorId(Long authorId);
    List<Book> findAllByOrderByAuthor();
}
