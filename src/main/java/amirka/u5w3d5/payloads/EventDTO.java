package amirka.u5w3d5.payloads;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record EventDTO(@NotBlank(message = "Title is required")
                       String title,

                       String description,

                       @NotNull(message = "Date is required")
                       @Future(message = "Date must be in the future")
                       LocalDate date,

                       @NotBlank(message = "Location is required")
                       String location,

                       @NotNull(message = "Available spots is required")
                       @Positive(message = "Available spots must be greater than zero")
                       int availableSpots) {
}
