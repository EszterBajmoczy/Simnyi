package hu.bme.itsec.simnyi.backend.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpExceptionDto {
    private final LocalDateTime localDateTime = LocalDateTime.now();
    private final int status;
    private final String message;
    private String stackTrace;

    public HttpExceptionDto(String message, HttpStatus httpStatus) {
        this.message = message;
        this.status = httpStatus.value();
    }

    public HttpExceptionDto(HttpStatus httpStatus, Exception exception) {
        this(exception.getMessage(), httpStatus);
        setStackTrace(exception);
    }

    public void setStackTrace(Exception exception) {
        this.stackTrace = ExceptionUtils.getStackTrace(exception);
    }
}
