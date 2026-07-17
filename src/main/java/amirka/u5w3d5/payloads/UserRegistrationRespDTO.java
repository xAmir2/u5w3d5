package amirka.u5w3d5.payloads;

import java.util.UUID;

public record UserRegistrationRespDTO(UUID id,
                                      String username) {
}
