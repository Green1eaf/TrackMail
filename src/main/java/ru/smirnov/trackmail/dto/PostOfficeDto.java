package ru.smirnov.trackmail.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostOfficeDto {
    private Long id;
    private String name;
    private String address;
}
