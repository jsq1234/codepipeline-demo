package library.backend.api.config;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import library.backend.api.dto.ErrorDto;
import library.backend.api.dto.ValidationErrorDto;
import library.backend.api.exceptions.MissingLoginFieldsException;
import library.backend.api.exceptions.UserAlreadyExistsException;
import library.backend.api.utils.Status;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(MissingLoginFieldsException.class)
    public ResponseEntity<ErrorDto> handleMissingLoginFieldsException(
            MissingLoginFieldsException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleUserExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDto(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorDto> handleConstriantViolations(ConstraintViolationException ex) {
        var errMap = new HashMap<String, String>();
        ex.getConstraintViolations().stream()
                .forEach(err -> errMap.put(err.getPropertyPath().toString(), err.getMessage()));

        var error = new ValidationErrorDto(HttpStatus.BAD_REQUEST, errMap);
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDto> handleConstriantViolation(MethodArgumentNotValidException ex) {
        var errMap = new HashMap<String, String>();
        ex.getBindingResult().getFieldErrors().stream()
                .forEach(err -> errMap.put(err.getField(), err.getDefaultMessage()));
        var error = new ValidationErrorDto(HttpStatus.BAD_REQUEST, errMap);
        return ResponseEntity.badRequest().body(error);
    }
}
