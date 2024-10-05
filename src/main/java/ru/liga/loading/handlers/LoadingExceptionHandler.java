package ru.liga.loading.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.liga.loading.dto.ResponseInfoDto;
import ru.liga.loading.exceptions.ParcelNotFound;

@RestControllerAdvice
public class LoadingExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ResponseInfoDto> handleUnsupportedOperationException(UnsupportedOperationException e) {
        return new ResponseEntity<>(
                new ResponseInfoDto(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<ResponseInfoDto> handleParcelNotFound(ParcelNotFound e) {
        return new ResponseEntity<>(
                new ResponseInfoDto(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}
