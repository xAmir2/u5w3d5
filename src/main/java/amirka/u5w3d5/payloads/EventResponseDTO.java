package amirka.u5w3d5.payloads;

import java.time.LocalDate;
import java.util.UUID;

public record EventResponseDTO(UUID id,
                               String title,
                               String description,
                               LocalDate date,
                               String location,
                               int availableSpots,
                               UUID organizerId,
                               String organizerUsername) {
}
