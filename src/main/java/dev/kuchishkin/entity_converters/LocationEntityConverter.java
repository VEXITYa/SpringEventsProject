package dev.kuchishkin.entity_converters;


import dev.kuchishkin.entity.LocationEntity;
import dev.kuchishkin.model.Location;
import org.springframework.stereotype.Component;


@Component
public class LocationEntityConverter {

    public LocationEntity toEntity(Location location) {
        return new LocationEntity(
            location.id(),
            location.name(),
            location.address(),
            location.capacity(),
            location.description()
        );
    }

    public Location toModel(LocationEntity locationEntity) {
        return new Location(
            locationEntity.getId(),
            locationEntity.getName(),
            locationEntity.getAddress(),
            locationEntity.getCapacity(),
            locationEntity.getDescription()
        );
    }
}
