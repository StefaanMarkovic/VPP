package mk.finki.ukim.wp.lab.repository;

import mk.finki.ukim.wp.lab.model.BookReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {
}
