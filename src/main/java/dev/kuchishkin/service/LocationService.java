package dev.kuchishkin.service;


import dev.kuchishkin.entity.LocationEntity;
import dev.kuchishkin.entity_converters.LocationEntityConverter;
import dev.kuchishkin.model.Location;
import dev.kuchishkin.repository.LocationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationEntityConverter locationEntityConverter;

    public LocationService(LocationRepository locationRepository,
        LocationEntityConverter locationEntityConverter) {
        this.locationRepository = locationRepository;
        this.locationEntityConverter = locationEntityConverter;
    }

    public List<Location> findAll() {
        return locationRepository.findAll()
            .stream()
            .map(locationEntityConverter::toModel)
            .toList();
    }

    public Location findById(Long id) {
        return locationEntityConverter.toModel(locationRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Location with id " + id + " not found")));
    }

    public Location save(Location location) {
        return locationEntityConverter.toModel(
            locationRepository.save(locationEntityConverter.toEntity(location)));
    }

    public void deleteById(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new EntityNotFoundException("Location with id " + id + " not found");
        }
        locationRepository.deleteById(id);
    }

    @Transactional
    public Location update(Long id, Location location) {

        LocationEntity locationEntity = locationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                "Location with id " + location.id() + " not found"));

        if (location.capacity() < locationEntity.getCapacity()) {
            throw new IllegalArgumentException(
                "Location capacity cant be less than " + locationEntity.getCapacity());
        }

        locationEntity.setName(location.name());
        locationEntity.setAddress(location.address());
        locationEntity.setCapacity(location.capacity());
        locationEntity.setDescription(location.description());

        locationRepository.save(locationEntity);

        return locationEntityConverter.toModel(
            locationRepository.findById(id).get()
        );
    }
}
