package dev.kuchishkin.dto_converters;


import dev.kuchishkin.dto.LocationDto;
import dev.kuchishkin.model.Location;
import org.springframework.stereotype.Component;


@Component
public class LocationDtoConverter {

    public LocationDto toDto(Location location) {
        return new LocationDto(
            location.id(),
            location.name(),
            location.address(),
            location.capacity(),
            location.description()
        );
    }

    public Location toModel(LocationDto locationDto) {
        return new Location(
            locationDto.id(),
            locationDto.name(),
            locationDto.address(),
            locationDto.capacity(),
            locationDto.description()
        );
    }
}
