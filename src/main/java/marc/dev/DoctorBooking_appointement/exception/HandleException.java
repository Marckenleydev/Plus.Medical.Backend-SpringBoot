package marc.dev.DoctorBooking_appointement.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import marc.dev.DoctorBooking_appointement.domain.Response;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

import static marc.dev.DoctorBooking_appointement.utils.RequestUtils.handleErrorResponse;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class HandleException extends ResponseEntityExceptionHandler implements ErrorController {
    private final HttpServletRequest request;
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest webRequest) {
        log.error(String.format("handleExceptionInternal: %s", exception.getMessage()));
        return new ResponseEntity<>(handleErrorResponse(exception.getMessage(), getRootCauseMessage(exception), request, (HttpStatus) statusCode), statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode statusCode, WebRequest webRequest) {
        log.error(String.format("handleExceptionInternal: %s", exception.getMessage()));
        var fieldErrors = exception.getBindingResult().getFieldErrors();
        var fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>(handleErrorResponse(fieldsMessage,getRootCauseMessage(exception), request, (HttpStatus) statusCode),statusCode);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Response> apiException(ApiException exception){
        log.error(String.format("ApiException(: %s", exception.getMessage()));
        return  new ResponseEntity<>(handleErrorResponse(exception.getMessage(), getRootCauseMessage(exception), request, BAD_REQUEST),BAD_REQUEST);

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response> badCredentialsException(BadCredentialsException exception){
        log.error(String.format("BadCredentialsException(: %s", exception.getMessage()));
        return  new ResponseEntity<>(handleErrorResponse(exception.getMessage(), getRootCauseMessage(exception), request, BAD_REQUEST),BAD_REQUEST);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> Exception(Exception exception){
        log.error(String.format("Exception(: %s", exception.getMessage()));
        return  new ResponseEntity<>(handleErrorResponse(exception.getMessage(), getRootCauseMessage(exception), request, INTERNAL_SERVER_ERROR),INTERNAL_SERVER_ERROR);

    }


}
