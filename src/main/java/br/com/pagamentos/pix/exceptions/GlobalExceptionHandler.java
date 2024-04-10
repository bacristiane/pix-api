package br.com.pagamentos.pix.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ChavePixInvalidException.class)
	public ResponseEntity<Object> handlePixNotFoundException(
			ChavePixInvalidException ex) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("message", ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PixNotFoundException.class)
	public ResponseEntity<Object> handlePixNotFoundException(
			PixNotFoundException ex) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", HttpStatus.NOT_FOUND.value());
		body.put("message", ex.getMessage());

		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class})
	public ResponseEntity<Object> handleInternal(final RuntimeException ex) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		body.put("message", "Internal server error");
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}

