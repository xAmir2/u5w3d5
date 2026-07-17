package amirka.u5w3d5.controllers;

import amirka.u5w3d5.entities.User;
import amirka.u5w3d5.exceptions.ValidationEx;
import amirka.u5w3d5.payloads.LoginDTO;
import amirka.u5w3d5.payloads.LoginResponseDTO;
import amirka.u5w3d5.payloads.UserRegistrationDTO;
import amirka.u5w3d5.payloads.UserRegistrationRespDTO;
import amirka.u5w3d5.services.AuthService;
import amirka.u5w3d5.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthUserController {

    private final UserService userService;
    private final AuthService authService;

    public AuthUserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationRespDTO register(@RequestBody @Valid UserRegistrationDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationEx(
                    validationResult.getFieldErrors()
                            .stream()
                            .map(fe -> fe.getDefaultMessage())
                            .toList()
            );
        }

        User user = userService.register(payload);
        return new UserRegistrationRespDTO(user.getId(), user.getUsername(), user.getName(), user.getSurname(),
                user.getEmail(),
                user.getRole());
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            throw new ValidationEx(
                    validationResult.getFieldErrors()
                            .stream()
                            .map(fe -> fe.getDefaultMessage())
                            .toList()
            );
        }
        String token = authService.checkCredentialsAndGenerateToken(payload);
        return new LoginResponseDTO(token);
    }
}
