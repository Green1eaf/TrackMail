package ru.smirnov.trackmail.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smirnov.trackmail.repository.PostOfficeRepository;

@Service
@AllArgsConstructor
public class PostOfficeServiceImpl {
    private final PostOfficeRepository repository;
}
