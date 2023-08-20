package ru.smirnov.trackmail.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smirnov.trackmail.repository.PostalPackageRepository;

@Service
@AllArgsConstructor
public class PostalPackageServiceImpl {
    private final PostalPackageRepository repository;
}
