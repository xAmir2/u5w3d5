package amirka.u5w3d5.services;

import amirka.u5w3d5.entities.Booking;
import amirka.u5w3d5.entities.Event;
import amirka.u5w3d5.entities.User;
import amirka.u5w3d5.exceptions.BadRequestEx;
import amirka.u5w3d5.exceptions.ForbiddenEx;
import amirka.u5w3d5.exceptions.NotFoundEx;
import amirka.u5w3d5.payloads.BookingRespDTO;
import amirka.u5w3d5.repositories.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class BookingServices {

    private final BookingRepository bookingRepository;
    private final EventService eventService;

    public BookingServices(BookingRepository bookingRepository, EventService eventService) {
        this.bookingRepository = bookingRepository;
        this.eventService = eventService;
    }

    @Transactional
    public Booking bookEvent(User currentUser, UUID eventId) {
        Event event = eventService.getEventById(eventId);

        if (event.getOrganizer()
                .getId()
                .equals(currentUser.getId())) {
            throw new BadRequestEx("You cannot book your own event");
        }

        if (event.getAvailableSpots() <= 0) {
            throw new BadRequestEx("No more spots available for this event");
        }

        bookingRepository.findByUserAndEvent(currentUser, event)
                .ifPresent(b -> {
                    throw new BadRequestEx("You have already booked this event");
                });

        event.setAvailableSpots(event.getAvailableSpots() - 1);
        eventService.save(event);

        Booking booking = new Booking(currentUser, event, LocalDate.now());
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }

    @Transactional
    public void cancelBooking(UUID bookingId, User currentUser) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundEx("Booking not found with id: " + bookingId));

        if (!booking.getUser()
                .getId()
                .equals(currentUser.getId())) {
            throw new ForbiddenEx("You can only cancel your own bookings");
        }

        Event event = booking.getEvent();
        event.setAvailableSpots(event.getAvailableSpots() + 1);
        eventService.save(event);

        bookingRepository.delete(booking);
    }

    public BookingRespDTO toResponseDTO(Booking booking) {
        return new BookingRespDTO(
                booking.getId(),
                booking.getUser()
                        .getId(),
                booking.getEvent()
                        .getId(),
                booking.getEvent()
                        .getTitle(),
                booking.getBookingDate()
        );
    }
}
