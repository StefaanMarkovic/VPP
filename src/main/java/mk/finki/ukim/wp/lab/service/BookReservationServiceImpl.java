package mk.finki.ukim.wp.lab.service;

import mk.finki.ukim.wp.lab.model.Book;
import mk.finki.ukim.wp.lab.model.BookReservation;
import mk.finki.ukim.wp.lab.repository.BookReservationRepository;
import org.springframework.stereotype.Service;

@Service
public class BookReservationServiceImpl implements BookReservationService {

    private final BookService bookService;
    private final BookReservationRepository bookReservationRepository;

    public BookReservationServiceImpl(BookService bookService, BookReservationRepository bookReservationRepository) {
        this.bookService = bookService;
        this.bookReservationRepository = bookReservationRepository;
    }

    @Override
    public BookReservation placeReservation(Long bookId,
                                            String readerName,
                                            String readerAddress,
                                            Long numberOfCopies) {

        Book book = bookService.findById(bookId);
        if (book == null) {
            throw new RuntimeException("Book not found with id: " + bookId);
        }

        BookReservation reservation = new BookReservation(
                book.getTitle(),
                readerName,
                readerAddress,
                numberOfCopies
        );

        return bookReservationRepository.save(reservation);
    }
}
