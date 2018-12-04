package th.in.tcas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JWTExpiredException extends RuntimeException {
    public JWTExpiredException() {
    }

    public JWTExpiredException(String message) {
        super(message);
    }

    public JWTExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
