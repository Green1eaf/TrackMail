package ru.smirnov.trackmail.mapper;

import lombok.experimental.UtilityClass;
import ru.smirnov.trackmail.dto.PostalPackageDto;
import ru.smirnov.trackmail.model.PostalPackage;

@UtilityClass
public class PostalPackageMapper {
    public static PostalPackage toPostalPackage(PostalPackageDto dto) {
        return PostalPackage.builder()
                .id(dto.getId())
                .type(dto.getType())
                .postalCode(dto.getPostalCode())
                .recipientAddress(dto.getRecipientAddress())
                .recipientName(dto.getRecipientName())
                .build();
    }

    public static PostalPackageDto toDto(PostalPackage postalPackage) {
        return PostalPackageDto.builder()
                .id(postalPackage.getId())
                .type(postalPackage.getType())
                .postalCode(postalPackage.getPostalCode())
                .recipientAddress(postalPackage.getRecipientAddress())
                .recipientName(postalPackage.getRecipientName())
                .build();
    }
}
