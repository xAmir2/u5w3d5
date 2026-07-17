package amirka.u5w3d5.services;

import amirka.u5w3d5.entities.User;
import amirka.u5w3d5.exceptions.UnauthorizedEx;
import amirka.u5w3d5.payloads.LoginDTO;
import amirka.u5w3d5.security.JWTTools;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService userService;
    private final JWTTools jwtTools;
    private PasswordEncoder bCrypt;


    public AuthService(UserService userService, JWTTools jwtTools, PasswordEncoder bCrypt) {
        this.userService = userService;
        this.jwtTools = jwtTools;
        this.bCrypt = bCrypt;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body) {

        User found = this.userService.findByEmail(body.email());

        if (bCrypt.matches(body.password(), found.getPassword())) {

            return this.jwtTools.generateToken(found);

        } else {
            throw new UnauthorizedEx("Invalid password or email.");
        }
    }
}
