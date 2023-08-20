package ru.smirnov.trackmail.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.smirnov.trackmail.repository.HistoryRepository;

@Service
@AllArgsConstructor
public class HistoryServiceImpl {
    private final HistoryRepository repository;
}
