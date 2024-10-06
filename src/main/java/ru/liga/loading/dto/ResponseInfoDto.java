package ru.liga.loading.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseInfoDto {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ResponseInfoDto(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
