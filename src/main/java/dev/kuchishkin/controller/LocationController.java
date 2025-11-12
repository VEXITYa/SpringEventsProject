package dev.kuchishkin.controller;


import dev.kuchishkin.dto.LocationDto;
import dev.kuchishkin.dto_converters.LocationDtoConverter;
import dev.kuchishkin.service.LocationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping(path = "/locations")
public class LocationController {
    private static final Logger log = LoggerFactory.getLogger(LocationController.class);

    private final LocationService locationService;
    private final LocationDtoConverter locationDtoConverter;

    public LocationController(LocationService locationService, LocationDtoConverter locationDtoConverter) {
        this.locationService = locationService;
        this.locationDtoConverter = locationDtoConverter;
    }

    @GetMapping
    public List<LocationDto> findAll() {
        log.info("Get Request findAllLocations");

        return locationService.findAll()
                .stream()
                .map(locationDtoConverter::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public LocationDto findById(@PathVariable Long id) {
        log.info("Get Request findByIdLocation: locationId={}", id);

        return locationDtoConverter.toDto(locationService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LocationDto> save(@RequestBody @Valid LocationDto locationDto) {
        log.info("Post Request locationDto={}", locationDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(locationDtoConverter.toDto(locationService.save(locationDtoConverter.toModel(locationDto))));
    }

    @PutMapping("/{id}")
    public LocationDto update(@PathVariable Long id, @RequestBody @Valid LocationDto locationDto) {
        log.info("Update Request locationDto={}", locationDto);

        return locationDtoConverter.toDto(locationService.update(id, locationDtoConverter.toModel(locationDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Delete Request locationId={}", id);

        locationService.deleteById(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
