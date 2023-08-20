package ru.smirnov.trackmail.model;

import java.time.LocalDateTime;

public class History {
    private long id;
    private Status status;
    private LocalDateTime dateTime;
    private PostalPackage postalPackage;
    private PostOffice office;
}
