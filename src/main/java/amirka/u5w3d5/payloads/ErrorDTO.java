package amirka.u5w3d5.payloads;

import java.time.LocalDateTime;

public record ErrorDTO(String message,
                       LocalDateTime timestamp) {
}
