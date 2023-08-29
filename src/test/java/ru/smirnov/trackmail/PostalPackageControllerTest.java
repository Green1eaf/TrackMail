package ru.smirnov.trackmail;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.smirnov.trackmail.controller.PostalPackageController;
import ru.smirnov.trackmail.dto.HistoryDto;
import ru.smirnov.trackmail.dto.PostalPackageDto;
import ru.smirnov.trackmail.model.PostalPackageType;
import ru.smirnov.trackmail.model.Status;
import ru.smirnov.trackmail.service.PostalPackageServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PostalPackageControllerTest {

    private static final String OFFICE_ID = "X-Sharer-Office-Id";
    private static final String POSTAL_PACKAGE_ID = "X-Sharer-Postal-Package-Id";
    @Mock
    private PostalPackageServiceImpl service;

    @InjectMocks
    private PostalPackageController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    private MockMvc mvc;

    private PostalPackageDto postalPackageDto;

    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        postalPackageDto = PostalPackageDto.builder()
                .id(1L)
                .postalCode("118811")
                .recipientName("Alex")
                .type(PostalPackageType.POSTCARD)
                .recipientAddress("test address")
                .build();
    }

    @Test
    public void register() throws Exception {
        when(service.register(any(PostalPackageDto.class), anyLong()))
                .thenReturn(postalPackageDto);

        mvc.perform(post("/packages/register")
                        .header(OFFICE_ID, 1)
                        .content(mapper.writeValueAsString(postalPackageDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(postalPackageDto.getId()), Long.class))
                .andExpect(jsonPath("$.recipientName", is(postalPackageDto.getRecipientName())))
                .andExpect(jsonPath("$.postalCode", is(postalPackageDto.getPostalCode())))
                .andExpect(jsonPath("$.type", is(postalPackageDto.getType().name())))
                .andExpect(jsonPath("$.recipientAddress", is(postalPackageDto.getRecipientAddress())));

        verify(service, times(1)).register(any(PostalPackageDto.class), anyLong());
    }

    @Test
    public void arrivedToNextPostOffice() throws Exception {
        when(service.arrivedToNextPostOffice(anyLong(), anyLong()))
                .thenReturn(postalPackageDto);

        mvc.perform(post("/packages/arrived")
                        .header(OFFICE_ID, 1)
                        .header(POSTAL_PACKAGE_ID, 1)
                        .content(mapper.writeValueAsString(postalPackageDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(postalPackageDto.getId()), Long.class))
                .andExpect(jsonPath("$.recipientName", is(postalPackageDto.getRecipientName())))
                .andExpect(jsonPath("$.postalCode", is(postalPackageDto.getPostalCode())))
                .andExpect(jsonPath("$.type", is(postalPackageDto.getType().name())))
                .andExpect(jsonPath("$.recipientAddress", is(postalPackageDto.getRecipientAddress())));

        verify(service, times(1)).arrivedToNextPostOffice(anyLong(), anyLong());
    }

    @Test
    public void departedFromPostOffice() throws Exception {
        when(service.departedFromPostOffice(anyLong(), anyLong()))
                .thenReturn(postalPackageDto);

        mvc.perform(post("/packages/departed")
                        .header(OFFICE_ID, 1)
                        .header(POSTAL_PACKAGE_ID, 1)
                        .content(mapper.writeValueAsString(postalPackageDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(postalPackageDto.getId()), Long.class))
                .andExpect(jsonPath("$.recipientName", is(postalPackageDto.getRecipientName())))
                .andExpect(jsonPath("$.postalCode", is(postalPackageDto.getPostalCode())))
                .andExpect(jsonPath("$.type", is(postalPackageDto.getType().name())))
                .andExpect(jsonPath("$.recipientAddress", is(postalPackageDto.getRecipientAddress())));

        verify(service, times(1)).departedFromPostOffice(anyLong(), anyLong());
    }

    @Test
    public void receivedByAddressee() throws Exception {
        when(service.receivedByAddressee(anyLong()))
                .thenReturn(postalPackageDto);

        mvc.perform(post("/packages/received")
                        .header(POSTAL_PACKAGE_ID, 1)
                        .content(mapper.writeValueAsString(postalPackageDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(postalPackageDto.getId()), Long.class))
                .andExpect(jsonPath("$.recipientName", is(postalPackageDto.getRecipientName())))
                .andExpect(jsonPath("$.postalCode", is(postalPackageDto.getPostalCode())))
                .andExpect(jsonPath("$.type", is(postalPackageDto.getType().name())))
                .andExpect(jsonPath("$.recipientAddress", is(postalPackageDto.getRecipientAddress())));

        verify(service, times(1)).receivedByAddressee(anyLong());
    }

    @Test
    public void findAllHistoryByPostalPackageId() throws Exception {
        var historyDto = HistoryDto.builder()
                .id(1L)
                .officeAddress("address")
                .status(Status.ARRIVED)
                .build();
        when(service.findAllHistoryByPostalPackageId(anyLong()))
                .thenReturn(List.of(historyDto));

        mvc.perform(get("/packages/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(historyDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].officeAddress", is(historyDto.getOfficeAddress())))
                .andExpect(jsonPath("$.[0].status", is(historyDto.getStatus().name())));

        verify(service, times(1)).findAllHistoryByPostalPackageId(anyLong());
    }

    @Test
    public void findStatusOfPostalPackageById() throws Exception {
        var historyDto = HistoryDto.builder()
                .id(1L)
                .officeAddress("address")
                .status(Status.ARRIVED)
                .build();
        when(service.findStatusOfPostalPackageById(anyLong()))
                .thenReturn(historyDto);

        mvc.perform(get("/packages/status/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(historyDto.getId()), Long.class))
                .andExpect(jsonPath("$.officeAddress", is(historyDto.getOfficeAddress())))
                .andExpect(jsonPath("$.status", is(historyDto.getStatus().name())));

        verify(service, times(1)).findStatusOfPostalPackageById(anyLong());
    }
}
