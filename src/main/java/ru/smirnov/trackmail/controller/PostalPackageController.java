package ru.smirnov.trackmail.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.trackmail.dto.HistoryDto;
import ru.smirnov.trackmail.dto.PostalPackageDto;
import ru.smirnov.trackmail.service.PostalPackageService;

import java.util.List;

@RestController
@RequestMapping("/packages")
@AllArgsConstructor
@Slf4j
public class PostalPackageController {
    private final PostalPackageService postalPackageService;
    private static final String OFFICE_ID = "X-Sharer-Office-Id";
    private static final String POSTAL_PACKAGE_ID = "X-Sharer-Postal-Package-Id";

    @PostMapping("/register")
    public PostalPackageDto register(@RequestBody PostalPackageDto dto,
                                     @RequestHeader(OFFICE_ID) long officeId) {
        log.info("POST: /packages/register = controller register done");
        return postalPackageService.register(dto, officeId);
    }

    @PostMapping("/arrived")
    public PostalPackageDto arrivedToNextPostOffice(@RequestHeader(OFFICE_ID) long officeId,
                                                    @RequestHeader(POSTAL_PACKAGE_ID) long postalPackageId) {
        log.info("POST: /packages/arrived = controller arrivedToNextPostOffice");
        return postalPackageService.arrivedToNextPostOffice(postalPackageId, officeId);
    }

    @PostMapping("/departed")
    public PostalPackageDto departedFromPostOffice(@RequestHeader(OFFICE_ID) long officeId,
                                                   @RequestHeader(POSTAL_PACKAGE_ID) long postalPackageId) {
        log.info("POST: /package/departed = controller departedFromPostOffice");
        return postalPackageService.departedFromPostOffice(postalPackageId, officeId);
    }

    @PostMapping("/received")
    public PostalPackageDto receivedByAddressee(@RequestHeader(POSTAL_PACKAGE_ID) long postalPackageId) {
        log.info("POST: /package/received = controller receivedByAddressee");
        return postalPackageService.receivedByAddressee(postalPackageId);
    }

    @GetMapping("/{id}")
    public List<HistoryDto> findAllHistoryByPostalPackageId(@PathVariable long id,
                                                            @RequestParam(value = "from", required = false, defaultValue = "0") int from,
                                                            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        log.info("GET: /package/{} = controller findAllHistoryByPostalPackageId", id);
        return postalPackageService.findAllHistoryByPostalPackageId(id, from, size);
    }

    @GetMapping("/status/{id}")
    public HistoryDto findStatusOfPostalPackageById(@PathVariable long id) {
        log.info("GET: /package/status/{} = controller findStatusOfPostalPackageById", id);
        return postalPackageService.findStatusOfPostalPackageById(id);
    }
}
