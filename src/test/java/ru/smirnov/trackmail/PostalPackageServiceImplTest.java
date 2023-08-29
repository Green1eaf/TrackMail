package ru.smirnov.trackmail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.smirnov.trackmail.dto.PostalPackageDto;
import ru.smirnov.trackmail.exception.NotFoundException;
import ru.smirnov.trackmail.mapper.HistoryMapper;
import ru.smirnov.trackmail.mapper.PostalPackageMapper;
import ru.smirnov.trackmail.model.*;
import ru.smirnov.trackmail.repository.HistoryRepository;
import ru.smirnov.trackmail.repository.PostOfficeRepository;
import ru.smirnov.trackmail.repository.PostalPackageRepository;
import ru.smirnov.trackmail.service.PostalPackageServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostalPackageServiceImplTest {
    @Mock
    private PostalPackageRepository postalPackageRepository;
    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private PostOfficeRepository postOfficeRepository;

    @InjectMocks
    private PostalPackageServiceImpl service;

    private PostalPackageDto postalPackageDto;
    private PostOffice postOffice;
    private History history;

    @BeforeEach
    public void init() {
        postalPackageDto = PostalPackageDto.builder()
                .id(1L)
                .postalCode("112211")
                .type(PostalPackageType.POSTCARD)
                .recipientAddress("address")
                .recipientName("Gustav")
                .build();

        postOffice = PostOffice.builder()
                .id(1L)
                .name("name")
                .address("address")
                .build();

        history = History.builder()
                .id(1L)
                .postalPackage(PostalPackageMapper.toPostalPackage(postalPackageDto))
                .office(postOffice)
                .status(Status.ARRIVED)
                .build();
    }

    @Test
    public void registerThrowsNotFoundExceptionByPostOffice() {
        when(postOfficeRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        long officeId = 1L;
        var exception = assertThrows(NotFoundException.class,
                () -> service.register(postalPackageDto, officeId));
        assertEquals("PostOffice with id=" + officeId + " not found", exception.getMessage());

        verify(postOfficeRepository, times(1)).findById(anyLong());
    }

    @Test
    public void register() {
        when(postOfficeRepository.findById(anyLong()))
                .thenReturn(Optional.of(postOffice));
        when(postalPackageRepository.save(any(PostalPackage.class)))
                .thenReturn(PostalPackageMapper.toPostalPackage(postalPackageDto));
        long officeId = 1L;

        assertEquals(postalPackageDto, service.register(postalPackageDto, officeId));

        verify(historyRepository, times(1)).save(any(History.class));
        verify(postalPackageRepository, times(1)).save(any(PostalPackage.class));
    }

    @Test
    public void arrivedToNextPostOfficeThrowsNotFoundExceptionByPostalPackage() {
        when(postOfficeRepository.findById(anyLong()))
                .thenReturn(Optional.of(postOffice));
        when(postalPackageRepository.findById(anyLong()))
                .thenReturn(Optional.empty());
        long officeId = 1L;
        long postalPackageId = 1L;

        var exception = assertThrows(NotFoundException.class,
                () -> service.arrivedToNextPostOffice(postalPackageId, officeId));
        assertEquals("PostalPackage with id=" + postalPackageId + " not found", exception.getMessage());
    }

    @Test
    public void arrivedToNextPostOffice() {
        when(postOfficeRepository.findById(anyLong()))
                .thenReturn(Optional.of(postOffice));
        when(postalPackageRepository.findById(anyLong()))
                .thenReturn(Optional.of(PostalPackageMapper.toPostalPackage(postalPackageDto)));
        long officeId = 1L;
        long postalPackageId = 1L;

        assertEquals(postalPackageDto, service.arrivedToNextPostOffice(postalPackageId, officeId));

        verify(historyRepository, times(1)).save(any(History.class));
    }

    @Test
    public void departedFromPostOffice() {
        when(postOfficeRepository.findById(anyLong()))
                .thenReturn(Optional.of(postOffice));
        when(postalPackageRepository.findById(anyLong()))
                .thenReturn(Optional.of(PostalPackageMapper.toPostalPackage(postalPackageDto)));
        long officeId = 1L;
        long postalPackageId = 1L;

        assertEquals(postalPackageDto, service.departedFromPostOffice(postalPackageId, officeId));

        verify(historyRepository, times(1)).save(any(History.class));
    }

    @Test
    public void receivedByAddressee() {
        when(postalPackageRepository.findById(anyLong()))
                .thenReturn(Optional.of(PostalPackageMapper.toPostalPackage(postalPackageDto)));
        long postalPackageId = 1L;

        assertEquals(postalPackageDto, service.receivedByAddressee(postalPackageId));

        verify(historyRepository, times(1)).save(any(History.class));
    }

    @Test
    public void findAllHistoryByPostalPackageId() {
        when(historyRepository.findAllByPostalPackageIdOrderByDateTimeDesc(anyLong()))
                .thenReturn(List.of(history));
        when(postalPackageRepository.findById(anyLong()))
                .thenReturn(Optional.of(PostalPackageMapper.toPostalPackage(postalPackageDto)));
        long postalPackageId = 1L;
        int from = 0;
        int size = 1;

        assertArrayEquals(List.of(HistoryMapper.toDto(history)).toArray(),
                service.findAllHistoryByPostalPackageId(postalPackageId, from, size).toArray());

        verify(postalPackageRepository, times(1)).findById(anyLong());
        verify(historyRepository, times(1)).findAllByPostalPackageIdOrderByDateTimeDesc(anyLong());
    }

    @Test
    public void findStatusOfPostalPackageById() {
        when(historyRepository.findFirstByPostalPackageIdOrderByDateTimeDesc(anyLong()))
                .thenReturn(history);
        when(postalPackageRepository.findById(anyLong()))
                .thenReturn(Optional.of(PostalPackageMapper.toPostalPackage(postalPackageDto)));
        long postalPackageId = 1L;

        assertEquals(HistoryMapper.toDto(history), service.findStatusOfPostalPackageById(postalPackageId));
        verify(postalPackageRepository, times(1)).findById(anyLong());
        verify(historyRepository, times(1)).findFirstByPostalPackageIdOrderByDateTimeDesc(anyLong());
    }
}
