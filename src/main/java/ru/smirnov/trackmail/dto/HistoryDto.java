package ru.smirnov.trackmail.dto;

import lombok.Builder;
import lombok.Data;
import ru.smirnov.trackmail.model.Status;

import java.time.LocalDateTime;

@Data
@Builder
public class HistoryDto {
    private long id;
    private Status status;
    private LocalDateTime dateTime;
    private String officeAddress;
}
