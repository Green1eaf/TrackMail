package ru.smirnov.trackmail.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.smirnov.trackmail.model.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class HistoryDto {
    private long id;
    private Status status;
    private LocalDateTime dateTime;
    private String officeAddress;
}
