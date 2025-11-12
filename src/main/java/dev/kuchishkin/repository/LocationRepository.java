package dev.kuchishkin.repository;


import dev.kuchishkin.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

}
