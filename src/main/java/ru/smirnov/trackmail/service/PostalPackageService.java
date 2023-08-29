package ru.smirnov.trackmail.service;

import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.trackmail.dto.HistoryDto;
import ru.smirnov.trackmail.dto.PostalPackageDto;

import java.util.List;

public interface PostalPackageService {
    @Transactional
    PostalPackageDto register(PostalPackageDto postalPackageDto, long officeId);

    @Transactional
    PostalPackageDto arrivedToNextPostOffice(long postalPackageId, long officeId);

    @Transactional
    PostalPackageDto departedFromPostOffice(long postalPackageId, long officeId);

    @Transactional
    PostalPackageDto receivedByAddressee(long postalPackageId);

    @Transactional(readOnly = true)
    List<HistoryDto> findAllHistoryByPostalPackageId(long id);

    @Transactional(readOnly = true)
    HistoryDto findStatusOfPostalPackageById(long id);
}
