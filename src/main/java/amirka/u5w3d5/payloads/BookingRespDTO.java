package amirka.u5w3d5.payloads;

import java.time.LocalDate;
import java.util.UUID;

public record BookingRespDTO(UUID id,
                             UUID userId,
                             UUID eventId,
                             String eventTitle,
                             LocalDate bookingDate) {
}
