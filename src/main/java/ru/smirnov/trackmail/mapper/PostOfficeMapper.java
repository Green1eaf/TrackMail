package ru.smirnov.trackmail.mapper;

import lombok.experimental.UtilityClass;
import ru.smirnov.trackmail.dto.PostOfficeDto;
import ru.smirnov.trackmail.model.PostOffice;

@UtilityClass
public class PostOfficeMapper {
    public static PostOfficeDto toDto(PostOffice postOffice) {
        return PostOfficeDto.builder()
                .id(postOffice.getId())
                .name(postOffice.getName())
                .address(postOffice.getAddress())
                .build();
    }

    public static PostOffice toPostOffice(PostOfficeDto dto) {
        return PostOffice.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .build();
    }
}
