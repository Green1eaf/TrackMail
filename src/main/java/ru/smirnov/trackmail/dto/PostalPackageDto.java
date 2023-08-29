package ru.smirnov.trackmail.dto;

import lombok.*;
import ru.smirnov.trackmail.model.PostalPackageType;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostalPackageDto {
    private Long id;
    private PostalPackageType type;
    private String postalCode;
    private String recipientAddress;
    private String recipientName;
}
