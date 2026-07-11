package ru.xromza.order.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import ru.xromza.order.dto.ErrorResponseDto;
import ru.xromza.order.dto.MapErrorResponseDto;
import ru.xromza.order.exceptions.ForbiddenException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<MapErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
                Map<String, String> fieldErrors = new HashMap<>();
                ex.getBindingResult().getAllErrors().forEach((error) -> {
                        String errorName = ((FieldError) error).getField();
                        String errorMessage = error.getDefaultMessage();
                        fieldErrors.put(errorName, errorMessage);
                });
                MapErrorResponseDto errors = MapErrorResponseDto.builder()
                                .error("ValidationMapError")
                                .errors(fieldErrors)
                                .build();

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex) {
                ex.printStackTrace();

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ErrorResponseDto.builder()
                                                .error("InternalServerError")
                                                .description("Произошло что-то ужасное: " + ex.getMessage())
                                                .build());
        }

        @ExceptionHandler(ForbiddenException.class)
        public ResponseEntity<ErrorResponseDto> handleForbidden(ForbiddenException ex) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                                ErrorResponseDto.builder()
                                                .description(ex.getMessage())
                                                .error("Forbidden")
                                                .build());
        }
}