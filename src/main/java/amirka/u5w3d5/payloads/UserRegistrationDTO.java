package amirka.u5w3d5.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationDTO(@NotBlank(message = "Username is required")
                                  String username,

                                  @NotBlank(message = "Name is required")
                                  String name,

                                  @NotBlank(message = "Surname is required")
                                  String surname,

                                  @NotBlank(message = "Email is required")
                                  @Email(message = "Email format is invalid")
                                  String email,

                                  @NotBlank(message = "Password is required")
                                  @Size(min = 8, message = "Password must be at least 8 characters")
                                  String password
) {
}
