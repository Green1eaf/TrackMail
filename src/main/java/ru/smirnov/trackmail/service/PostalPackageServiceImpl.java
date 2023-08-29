package ru.smirnov.trackmail.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smirnov.trackmail.dto.HistoryDto;
import ru.smirnov.trackmail.dto.PostalPackageDto;
import ru.smirnov.trackmail.exception.NotFoundException;
import ru.smirnov.trackmail.mapper.HistoryMapper;
import ru.smirnov.trackmail.model.PostOffice;
import ru.smirnov.trackmail.model.PostalPackage;
import ru.smirnov.trackmail.model.Status;
import ru.smirnov.trackmail.repository.HistoryRepository;
import ru.smirnov.trackmail.repository.PostOfficeRepository;
import ru.smirnov.trackmail.repository.PostalPackageRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.smirnov.trackmail.mapper.HistoryMapper.toHistory;
import static ru.smirnov.trackmail.mapper.PostalPackageMapper.toDto;
import static ru.smirnov.trackmail.mapper.PostalPackageMapper.toPostalPackage;

@Service
@AllArgsConstructor
@Slf4j
public class PostalPackageServiceImpl implements PostalPackageService {
    private final PostalPackageRepository postalPackageRepository;
    private final HistoryRepository historyRepository;
    private final PostOfficeRepository postOfficeRepository;

    @Override
    @Transactional
    public PostalPackageDto register(PostalPackageDto postalPackageDto, long officeId) {
        //Проверяем существует ли почтовое отделение
        var office = getPostOfficeIfExists(officeId);

        //Создаем объект хранящий историю перемещения почтового отправления
        //добавляем в него время регистрации, адрес офиса и статус "зарегистрирован" ("REGISTERED")
        var historyDto = createHistoryDto(office.getAddress(), Status.REGISTERED);

        //Мапим ДТО объект в сущность для сохранения в бд
        var postalPackage = toPostalPackage(postalPackageDto);

        //Сохраняем историю движения почтового отправления в бд
        log.info("Service: save new History to db");
        historyRepository.save(toHistory(historyDto, postalPackage, office));

        //Сохраняем почтовое отправление в бд
        log.info("Service: save new postalPackage to db");
        return toDto(postalPackageRepository.save(postalPackage));
    }

    @Override
    @Transactional
    public PostalPackageDto arrivedToNextPostOffice(long postalPackageId, long officeId) {
        //Проверяем существует ли почтовое отделение
        var office = getPostOfficeIfExists(officeId);

        //Проверяем существует ли почтовое отправление
        var postalPackage = getPostalPackageIfExists(postalPackageId);

        //Создаем объект хранящий историю перемещения почтового отправления
        //добавляем в него время регистрации, адрес офиса и статус "прибыл" ("ARRIVED")
        var historyDto = createHistoryDto(office.getAddress(), Status.ARRIVED);

        //Сохраняем историю перемещения почтового отправления в бд
        log.info("Service: save new History to db when postalPackage was arrived at the intermediate post office");
        historyRepository.save(toHistory(historyDto, postalPackage, office));

        return toDto(postalPackage);
    }

    @Override
    @Transactional
    public PostalPackageDto departedFromPostOffice(long postalPackageId, long officeId) {
        //Проверяем существует ли почтовое отделение
        var office = getPostOfficeIfExists(officeId);

        //Проверяем существует ли почтовое отправление
        var postalPackage = getPostalPackageIfExists(postalPackageId);

        //Создаем объект хранящий историю перемещения почтового отправления
        //добавляем в него время регистрации, адрес и статус "отбыл" ("DEPARTED")
        var historyDto = createHistoryDto(office.getAddress(), Status.DEPARTED);

        //Сохраняем историю перемещения почтового отправления в бд
        log.info("Service: save new History to db when postalPackage was departed from intermediate post office");
        historyRepository.save(toHistory(historyDto, postalPackage, office));

        return toDto(postalPackage);
    }

    @Override
    @Transactional
    public PostalPackageDto receivedByAddressee(long postalPackageId) {
        //Проверяем существует ли почтовое отправление
        var postalPackage = getPostalPackageIfExists(postalPackageId);

        //Создаем объект хранящий историю перемещения почтового отправления
        //добавляем в него время получения адресатом, адрес и статус "получен" ("DELIVERED")
        var historyDto = createHistoryDto(postalPackage.getRecipientAddress(), Status.DELIVERED);

        log.info("Service: save new History to db when postalPackage was delivered by addressee");
        historyRepository.save(toHistory(historyDto, postalPackage, null));
        return toDto(postalPackage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoryDto> findAllHistoryByPostalPackageId(long id) {
        //Проверяем существует ли почтовое отправление
        getPostalPackageIfExists(id);

        //Возвращаем полную историю перемещения почтового отправления
        log.info("Service: find all history by postalPackageId");
        return historyRepository.findAllByPostalPackageIdOrderByDateTimeDesc(id).stream()
                .map(HistoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HistoryDto findStatusOfPostalPackageById(long id) {
        //Проверяем существует ли почтовое отправление
        getPostalPackageIfExists(id);

        log.info("Service: find latest History by postalPackageId");
        return HistoryMapper.toDto(historyRepository.findFirstByPostalPackageIdOrderByDateTimeDesc(id));
    }

    /**
     * Метод возвращает почтовое отправление, если оно существует в бд, иначе бросает исключение
     *
     * @param postalPackageId = id почтового отправления
     * @return PostalPackage
     */
    private PostalPackage getPostalPackageIfExists(long postalPackageId) {
        return postalPackageRepository.findById(postalPackageId)
                .orElseThrow(() -> new NotFoundException("PostalPackage with id=" + postalPackageId + " not found"));
    }

    /**
     * Метод создает историю перемещения почтового отправления
     *
     * @param officeAddress = адрес почтового отделения
     * @param status        = статус почтового отправления
     * @return HistoryDto
     */
    private HistoryDto createHistoryDto(String officeAddress, Status status) {
        return HistoryDto.builder()
                .status(status)
                .dateTime(LocalDateTime.now())
                .officeAddress(officeAddress)
                .build();
    }

    /**
     * Если почтовое отделение существует, то метод вернет его.
     * Если такого почтового отделения не существует, то выбросит исключение
     *
     * @param officeId = id почтового отделения
     * @return PostOffice
     */
    private PostOffice getPostOfficeIfExists(long officeId) {
        return postOfficeRepository.findById(officeId)
                .orElseThrow(() -> new NotFoundException("PostOffice with id=" + officeId + " not found"));
    }
}