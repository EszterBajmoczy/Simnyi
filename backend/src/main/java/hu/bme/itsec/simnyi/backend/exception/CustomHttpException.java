package hu.bme.itsec.simnyi.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomHttpException extends ResponseStatusException {

    public CustomHttpException(HttpStatus status) {
        super(status, "");
    }

    public CustomHttpException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public CustomHttpException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public CustomHttpException(int rawStatusCode, String reason, Throwable cause) {
        super(rawStatusCode, reason, cause);
    }

}
