package hu.bme.itsec.simnyi.backend.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${logging.level.root}")
    private String loggingLevel;

    @ExceptionHandler(CustomHttpException.class)
    public ResponseEntity<HttpExceptionDto> customHttpException(CustomHttpException customHttpException,
                                                                WebRequest webRequest) {
        customHttpException.printStackTrace(); //FIXME ne maradjon itt ha lehet
        var result = new HttpExceptionDto(
                StringUtils.isBlank(customHttpException.getReason()) ? customHttpException.getMessage() :
                        customHttpException.getReason(),
                customHttpException.getStatus()
        );
        if (loggingLevel.equals("debug") || loggingLevel.equals("trace")) {
            customHttpException.printStackTrace();
            result.setStackTrace(customHttpException);
        }
        return ResponseEntity
                .status(result.getStatus())
                .body(result);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpExceptionDto> internalServerError(Exception exception, WebRequest webRequest) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new HttpExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR, exception));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpExceptionDto> illeagalArgumentException(IllegalArgumentException exception,
                                                                      WebRequest webRequest) {
        exception.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new HttpExceptionDto(HttpStatus.BAD_REQUEST, exception));
    }


}
