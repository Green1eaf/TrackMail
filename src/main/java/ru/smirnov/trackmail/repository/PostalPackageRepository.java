package ru.smirnov.trackmail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smirnov.trackmail.model.PostalPackage;

@Repository
public interface PostalPackageRepository extends JpaRepository<PostalPackage, Long> {
}
