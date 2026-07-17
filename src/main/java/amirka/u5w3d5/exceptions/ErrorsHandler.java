package amirka.u5w3d5.exceptions;

import amirka.u5w3d5.payloads.ErrorDTO;
import amirka.u5w3d5.payloads.ErrorListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorsHandler {
    @ExceptionHandler(NotFoundEx.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFound(NotFoundEx ex) {

        return new ErrorDTO(
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(BadRequestEx.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequest(BadRequestEx ex) {

        return new ErrorDTO(
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ValidationEx.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorListDTO handleValidation(ValidationEx ex) {
        return new ErrorListDTO(ex.getMessage(), LocalDateTime.now(), ex.getErrorMessages());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleGenericEx(Exception ex) {

        ex.printStackTrace();

        return new ErrorDTO(
                "Internal server error. We are currently working on it.",
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(UnauthorizedEx.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    public ErrorDTO handleUnauthorized(UnauthorizedEx ex) {
        return new ErrorDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public ErrorDTO handleForbidden(AuthorizationDeniedException ex) {
        return new ErrorDTO("You don't permissions to access!", LocalDateTime.now());
    }
}
