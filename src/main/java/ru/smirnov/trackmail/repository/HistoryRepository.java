package ru.smirnov.trackmail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.smirnov.trackmail.model.History;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllByPostalPackageIdOrderByDateTimeDesc(long id);

    @Query("SELECT h FROM History h WHERE h.postalPackage.id=:id ORDER BY h.dateTime DESC")
    History findFirstByPostalPackageIdOrderByDateTimeDesc(long id);
}
