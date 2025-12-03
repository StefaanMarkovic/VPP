package mk.finki.ukim.wp.lab.repository;

import mk.finki.ukim.wp.lab.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByAuthor_Id(Long authorId);
    @Query("SELECT b.author.id, COUNT(b) FROM Book b GROUP BY b.author.id")
    List<Object[]> countBooksByAuthor();

}
