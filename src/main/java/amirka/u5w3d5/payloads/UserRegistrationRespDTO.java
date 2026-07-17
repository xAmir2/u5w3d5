package amirka.u5w3d5.payloads;

import amirka.u5w3d5.enums.Role;

import java.util.UUID;

public record UserRegistrationRespDTO(UUID id,
                                      String username,
                                      String name,
                                      String surname,
                                      String email,
                                      Role role) {
}
