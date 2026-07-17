package amirka.u5w3d5.repositories;

import amirka.u5w3d5.entities.Booking;
import amirka.u5w3d5.entities.Event;
import amirka.u5w3d5.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByUser(User user);

    List<Booking> findByEvent(Event event);

    Optional<Booking> findByUserAndEvent(User user, Event event);
}
