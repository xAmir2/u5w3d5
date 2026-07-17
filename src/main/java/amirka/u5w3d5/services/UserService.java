package amirka.u5w3d5.services;

import amirka.u5w3d5.entities.User;
import amirka.u5w3d5.exceptions.BadRequestEx;
import amirka.u5w3d5.exceptions.NotFoundEx;
import amirka.u5w3d5.payloads.UserRegistrationDTO;
import amirka.u5w3d5.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(UserRegistrationDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new BadRequestEx("Username already in use");
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new BadRequestEx("Email already in use");
        }

        User newUser = new User(
                dto.username(),
                dto.name(),
                dto.surname(),
                dto.email(),
                passwordEncoder.encode(dto.password())
        );

        return userRepository.save(newUser);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEx("No user found with email: " + email));
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundEx("No user found with the id: " + id));
    }
}
