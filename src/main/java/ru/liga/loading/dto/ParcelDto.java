package ru.liga.loading.dto;

import lombok.Data;

@Data
public class ParcelDto {
    private Long id;
    private String name;
    private char symbol;
    private char[][] box;
}
