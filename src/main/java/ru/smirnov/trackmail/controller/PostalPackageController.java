package ru.smirnov.trackmail.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.smirnov.trackmail.dto.PostalPackageDto;
import ru.smirnov.trackmail.service.PostalPackageServiceImpl;

@RestController
@RequestMapping("/packages")
@AllArgsConstructor
@Slf4j
public class PostalPackageController {
    private final PostalPackageServiceImpl postalPackageService;

    @PostMapping("/register")
    public PostalPackageDto register(@RequestBody PostalPackageDto dto,
                                     @RequestHeader("X-Sharer-Office-Id") long officeId) {
        log.info("POST: /packages/register = controller register done");
        return postalPackageService.register(dto, officeId);
    }
}
