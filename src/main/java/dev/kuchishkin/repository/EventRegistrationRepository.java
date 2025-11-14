package dev.kuchishkin.repository;

import dev.kuchishkin.entity.EventEntity;
import dev.kuchishkin.entity.EventRegistrationEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRegistrationRepository extends JpaRepository<EventRegistrationEntity, Long> {

    @Query("""
        SELECT e FROM EventRegistrationEntity e
        WHERE e.userId = :userId
        AND e.event.id = :eventId
        """)
    Optional<EventRegistrationEntity> findEventRegistration(
        @Param("userId") Long userId,
        @Param("eventId") Long eventId
    );

    @Query("""
        SELECT e.event FROM EventRegistrationEntity e
        WHERE e.userId = :userId
        """)
    List<EventEntity> findUserEventRegistrations(@Param("userId") Long userId);
}
