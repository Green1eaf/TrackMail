package ru.smirnov.trackmail.mapper;

import lombok.experimental.UtilityClass;
import ru.smirnov.trackmail.dto.HistoryDto;
import ru.smirnov.trackmail.model.History;
import ru.smirnov.trackmail.model.PostOffice;
import ru.smirnov.trackmail.model.PostalPackage;

@UtilityClass
public class HistoryMapper {
    public static History toHistory(HistoryDto dto, PostalPackage postalPackage, PostOffice postOffice) {
        return History.builder()
                .id(dto.getId())
                .status(dto.getStatus())
                .dateTime(dto.getDateTime())
                .postalPackage(postalPackage)
                .office(postOffice)
                .build();
    }

    public static HistoryDto toDto(History history) {
        return HistoryDto.builder()
                .id(history.getId())
                .status(history.getStatus())
                .dateTime(history.getDateTime())
                .officeAddress(history.getOffice().getAddress())
                .build();
    }
}
