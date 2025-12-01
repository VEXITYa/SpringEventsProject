package dev.kuchishkin.repository;

import dev.kuchishkin.entity.EventEntity;
import dev.kuchishkin.enums.EventStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Modifying
    @Query("""
        UPDATE EventEntity e SET e.status = :status WHERE e.id = :eventId
        """)
    void changeEventStatus(
        @Param("eventId") Long eventId,
        @Param("eventStatus") EventStatus eventStatus
    );

    List<EventEntity> findAllByOwnerId(Long id);


    @Query("""
        SELECT e FROM EventEntity e
        WHERE (:name IS NULL OR e.name = :name)
        AND (:placesMin IS NULL OR e.maxPlaces >= :placesMin)
        AND (:placesMax IS NULL OR e.maxPlaces <= :placesMax)
        AND (CAST(:dateAfter as date) IS NULL OR e.date >= :dateAfter)
        AND (CAST(:dateBefore as date) IS NULL OR e.date <= :dateBefore)
        AND (:costMin IS NULL OR e.cost >= :costMin)
        AND (:costMax IS NULL OR e.cost >= :costMax)
        AND (:durationMin IS NULL OR e.duration >= :durationMin)
        AND (:durationMax IS NULL OR e.duration >= :durationMax)
        AND (:locationId IS NULL OR e.location.id = :locationId)
        AND (:eventStatus IS NULL OR e.status = :eventStatus)
        """)
    List<EventEntity> findEvents(
        @Param("name") String name,
        @Param("placesMin") Integer placesMin,
        @Param("placesMax") Integer placesMax,
        @Param("dateAfter") LocalDateTime dateAfter,
        @Param("dateBefore") LocalDateTime dateBefore,
        @Param("costMin") BigDecimal costMin,
        @Param("costMax") BigDecimal costMax,
        @Param("durationMin") Integer durationMin,
        @Param("durationMax") Integer durationMax,
        @Param("locationId") Integer locationId,
        @Param("eventStatus") EventStatus eventStatus
    );

    @Query("""
        SELECT e.id FROM EventEntity e
        WHERE e.date < CURRENT TIMESTAMP
        AND e.status = :eventStatus
        """)
    List<Long> findStartedEventsWithStatus(@Param("eventStatus") EventStatus eventStatus);

    @Query(value = """
        SELECT e.id FROM events e
        WHERE e.date + INTERVAL '1 minute' * e.duration < NOW()
          AND e.status = :eventStatus
        """, nativeQuery = true)
    List<Long> findFinishedEventsWithStatus(@Param("eventStatus") EventStatus eventStatus);
}
