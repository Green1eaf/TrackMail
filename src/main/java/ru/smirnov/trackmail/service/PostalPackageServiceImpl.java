package ru.smirnov.trackmail.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.trackmail.dto.HistoryDto;
import ru.smirnov.trackmail.dto.PostalPackageDto;
import ru.smirnov.trackmail.exception.NotFoundException;
import ru.smirnov.trackmail.mapper.HistoryMapper;
import ru.smirnov.trackmail.model.Status;
import ru.smirnov.trackmail.repository.HistoryRepository;
import ru.smirnov.trackmail.repository.PostOfficeRepository;
import ru.smirnov.trackmail.repository.PostalPackageRepository;

import java.time.LocalDateTime;

import static ru.smirnov.trackmail.mapper.PostalPackageMapper.toDto;
import static ru.smirnov.trackmail.mapper.PostalPackageMapper.toPostalPackage;

@Service
@AllArgsConstructor
@Slf4j
public class PostalPackageServiceImpl {
    private final PostalPackageRepository postalPackageRepository;
    private final HistoryRepository historyRepository;
    private final PostOfficeRepository postOfficeRepository;

    @Transactional
    public PostalPackageDto register(PostalPackageDto postalPackageDto, long officeId) {
        var office = postOfficeRepository.findById(officeId)
                .orElseThrow(() -> new NotFoundException("PostOffice with id=" + officeId + " not found"));
        var historyDto = HistoryDto.builder()
                .status(Status.REGISTERED)
                .dateTime(LocalDateTime.now())
                .officeAddress(office.getAddress())
                .build();
        var postalPackage = toPostalPackage(postalPackageDto);
        log.info("Service: save new postalPackage to db");
        historyRepository.save(HistoryMapper.toHistory(historyDto, postalPackage, office));
        return toDto(postalPackageRepository.save(postalPackage));
    }
}
