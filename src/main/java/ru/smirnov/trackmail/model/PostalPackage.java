package ru.smirnov.trackmail.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "package")
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PostalPackage extends AbstractBaseEntity {
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PostalPackageType type;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "address")
    private String recipientAddress;

    @Column(name = "recipient_name")
    private String recipientName;
}
