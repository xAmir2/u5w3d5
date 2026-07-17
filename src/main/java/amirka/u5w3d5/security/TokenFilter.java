package amirka.u5w3d5.security;

import amirka.u5w3d5.entities.User;
import amirka.u5w3d5.exceptions.UnauthorizedEx;
import amirka.u5w3d5.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TokenFilter extends OncePerRequestFilter {
    private final JWTTools jwtTools;
    private final UserService userService;

    public TokenFilter(JWTTools jwTools, UserService userService) {
        this.jwtTools = jwTools;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse resp,
                                    FilterChain filter) throws ServletException, IOException {

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedEx("Insert token in authorization bearer!");

        String accessToken = authHeader.replace("Bearer ", "");

        this.jwtTools.tokenVerification(accessToken);

        UUID userId = jwtTools.extractIdFromToken(accessToken);
        User currentUser = this.userService.findById(userId);

        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser, null,
                currentUser.getAuthorities());
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        filter.doFilter(req, resp);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException {

        return new AntPathMatcher().match("/auth/**", req.getServletPath());
    }
}

