package amirka.u5w3d5.controllers;

import amirka.u5w3d5.entities.Booking;
import amirka.u5w3d5.entities.User;
import amirka.u5w3d5.payloads.BookingRespDTO;
import amirka.u5w3d5.services.BookingServices;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingServices bookingServices;

    public BookingController(BookingServices bookingServices) {
        this.bookingServices = bookingServices;
    }

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingRespDTO bookEvent(@PathVariable UUID eventId, @AuthenticationPrincipal User currentUser) {
        Booking booking = bookingServices.bookEvent(currentUser, eventId);
        return bookingServices.toResponseDTO(booking);
    }

    @GetMapping("/me")
    public List<BookingRespDTO> getMyBookings(@AuthenticationPrincipal User currentUser) {
        return bookingServices.getBookingsByUser(currentUser)
                .stream()
                .map(bookingServices::toResponseDTO)
                .toList();
    }


    @DeleteMapping("/{bookingId}")
    @PreAuthorize("'ORGANIZER'")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBooking(@PathVariable UUID bookingId, @AuthenticationPrincipal User currentUser) {
        bookingServices.cancelBooking(bookingId, currentUser);
    }
}
