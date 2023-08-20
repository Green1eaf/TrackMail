package ru.smirnov.trackmail.dto;

import lombok.Builder;
import lombok.Data;
import ru.smirnov.trackmail.model.PostalPackageType;

@Builder
@Data
public class PostalPackageDto {
    private Long id;
    private PostalPackageType type;
    private String postalCode;
    private String recipientAddress;
    private String recipientName;
}
