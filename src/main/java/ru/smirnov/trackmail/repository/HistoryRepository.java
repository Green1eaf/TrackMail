package ru.smirnov.trackmail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smirnov.trackmail.model.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
}
